package com.hs.application.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.hs.application.usecase.EventLogCommandProcessor
import com.hs.dto.PublishProductDto
import com.hs.event.ProductEvent
import com.hs.publisher.ProductQueuePublisher
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
    private val eventLogCommandProcessor: EventLogCommandProcessor,
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onHandleProduct(event: ProductEvent) = runBlocking {
        launch(Dispatchers.IO) {
            logger.info("[ Handler - onHandleProduct() ] event : {}", event)

            productQueuePublisher.publish(
                body = objectMapper.writeValueAsString(PublishProductDto(productId = event.productId)).toByteArray()
            )

            eventLogCommandProcessor.createPublishedEventLog(event = event)
        }
    }
}
