package com.hs.application.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.hs.dto.PublishProductDto
import com.hs.entity.PublishedEventLog
import com.hs.event.ProductEvent
import com.hs.repository.PublishedEventLogRepository
import com.hs.service.ProductQueuePublisher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CommandEventHandler(
    private val objectMapper: ObjectMapper,
    private val productQueuePublisher: ProductQueuePublisher,
    private val publishedEventLogRepository: PublishedEventLogRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onHandleProduct(event: ProductEvent) = runBlocking {
        logger.info("[ Handler - onHandleProduct() ] event : {}", event)

        launch(Dispatchers.IO) {
            productQueuePublisher.publish(
                body = objectMapper.writeValueAsString(PublishProductDto(productId = event.productId)).toByteArray()
            )

            publishedEventLogRepository.save(
                PublishedEventLog(commandCode = event.commandCode, message = event.toString())
            )
        }
    }
}
