package biz.vladiator.tgkafka.data.repositories

import biz.vladiator.tgkafka.data.entities.TelegramChat
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * MongoDB repository of registered chats where to send messages from Kafka.
 */
interface ChatsRepository : MongoRepository<TelegramChat, Long>