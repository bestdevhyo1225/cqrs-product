package com.hs.handler.publisher

import com.fasterxml.jackson.databind.ObjectMapper
import com.hs.config.RabbitMQConfig
import com.hs.dto.PublishProductDto
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.core.MessagePropertiesBuilder
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class ProductQueuePublisher(
    private val objectMapper: ObjectMapper,
    private val rabbitTemplate: RabbitTemplate,
) {

    fun publish(publishProductDto: PublishProductDto) {
        val properties = MessagePropertiesBuilder.newInstance()
            .setContentType("application/json")
            .build()

        val message = MessageBuilder.withBody(objectMapper.writeValueAsString(publishProductDto).toByteArray())
            .andProperties(properties)
            .build()

        rabbitTemplate.convertAndSend(RabbitMQConfig.QueueName.PRODUCT, message)
    }
}
