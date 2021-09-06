package com.hs.application.handler

import com.rabbitmq.client.Channel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.stereotype.Component

@Component(value = "productQueueErrorHandler")
class ProductQueueErrorHandler : RabbitListenerErrorHandler {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun handleError(
        amqpMessage: Message,
        message: org.springframework.messaging.Message<*>,
        exception: ListenerExecutionFailedException
    ) {
        logger.error("localizedMessage : {}", exception.cause?.localizedMessage)

        val channel = message.headers.get(AmqpHeaders.CHANNEL, Channel::class.java) as Channel

        channel.basicReject(amqpMessage.messageProperties.deliveryTag, false)
    }
}
