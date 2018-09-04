package biz.vladiator.tgkafka.service.impl

import biz.vladiator.tgkafka.Message.TelegramMessage
import biz.vladiator.tgkafka.data.repositories.ChatsRepository
import biz.vladiator.tgkafka.service.MessageListener
import biz.vladiator.tgkafka.service.MessagePublisher
import biz.vladiator.tgkafka.service.TelegramBot
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException

// when errors occur, pause message consuming for some exponential progressive time in milliseconds
const val SLEEP_MULTIPLIER = 1.5

/**
 * Listens to a kafka topic and sends received messages to Telegram.
 */
@Service
class MessageListenerImpl : MessageListener {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var telegramBot: TelegramBot

    @Autowired
    private lateinit var chatsRepository: ChatsRepository

    @Autowired
    private lateinit var messagePublisher: MessagePublisher

    @Value("\${tgkafka.onerror.mindelay}")
    private var errorMinDelay: Long = 1000

    @Value("\${tgkafka.onerror.maxdelay}")
    private var errorMaxDelay: Long = 20000

    @Value("\${tgkafka.onerror.mindelay}")
    private var errorCurrentDelay: Long = 1000

    @KafkaListener(topics = ["\${tgkafka.topic}"])
    fun receive(record: ConsumerRecord<String, ByteArray>, consumer: KafkaConsumer<String, ByteArray>) {
        val message = TelegramMessage.parseFrom(record.value())
        logger.info(message.content)
        var messageReceived = false
        for (telegramChat in chatsRepository.findAll()) {
            try {
                telegramBot.sendMessage(telegramChat.chatId, message.content)
                messageReceived = true
            } catch (e: TelegramApiException) {
                if (e is TelegramApiRequestException && e.errorCode == 403) {
                    // possibly user had removed the bot, so delete the conversation from the database
                    chatsRepository.delete(telegramChat)
                } else {
                    logger.error("Error occurred", e)
                }
            }
        }
        // no Telegram user received this message, send it back and block messages receiving for some time
        if (messageReceived) {
            errorCurrentDelay = errorMinDelay
        } else {
            logger.info("No users received the message \"{}\", returning it back to Kafka", message.content)
            // send back to Kafka
            messagePublisher.publish(message)

            // pause
            consumer.pause(consumer.assignment())
            Thread.sleep(errorCurrentDelay)
            errorCurrentDelay = Math.min(errorMaxDelay, (errorCurrentDelay * SLEEP_MULTIPLIER).toLong())
            consumer.resume(consumer.paused())
        }
    }
}
