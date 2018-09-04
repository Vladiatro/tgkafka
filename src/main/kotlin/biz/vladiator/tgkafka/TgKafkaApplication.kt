package biz.vladiator.tgkafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.telegram.telegrambots.ApiContextInitializer


@SpringBootApplication
class TgKafkaApplication

fun main(args: Array<String>) {
    // initialize Telegram API Context
    ApiContextInitializer.init();

    runApplication<TgKafkaApplication>(*args)
}
