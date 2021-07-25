package com.hs.web.amqp

import com.hs.application.usecase.ProductAggregateCommand
import com.hs.config.RabbitMqConfig
import com.hs.dto.PublishProductDto
import com.rabbitmq.client.Channel

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ProductQueueListener(
    private val productAggregateCommand: ProductAggregateCommand
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @RabbitListener(id = "product", queues = [RabbitMqConfig.QueueName.PRODUCT])
    fun consume(publishProductDto: PublishProductDto, channel: Channel, message: Message) {
        logger.info("[ Queue Listener ] channel : {}", channel)
        logger.info("[ Queue Listener ] message : {}", message)
        logger.info("[ Queue Listener ] publishProductDto : {}", publishProductDto)

        productAggregateCommand.createOrUpdate(productId = publishProductDto.productId)

        channel.basicAck(message.messageProperties.deliveryTag, false)
    }
}
