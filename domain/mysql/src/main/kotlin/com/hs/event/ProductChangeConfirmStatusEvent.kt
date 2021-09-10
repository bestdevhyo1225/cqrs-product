package com.hs.event

import com.hs.entity.Product
import com.hs.entity.ProductCommandCode

data class ProductChangeConfirmStatusEvent(
    val productId: Long,
    val productCommandCode: ProductCommandCode,
    val confirmStatus: Product.ConfirmStatus
) {
    override fun toString(): String = "Product(confirmStatus=${confirmStatus})"
}
