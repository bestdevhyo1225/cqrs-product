package com.hs.vo

import com.hs.exception.DomainMongoException
import com.hs.exception.DomainMongoExceptionMessage

class ProductAggregateId private constructor(
    private var id: String?,
    private val productId: Long
) {

    override fun toString(): String {
        return "ProductAggregateId(id=$id, productId=$productId)"
    }

    companion object {
        @JvmStatic
        fun create(id: String? = null, productId: Long): ProductAggregateId {
            return ProductAggregateId(id = id, productId = productId)
        }
    }

    fun reflectIdAfterPersistence(id: String?) {
        if (id == null || id.isBlank()) {
            throw DomainMongoException(DomainMongoExceptionMessage.PRODUCT_ID_IS_NULL_OR_BLANK)
        }

        this.id = id
    }

    fun getId(): String? = id
    fun getProductId(): Long = productId
}
