package com.hs.application.handler.event

import com.hs.entity.Product

data class ProductCreateAndUpdateEvent(
    val productId: Long,
    val status: Product.EventStatus,
    val product: Product
) {
    override fun toString(): String =
        "Product(name=${product.detail.getName()}, " +
                "price=${product.detail.getPrice()}, " +
                "stockQuantity=${product.detail.getStockQuantity()}, " +
                "imageUrls=${product.imageUrls}"
}
