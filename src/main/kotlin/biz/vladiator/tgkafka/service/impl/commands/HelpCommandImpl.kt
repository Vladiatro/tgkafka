package biz.vladiator.tgkafka.service.impl.commands

import biz.vladiator.tgkafka.service.TelegramBot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender


/**
 * /help command: show help
 */
@Service("helpCommand")
class HelpCommandImpl : BotCommand("help", "Показать справку") {

    @Autowired
    private lateinit var telegramBot: TelegramBot

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        val answer = SendMessage()
        val chatId = chat?.id
        answer.setChatId(chatId)
        val stringBuilder = StringBuilder("<b>Помощь</b>\n")
        stringBuilder.append("Список команд:\n\n")
        telegramBot.getRegisteredCommands().forEach{ stringBuilder.append(it.toString()).append("\n\n") }
        answer.enableHtml(true)
        answer.text = stringBuilder.toString()
        absSender?.execute(answer)
    }

}