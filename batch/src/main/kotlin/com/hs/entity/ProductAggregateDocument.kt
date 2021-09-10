package com.hs.entity

import com.hs.dto.FindProductDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Document(collection = "product_aggregates")
class ProductAggregateDocument(
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
        private val DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

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
                createdDatetime = createdDatetime.format(DATETIME_FORMATTER),
                updatedDatetime = updatedDatetime.format(DATETIME_FORMATTER)
            )
        }
    }
}
