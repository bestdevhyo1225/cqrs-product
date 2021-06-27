package com.hs.infrastructure.rabbitmq

import com.fasterxml.jackson.databind.ObjectMapper
import com.hs.config.RabbitMQConfig
import com.hs.dto.PublishProductDto
import com.hs.service.ProductQueuePublisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.core.MessagePropertiesBuilder
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class ProductRabbitMQPublisher(
    private val objectMapper: ObjectMapper,
    private val rabbitTemplate: RabbitTemplate
) : ProductQueuePublisher {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun publish(publishProductDto: PublishProductDto) {
        logger.info("publish() method is executed")

        val properties = MessagePropertiesBuilder.newInstance()
            .setContentType("application/json")
            .build()

        val body = objectMapper.writeValueAsBytes(PublishProductDto(productId = publishProductDto.productId))

        val message = MessageBuilder
            .withBody(body)
            .andProperties(properties)
            .build()

        rabbitTemplate.convertAndSend(RabbitMQConfig.QueueName.PRODUCT, message)
    }
}
