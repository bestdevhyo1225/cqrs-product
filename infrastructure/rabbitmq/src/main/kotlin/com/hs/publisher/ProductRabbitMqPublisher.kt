package com.hs.publisher

import com.fasterxml.jackson.databind.ObjectMapper
import com.hs.config.RabbitMqConfig
import com.hs.event.PublishProductEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.core.MessagePropertiesBuilder
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class ProductRabbitMqPublisher(
    private val objectMapper: ObjectMapper,
    private val rabbitTemplate: RabbitTemplate
) : ProductQueuePublisher {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun publish(productId: Long) {
        logger.info("publish() method is executed")

        val properties = MessagePropertiesBuilder.newInstance()
            .setContentType("application/json")
            .build()

        val body = objectMapper.writeValueAsBytes(PublishProductEvent(productId = productId))

        val message = MessageBuilder
            .withBody(body)
            .andProperties(properties)
            .build()

        rabbitTemplate.convertAndSend(RabbitMqConfig.QueueName.PRODUCT, message)
    }
}
