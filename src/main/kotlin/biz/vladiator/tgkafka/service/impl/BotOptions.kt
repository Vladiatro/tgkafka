package biz.vladiator.tgkafka.service.impl

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultBotOptions
import java.net.Authenticator
import java.net.PasswordAuthentication

/**
 * Configures proxy from the parameters, to be instantiated into [TelegramBotImpl].
 */
@Service
class BotOptions : DefaultBotOptions(), InitializingBean {

    @Value("\${tgkafka.bot.proxy.type}")
    private lateinit var configProxyType: ProxyType

    @Value("\${tgkafka.bot.proxy.host:@null}")
    private var configProxyHost: String? = null

    @Value("\${tgkafka.bot.proxy.port:0}")
    private var configProxyPort: Int = 0

    @Value("\${tgkafka.bot.proxy.login:@null}")
    var proxyLogin : String? = null
    @Value("\${tgkafka.bot.proxy.password:@null}")
    var proxyPassword : String? = null

    override fun afterPropertiesSet() {
        // initialize proxy
        if (configProxyType != ProxyType.NO_PROXY) {
            Authenticator.setDefault(object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(proxyLogin, proxyPassword?.toCharArray())
                }
            })
        }
    }

    override fun getProxyType(): ProxyType = configProxyType

    override fun getProxyHost(): String? = configProxyHost

    override fun getProxyPort(): Int = configProxyPort
}