package com.hs.application.handler

import com.hs.entity.Product
import com.hs.application.handler.event.ProductChangeConfirmStatusEvent
import com.hs.application.handler.event.ProductCreateAndUpdateEvent
import com.hs.application.handler.event.ProductDecreaseStockQuantityEvent
import com.hs.application.handler.event.ProductUpdateImageEvent
import com.hs.publisher.ProductQueuePublisher
import com.hs.repository.ProductEventLogRepository
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
    private val productEventLogRepository: ProductEventLogRepository
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
    * suspend ???????????? ?????????, ????????? ???????????? ??????????????? ????????? ??? ????????? ????????????. ??????, ?????? ???????????? ????????? ?????????,
    * ??????????????? ????????? ??? ?????? ?????? ???????????????.
    * */
    suspend fun publishProductEvent(productId: Long) {
        productQueuePublisher.publish(productId = productId)
    }

    /*
    * suspend ???????????? ?????????, ????????? ???????????? ??????????????? ????????? ??? ????????? ????????????. ??????, ?????? ???????????? ????????? ?????????,
    * ??????????????? ????????? ??? ?????? ?????? ???????????????.
    * */
    suspend fun createProductEventLog(productId: Long, eventStatus: Product.EventStatus, message: String) {
        productEventLogRepository.save(productId = productId, eventStatus = eventStatus, message = message)
    }
}
