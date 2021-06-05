package com.hs.handler.event

import com.hs.dto.PublishProductDto
import com.hs.entity.EventSendLog
import com.hs.event.ProductEvent
import com.hs.handler.publisher.ProductQueuePublisher
import com.hs.repository.EnumSendLogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CommandEventHandler(
    private val productQueuePublisher: ProductQueuePublisher,
    private val eventSendLogRepository: EnumSendLogRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onHandleProduct(event: ProductEvent) = runBlocking {
        async(Dispatchers.IO) {
            logger.info("[ Handler - onHandleProduct() ] event : {}", event)

            eventSendLogRepository.save(EventSendLog(commandCode = event.commandCode, message = event.toString()))

            productQueuePublisher.publish(PublishProductDto(productId = event.productId))
        }
    }
}