package biz.vladiator.tgkafka.service.impl.commands

import biz.vladiator.tgkafka.data.entities.TelegramChat
import biz.vladiator.tgkafka.data.repositories.ChatsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender


/**
 * /start command: register chat to the database
 */
@Service("startCommand")
class StartCommandImpl : BotCommand("start", "Начать использование бота и зарегистрировать чат") {

    @Autowired
    private lateinit var chatsRepository: ChatsRepository

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        val answer = SendMessage()
        val chatId = chat?.id!!
        answer.setChatId(chatId)
        if (chatsRepository.existsById(chatId)) {
            answer.text = "Ты уже был зарегистрирован в системе"
        } else {
            chatsRepository.insert(TelegramChat(chatId))
            answer.text = "Ты зарегистрирован в системе"
        }
        absSender?.execute(answer)
    }
}