package com.hs.application.handler

import com.hs.entity.Product
import com.hs.application.handler.event.ProductChangeConfirmStatusEvent
import com.hs.application.handler.event.ProductCreateAndUpdateEvent
import com.hs.application.handler.event.ProductDecreaseStockQuantityEvent
import com.hs.application.handler.event.ProductUpdateImageEvent
import com.hs.infrastructure.jpa.persistence.ProductEventLogPersistence
import com.hs.infrastructure.jpa.repository.ProductEventLogSpringDataJpaRepository
import com.hs.infrastructure.rabbitmq.ProductQueuePublisher
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
    private val productQueuePublisher: ProductQueuePublisher,
    private val productEventLogRepository: ProductEventLogSpringDataJpaRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onHandleCreateAndUpdateProduct(event: ProductCreateAndUpdateEvent) = runBlocking {
        launch(Dispatchers.IO) {
            logger.info("onHandleCreateAndUpdateProduct() method is executed : {}", event)

            publishProductEvent(productId = event.productId)

            createProductEventLog(
                productId = event.productId,
                eventStatus= event.status,
                message = event.toString()
            )
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onHandleDecreaseStockQuantity(event: ProductDecreaseStockQuantityEvent) = runBlocking {
        launch(Dispatchers.IO) {
            logger.info("onHandleDecreaseStockQuantity() method is executed : {}", event)

            publishProductEvent(productId = event.productId)

            createProductEventLog(
                productId = event.productId,
                eventStatus = event.status,
                message = event.toString()
            )
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onHandleChangeConfirmStatus(event: ProductChangeConfirmStatusEvent) = runBlocking {
        launch(Dispatchers.IO) {
            logger.info("onHandleChangeConfirmStatus() method is executed : {}", event)

            publishProductEvent(productId = event.productId)

            createProductEventLog(
                productId = event.productId,
                eventStatus = event.status,
                message = event.toString()
            )
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onHandleUpdateImage(event: ProductUpdateImageEvent) = runBlocking {
        launch(Dispatchers.IO) {
            logger.info("onHandleUpdateImage() method is executed : {}", event)

            publishProductEvent(productId = event.productId)

            createProductEventLog(
                productId = event.productId,
                eventStatus = event.status,
                message = event.toString()
            )
        }
    }

    /*
    * suspend 키워드가 있으면, 코루틴 컨텍스트 환경에서만 실행할 수 있다는 의미이다. 만약, 해당 키워드를 붙이지 않으면,
    * 어디에서나 실행할 수 있는 일반 메소드이다.
    * */
    suspend fun publishProductEvent(productId: Long) {
        productQueuePublisher.publish(productId = productId)
    }

    /*
    * suspend 키워드가 있으면, 코루틴 컨텍스트 환경에서만 실행할 수 있다는 의미이다. 만약, 해당 키워드를 붙이지 않으면,
    * 어디에서나 실행할 수 있는 일반 메소드이다.
    * */
    suspend fun createProductEventLog(productId: Long, eventStatus: Product.EventStatus, message: String) {
        productEventLogRepository.save(
            ProductEventLogPersistence.create(
                productId = productId,
                eventStatus = eventStatus,
                message = message
            )
        )
    }
}
