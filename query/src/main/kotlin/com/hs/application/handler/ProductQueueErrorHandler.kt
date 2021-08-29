package com.hs.application.handler

import com.hs.config.rabbitmq.RabbitMqConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component(value = "productQueueErrorHandler")
class ProductQueueErrorHandler(
    @Value("\${spring.rabbitmq.listener.simple.retry.max-attempts}")
    private val maxAttempts: Int,
    private val rabbitTemplate: RabbitTemplate,
) : RabbitListenerErrorHandler {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun handleError(
        amqpMessage: Message,
        message: org.springframework.messaging.Message<*>,
        exception: ListenerExecutionFailedException
    ) {
        logger.error("localizedMessage : {}", exception.cause?.localizedMessage)

        val headers: MutableMap<String, Any> = amqpMessage.messageProperties.headers

        if (headers.containsKey("retryCount")) {
            headers["retryCount"] = headers["retryCount"] as Int + 1
        } else {
            headers["retryCount"] = 1
        }

        logger.error("retryCount : {}", headers["retryCount"])

        if (headers["retryCount"] as Int >= maxAttempts) {
            rabbitTemplate.send(RabbitMqConfig.QueueName.PRODUCT_DLQ, amqpMessage)
        }
    }
}
