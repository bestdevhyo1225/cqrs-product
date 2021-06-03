package com.hs.handler

import com.hs.event.ChangeProductEvent
import com.hs.event.ChangeProductStockEvent
import com.hs.event.CreateProductAggregateEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CommandEventHandler {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onCreateProduct(event: CreateProductAggregateEvent) = runBlocking {
        async(Dispatchers.IO) {
            logger.info("[ Handler - onCreateProduct() ] event : {}", event)
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onUpdateProduct(event: ChangeProductEvent) = runBlocking {
        async(Dispatchers.IO) {
            logger.info("[ Handler - onUpdateProduct() ] event : {}", event)
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onUpdateProductStock(event: ChangeProductStockEvent) = runBlocking {
        async(Dispatchers.IO) {
            logger.info("[ Handler - onUpdateProductStock() ] event : {}", event)
        }
    }
}
