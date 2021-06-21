package com.hs.infrastructure.rabbitmq

import com.hs.infrastructure.config.RabbitMQConfig
import com.hs.dto.FindProductDto
import com.hs.dto.PublishProductDto
import com.hs.response.SuccessResponse
import com.hs.application.usecase.ProductAggregateCommandProcessor
import com.hs.infrastructure.rest.RestGetRequestor
import com.rabbitmq.client.Channel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ProductQueueListener(
    private val restGetRequestor: RestGetRequestor,
    private val productAggregateCommandProcessor: ProductAggregateCommandProcessor
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @RabbitListener(id = "product", queues = [RabbitMQConfig.QueueName.PRODUCT])
    fun consume(publishProductDto: PublishProductDto, channel: Channel, message: Message) = runBlocking {
        logger.info("[ Queue Listener ] publishProductDto : {}", publishProductDto)

        launch(Dispatchers.IO) {
            val responseEntity: ResponseEntity<SuccessResponse<FindProductDto>> =
                restGetRequestor.getProductAggregate(productId = publishProductDto.productId)

            productAggregateCommandProcessor.createOrUpdate(productDto = responseEntity.body!!.data)
        }

        channel.basicAck(message.messageProperties.deliveryTag, false)
    }
}
