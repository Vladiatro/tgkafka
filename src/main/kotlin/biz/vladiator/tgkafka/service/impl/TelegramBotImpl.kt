package biz.vladiator.tgkafka.service.impl

import biz.vladiator.tgkafka.service.TelegramBot
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update


/**
 * Controls the bot: registers new users and reads user messages to send them to Kafka.
 */
@Service
class TelegramBotImpl(options: DefaultBotOptions?, @Value("\${tgkafka.bot.username}") botUsername: String)
    : TelegramLongPollingCommandBot(options, botUsername), TelegramBot, InitializingBean {

    @Value("\${tgkafka.bot.token}")
    private lateinit var botToken: String

    @Autowired
    @Qualifier("startCommand")
    private lateinit var startCommand: IBotCommand

    @Autowired
    @Qualifier("publishCommand")
    private lateinit var publishCommand: IBotCommand

    @Autowired
    @Qualifier("helpCommand")
    private lateinit var helpCommand: IBotCommand


    override fun getBotToken(): String = botToken

    override fun afterPropertiesSet() {
        register(startCommand)
        register(publishCommand)
        register(helpCommand)
    }

    /**
     * Send something in response for feedback
     */
    override fun processNonCommandUpdate(update: Update?) {
        if (update?.message?.hasText()!!) {
            sendMessage(update.message.chatId, "Бот работает")
        }
    }

    override fun sendMessage(chatId: Long, text: String) {
        val message = SendMessage()
                .setChatId(chatId)
                .setText(text)
        execute(message)
    }

}