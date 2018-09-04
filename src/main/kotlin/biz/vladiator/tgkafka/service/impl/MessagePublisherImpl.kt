package biz.vladiator.tgkafka.service.impl

import biz.vladiator.tgkafka.Message.TelegramMessage
import biz.vladiator.tgkafka.service.MessagePublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

/**
 * Publishes messages to Kafka.
 */
@Service
class MessagePublisherImpl : MessagePublisher {
    @Autowired
    private lateinit var template: KafkaTemplate<String, ByteArray>;

    @Value("\${tgkafka.topic}")
    private lateinit var topic: String;

    override fun publish(content: String) {
        val telegramMessage = TelegramMessage.newBuilder()
                .setContent(content)
                .build()
        template.send(topic, telegramMessage.toByteArray())
    }

    override fun publish(message: TelegramMessage) {
        template.send(topic, message.toByteArray())
    }
}