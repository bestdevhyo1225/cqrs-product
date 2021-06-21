package com.hs.web.amqp

import com.hs.config.RabbitMQConfig
import com.hs.dto.PublishProductDto
import com.hs.application.usecase.ProductAggregateManager
import com.rabbitmq.client.Channel

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ProductQueueListener(
    private val productAggregateManager: ProductAggregateManager
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @RabbitListener(id = "product", queues = [RabbitMQConfig.QueueName.PRODUCT])
    fun consume(publishProductDto: PublishProductDto, channel: Channel, message: Message) {
        logger.info("[ Queue Listener ] publishProductDto : {}", publishProductDto)

        productAggregateManager.createOrUpdate(productId = publishProductDto.productId)

        channel.basicAck(message.messageProperties.deliveryTag, false)
    }
}
