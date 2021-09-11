package com.hs.application.handler.event

import com.hs.entity.Product

data class ProductUpdateImageEvent(
    val productId: Long,
    val status: Product.EventStatus,
    val imageUrls: List<String>
) {
    override fun toString(): String = "Product(imageUrls=${imageUrls})"
}
