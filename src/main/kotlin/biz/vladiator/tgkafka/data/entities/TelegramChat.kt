package biz.vladiator.tgkafka.data.entities

import org.springframework.data.annotation.Id

/**
 * Representation of Telegram chat which contains just its id.
 */
class TelegramChat() {
    @Id
    var chatId: Long = 0

    constructor(chatId: Long) : this() {
        this.chatId = chatId
    }
}