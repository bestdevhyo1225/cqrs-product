package com.hs.infrastructure.mongo.persistence

import com.hs.entity.ProductInfo
import com.hs.util.DatetimeFormatterUtils
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

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
            createdDatetime: LocalDateTime,
            updatedDatetime: LocalDateTime
        ): ProductAggregateDocument {
            return ProductAggregateDocument(
                id = id,
                productId = productId,
                isDisplay = isDisplay,
                productInfo = productInfo,
                createdDatetime = createdDatetime.format(DatetimeFormatterUtils.DATETIME_FORMATTER),
                updatedDatetime = updatedDatetime.format(DatetimeFormatterUtils.DATETIME_FORMATTER)
            )
        }
    }
}
