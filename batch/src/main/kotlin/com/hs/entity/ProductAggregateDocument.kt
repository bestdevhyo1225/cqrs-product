package com.hs.entity

import com.hs.vo.ProductInfo
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "product_aggregates")
class ProductAggregateDocument private constructor(
    id: String? = null,
    productId: Long,
    isDisplay: Boolean,
    productInfo: ProductInfo,
    createdDatetime: String,
    updatedDatetime: String,
) {

    @Id
    var id: String? = id
        protected set

    var productId: Long = productId
        protected set

    var isDisplay: Boolean = isDisplay
        protected set

    var productInfo: ProductInfo = productInfo
        protected set

    var createdDatetime: String = createdDatetime
        protected set

    var updatedDatetime: String = updatedDatetime
        protected set

    override fun toString(): String {
        return "ProductAggregateDocument(id=$id, productId=$productId, isDisplay=$isDisplay, " +
                "productInfo=$productInfo, createdDatetime=$createdDatetime, updatedDatetime=$updatedDatetime)"
    }

    companion object {
        fun create(
            id: String? = null,
            productId: Long,
            isDisplay: Boolean,
            productInfo: ProductInfo,
            createdDatetime: String,
            updatedDatetime: String
        ): ProductAggregateDocument {
            return ProductAggregateDocument(
                id = id,
                productId = productId,
                isDisplay = isDisplay,
                productInfo = productInfo,
                createdDatetime = createdDatetime,
                updatedDatetime = updatedDatetime
            )
        }
    }
}
