package biz.vladiator.tgkafka.service

import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.meta.generics.LongPollingBot

/**
 * Controls the bot: registers new users and reads user messages to send them to Kafka.
 */
interface TelegramBot : LongPollingBot {
    fun sendMessage(chatId: Long, text: String)

    fun getRegisteredCommands(): Collection<IBotCommand>
}