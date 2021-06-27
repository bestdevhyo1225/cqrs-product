package com.hs.event

import com.hs.entity.ProductCommandCode


data class ProductEvent(
    val productId: Long,
    val productCommandCode: ProductCommandCode
) {
    override fun toString(): String = "ProductEvent(productId=${productId})"
}
