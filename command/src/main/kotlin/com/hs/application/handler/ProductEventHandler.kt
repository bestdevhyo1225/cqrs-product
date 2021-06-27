package com.hs.application.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.hs.dto.PublishProductDto
import com.hs.entity.ProductCommandCode
import com.hs.entity.ProductEventLog
import com.hs.event.ProductChangeConfirmStatusEvent
import com.hs.event.ProductCreateAndUpdateEvent
import com.hs.event.ProductDecreaseStockQuantityEvent
import com.hs.repository.ProductEventLogRepository
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
class ProductEventHandler(
    private val objectMapper: ObjectMapper,
    private val productQueuePublisher: ProductQueuePublisher,
    private val productEventLogRepository: ProductEventLogRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onHandleCreateAndUpdateProduct(event: ProductCreateAndUpdateEvent) = runBlocking {
        logger.info("[ Handler - onHandleCreateAndUpdateProduct() ] event : {}", event)

        launch(Dispatchers.IO) {
            productQueuePublisher.publish(
                body = objectMapper.writeValueAsString(PublishProductDto(productId = event.productId)).toByteArray()
            )

            createProductEventLog(
                productId = event.productId,
                productCommandCode = event.productCommandCode,
                message = event.toString()
            )
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onHandleDecreaseStockQuantity(event: ProductDecreaseStockQuantityEvent) = runBlocking {
        logger.info("[ Handler - onHandleDecreaseStockQuantity() ] event : {}", event)

        launch(Dispatchers.IO) {
            productQueuePublisher.publish(
                body = objectMapper.writeValueAsString(PublishProductDto(productId = event.productId)).toByteArray()
            )

            createProductEventLog(
                productId = event.productId,
                productCommandCode = event.productCommandCode,
                message = event.toString()
            )
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onHandleChangeConfirmStatus(event: ProductChangeConfirmStatusEvent) = runBlocking {
        logger.info("[ Handler - onHandleChangeConfirmStatus() ] event : {}", event)

        launch(Dispatchers.IO) {
            productQueuePublisher.publish(
                body = objectMapper.writeValueAsString(PublishProductDto(productId = event.productId)).toByteArray()
            )

            createProductEventLog(
                productId = event.productId,
                productCommandCode = event.productCommandCode,
                message = event.toString()
            )
        }
    }

    suspend fun createProductEventLog(productId: Long, productCommandCode: ProductCommandCode, message: String) {
        productEventLogRepository.save(
            ProductEventLog(productId = productId, productCommandCode = productCommandCode, message = message)
        )
    }
}
