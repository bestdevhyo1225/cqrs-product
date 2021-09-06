package com.hs.adapter.amqp

import com.hs.adapter.amqp.event.ConsumeProductEvent
import com.hs.application.usecase.ProductAggregateCommand
import com.hs.config.rabbitmq.RabbitMqConfig
import com.rabbitmq.client.Channel

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class ProductQueueListener(
    private val rabbitTemplate: RabbitTemplate,
    private val productAggregateCommand: ProductAggregateCommand
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @RabbitListener(
        id = "product",
        queues = [RabbitMqConfig.QueueName.PRODUCT],
        errorHandler = "productQueueErrorHandler"
    )
    fun consumeProductQueue(consumeProductEvent: ConsumeProductEvent, channel: Channel, message: Message) {
        logger.info("message : {}", message)

        productAggregateCommand.createOrUpdate(productId = consumeProductEvent.productId)

        channel.basicAck(message.messageProperties.deliveryTag, false)
    }

    @RabbitListener(id = "product-dead-letter-queue", queues = [RabbitMqConfig.QueueName.PRODUCT_DLQ])
    fun consumeProductDeadLetterQueue(consumeProductEvent: ConsumeProductEvent, channel: Channel, message: Message) {
        val deliveryTag: Long = message.messageProperties.deliveryTag
        val xDeathHeader: Map<String, *> = message.messageProperties.xDeathHeader.first()
        val xDeathCount = xDeathHeader["count"] as Long
        val queue = xDeathHeader["queue"] as String

        if (xDeathCount >= RabbitMqConfig.Retry.MAX_ATTEMPT) {
            logger.error("Message exceed retry max attempt (message : {})", message)

            channel.basicReject(deliveryTag, false)
        } else {
            logger.warn("Send from ProductDeadLetterQueue to ProductQueue (xDeathCount: {})", xDeathCount)

            rabbitTemplate.send(queue, message)

            channel.basicAck(deliveryTag, false)
        }
    }
}
