package biz.vladiator.tgkafka.service.impl.commands

import biz.vladiator.tgkafka.service.MessagePublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * /publish command: publish a message to Kafka
 */
@Service("publishCommand")
class PublishCommandImpl : BotCommand("publish", "Опубликовать сообщение в Kafka") {
    @Autowired
    private lateinit var messagePublisher: MessagePublisher

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        if (!arguments?.isEmpty()!!) {
            messagePublisher.publish(arguments.joinToString(separator = " "))
        } else {
            absSender?.execute(SendMessage(chat?.id, "Укажите сообщение после /publish."))
        }
    }
}
