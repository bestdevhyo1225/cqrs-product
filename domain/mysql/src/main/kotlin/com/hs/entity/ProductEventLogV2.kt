package com.hs.entity

import java.time.LocalDateTime

class ProductEventLogV2(
    id: Long? = null,
    productId: Long,
    productCommandCode: ProductCommandCode,
    message: String,
    createdDate: LocalDateTime = LocalDateTime.now()
) {

    var id: Long? = id
        private set

    var productId: Long = productId
        private set

    var productCommandCode: ProductCommandCode = productCommandCode
        private set

    var message: String = message
        private set

    var createdDate: LocalDateTime = createdDate
        private set

    override fun toString(): String {
        return "ProductEventLogV@(id=$id, productId=$productId, productCommandCode=$productCommandCode, " +
                "message=$message, createdDate=$createdDate)"
    }

    fun reflectIdAfterPersistence(id: Long?) {
        this.id = id
    }
}
