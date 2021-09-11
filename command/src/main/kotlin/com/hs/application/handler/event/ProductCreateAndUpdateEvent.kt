package com.hs.application.handler.event

import com.hs.jpa.entity.Product

data class ProductCreateAndUpdateEvent(
    val productId: Long,
    val status: Product.EventStatus,
    val product: Product
) {
    override fun toString(): String =
        "Product(name=${product.name}, " +
                "price=${product.price}, " +
                "stockQuantity=${product.stockQuantity}, " +
                "imageUrls=${product.imageUrls}"
}
