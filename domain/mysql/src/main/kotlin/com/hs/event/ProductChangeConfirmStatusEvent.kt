package com.hs.event

import com.hs.entity.ProductCommandCode
import com.hs.entity.ProductConfirmStatus

data class ProductChangeConfirmStatusEvent(
    val productId: Long,
    val productCommandCode: ProductCommandCode,
    val confirmStatus: ProductConfirmStatus
) {
    override fun toString(): String = "Product(confirmStatus=${confirmStatus})"
}
