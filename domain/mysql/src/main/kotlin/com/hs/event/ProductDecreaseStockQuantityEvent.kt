package com.hs.event

import com.hs.entity.Product

data class ProductDecreaseStockQuantityEvent(
    val productId: Long,
    val status: Product.EventStatus,
    val currentStockQuantity: Int,
) {
    override fun toString(): String = "Product(stockQuantity=${currentStockQuantity})"
}
