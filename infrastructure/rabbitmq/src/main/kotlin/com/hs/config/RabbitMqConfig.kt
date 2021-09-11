package com.hs.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
@EnableRabbit
class RabbitMqConfig(
    @Value("\${spring.rabbitmq.host}")
    private val host: String,

    @Value("\${spring.rabbitmq.port}")
    private val port: Int,

    @Value("\${spring.rabbitmq.username}")
    private val username: String,

    @Value("\${spring.rabbitmq.password}")
    private val password: String,

    @Value("\${spring.rabbitmq.ssl.enabled}")
    private val sslEnabled: Boolean,
) {
    object QueueName {
        const val PRODUCT = "product.queue"
    }

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val factory = CachingConnectionFactory()
        val protocol = if (sslEnabled) "amqps" else "amqp"

        factory.setUri("${protocol}://${host}")
        factory.port = this.port
        factory.username = this.username
        factory.setPassword(this.password)

        return factory
    }

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper().findAndRegisterModules()

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        return objectMapper
    }

    @Bean
    fun jsonMessageConverter(objectMapper: ObjectMapper): MessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory, jsonMessageConverter: MessageConverter): RabbitTemplate? {
        val rabbitTemplate = RabbitTemplate(connectionFactory)

        rabbitTemplate.messageConverter = jsonMessageConverter

        return rabbitTemplate
    }
}
