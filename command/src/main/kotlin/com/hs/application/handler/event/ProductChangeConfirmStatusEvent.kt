package com.hs.application.handler.event

import com.hs.entity.Product
import com.hs.entity.ProductDetail

data class ProductChangeConfirmStatusEvent(
    val productId: Long,
    val status: Product.EventStatus,
    val confirmStatus: ProductDetail.ConfirmStatus
) {
    override fun toString(): String = "Product(confirmStatus=${confirmStatus})"
}
