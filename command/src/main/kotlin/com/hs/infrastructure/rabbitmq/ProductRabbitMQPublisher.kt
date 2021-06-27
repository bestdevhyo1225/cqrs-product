package com.hs.infrastructure.rabbitmq

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.hs.config.RabbitMQConfig
import com.hs.dto.PublishProductDto
import com.hs.service.ProductQueuePublisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.core.MessagePropertiesBuilder
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class ProductRabbitMQPublisher(private val rabbitTemplate: RabbitTemplate) : ProductQueuePublisher {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    /*
    * suspend 키워드가 있으면, 코루틴 컨텍스트 환경에서만 실행할 수 있다는 의미이다. 만약, 해당 키워드를 붙이지 않으면,
    * 어디에서나 실행할 수 있는 일반 메소드이다.
    * */
    override fun publish(publishProductDto: PublishProductDto) {
        logger.info("publish() method is executed")

        val body = ObjectMapper().writeValueAsBytes(PublishProductDto(productId = publishProductDto.productId))

        val properties = MessagePropertiesBuilder.newInstance()
            .setContentType("application/json")
            .build()

        val message = MessageBuilder
            .withBody(body)
            .andProperties(properties)
            .build()

        rabbitTemplate.convertAndSend(RabbitMQConfig.QueueName.PRODUCT, message)
    }
}
