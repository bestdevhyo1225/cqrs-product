package com.hs.event

import com.hs.entity.ProductCommandCode

data class ProductUpdateImageEvent(
    val productId: Long,
    val productCommandCode: ProductCommandCode,
    val imageUrls: List<String>
) {
    override fun toString(): String = "Product(imageUrls=${imageUrls})"
}
