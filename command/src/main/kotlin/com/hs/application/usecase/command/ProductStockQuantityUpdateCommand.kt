package com.hs.application.usecase.command

import com.hs.entity.Product
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductStockQuantityUpdateCommand(
    private val publisher: ApplicationEventPublisher
) {

    fun execute(id: Long, completeStockQuantity: Int, product: Product) {
        product.changeStockCount(stockQuantity = completeStockQuantity, publisher = publisher)
    }
}
