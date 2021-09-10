package com.hs.infrastructure.mongo.persistence

import com.hs.dto.FindProductDto
import com.hs.entity.ProductAggregateType
import com.hs.util.DatetimeFormatterUtils
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Document(collection = "product_aggregates")
class ProductAggregateDocument private constructor(
    id: String? = null,
    productId: Long,
    type: ProductAggregateType,
    isDisplay: Boolean,
    data: FindProductDto,
    createdDatetime: String,
    updatedDatetime: String,
) {

    @Id
    var id: String? = id
        protected set

    var productId: Long = productId
        protected set

    var type: ProductAggregateType = type
        protected set

    var isDisplay: Boolean = isDisplay
        protected set

    var data: FindProductDto = data
        protected set

    var createdDatetime: String = createdDatetime
        protected set

    var updatedDatetime: String = updatedDatetime
        protected set

    override fun toString(): String {
        return "ProductAggregateDocument(id=$id, productId=$productId, type=$type, isDisplay=$isDisplay, data=$data, " +
                "createdDatetime=$createdDatetime, updatedDatetime=$updatedDatetime)"
    }

    companion object {
        fun toPersistenceEntity(
            id: String? = null,
            productId: Long,
            type: ProductAggregateType,
            isDisplay: Boolean,
            data: FindProductDto,
            createdDatetime: LocalDateTime,
            updatedDatetime: LocalDateTime
        ): ProductAggregateDocument {
            return ProductAggregateDocument(
                id = id,
                productId = productId,
                type = type,
                isDisplay = isDisplay,
                data = data,
                createdDatetime = createdDatetime.format(DatetimeFormatterUtils.yyyyMMdd_HHmmss),
                updatedDatetime = updatedDatetime.format(DatetimeFormatterUtils.yyyyMMdd_HHmmss)
            )
        }
    }
}
