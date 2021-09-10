package com.hs.event

import com.hs.entity.Product

data class ProductChangeConfirmStatusEvent(
    val productId: Long,
    val status: Product.EventStatus,
    val confirmStatus: Product.ConfirmStatus
) {
    override fun toString(): String = "Product(confirmStatus=${confirmStatus})"
}
