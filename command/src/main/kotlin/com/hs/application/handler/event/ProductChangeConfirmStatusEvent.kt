package com.hs.application.handler.event

import com.hs.jpa.entity.Product

data class ProductChangeConfirmStatusEvent(
    val productId: Long,
    val status: Product.EventStatus,
    val confirmStatus: Product.ConfirmStatus
) {
    override fun toString(): String = "Product(confirmStatus=${confirmStatus})"
}
