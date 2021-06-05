package com.hs.handler.listener

import com.hs.config.RabbitMQConfig
import com.hs.dto.FindProductAggregateDto
import com.hs.dto.PublishProductDto
import com.rabbitmq.client.Channel
import kotlinx.coroutines.runBlocking

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

inline fun <reified T : Any> typeRef(): ParameterizedTypeReference<T> = object : ParameterizedTypeReference<T>() {}

@Component
class ProductQueueListener(
    private val restTemplate: RestTemplate
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @RabbitListener(id = "product", queues = [RabbitMQConfig.QueueName.PRODUCT])
    fun consumeQueue(publishProductDto: PublishProductDto, channel: Channel, message: Message) {
        logger.info("publishProductDto : {}", publishProductDto)

        channel.basicAck(message.messageProperties.deliveryTag, false)

//        val httpHeaders = HttpHeaders()
//        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//
//        val responseEntity: ResponseEntity<FindProductAggregateDto> = restTemplate.exchange(
//            "http://localhost:9700/products/${publishProductDto.productId}",
//            HttpMethod.GET,
//            HttpEntity(null, httpHeaders),
//            FindProductAggregateDto::class.java
//        )
//
//        logger.info("responseEntity : {}", responseEntity)
    }
}
