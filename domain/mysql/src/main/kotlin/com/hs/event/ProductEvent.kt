package com.hs.event

import com.hs.entity.CommandCode


data class ProductEvent(
    val productId: Long,
    val commandCode: CommandCode
) {
    override fun toString(): String = "ProductEvent(productId=${productId})"
}
