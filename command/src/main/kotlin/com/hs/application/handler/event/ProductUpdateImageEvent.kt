package com.hs.application.handler.event

import com.hs.jpa.entity.Product

data class ProductUpdateImageEvent(
    val productId: Long,
    val status: Product.EventStatus,
    val imageUrls: List<String>
) {
    override fun toString(): String = "Product(imageUrls=${imageUrls})"
}
