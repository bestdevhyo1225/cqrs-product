package com.hs.handler.listener

import com.hs.config.RabbitMQConfig
import com.hs.dto.FindProductAggregateDto
import com.hs.dto.PublishProductDto
import com.hs.handler.external.CommandApiCallHandler
import com.hs.response.SuccessResponse
import com.hs.usecase.ProductAggregateCommand
import com.rabbitmq.client.Channel
import kotlinx.coroutines.runBlocking

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ProductQueueListener(
    private val commandApiCallHandler: CommandApiCallHandler,
    private val productAggregateCommand: ProductAggregateCommand,
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @RabbitListener(id = "product", queues = [RabbitMQConfig.QueueName.PRODUCT])
    fun consumeQueue(publishProductDto: PublishProductDto, channel: Channel, message: Message) = runBlocking {
        logger.info("[ Queue Listener ] publishProductDto : {}", publishProductDto)

        val responseEntity: ResponseEntity<SuccessResponse<FindProductAggregateDto>> =
            commandApiCallHandler.getProductAggregate(url = "http://localhost:9700/products/${publishProductDto.productId}")

        productAggregateCommand.createOrUpdate(productAggregateDto = responseEntity.body!!.data)

        channel.basicAck(message.messageProperties.deliveryTag, false)
    }
}
