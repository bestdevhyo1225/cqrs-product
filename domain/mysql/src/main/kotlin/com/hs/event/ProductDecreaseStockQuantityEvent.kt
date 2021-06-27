package com.hs.event

import com.hs.entity.ProductCommandCode

data class ProductDecreaseStockQuantityEvent(
    val productId: Long,
    val productCommandCode: ProductCommandCode,
    val currentStockQuantity: Int,
) {
    override fun toString(): String = "Product(stockQuantity=${currentStockQuantity})"
}
