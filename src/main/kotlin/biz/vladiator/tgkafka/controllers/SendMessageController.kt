package biz.vladiator.tgkafka.controllers

import biz.vladiator.tgkafka.service.MessagePublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * An easy way to send a message to kafka (/sendMessage?text=message).
 */
@RestController
class SendMessageController {
    @Autowired
    private lateinit var messagePublisher: MessagePublisher;

    @RequestMapping(value = ["/sendMessage"], produces = ["application/json"])
    fun sendMessage(@RequestParam(value = "text") content: String): ResponseEntity<Map<String, Any?>> {
        try {
            messagePublisher.publish(content)
            return ResponseEntity(mapOf("result" to "success"), HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(mapOf("result" to "error", "message" to e.message),
                    HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}