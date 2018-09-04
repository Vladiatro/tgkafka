package biz.vladiator.tgkafka.service

import biz.vladiator.tgkafka.Message

/**
 * Publishes messages to Kafka.
 */
interface MessagePublisher {
    fun publish(content: String)

    fun publish(message: Message.TelegramMessage)
}