package com.hs.entity

import com.hs.dto.FindProductDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Document(collection = "product_aggregates")
class ProductAggregate(productId: Long, type: ProductAggregateType, isDisplay: Boolean, data: FindProductDto) {

    @Id
    var id: String? = null
        protected set

    var productId: Long = productId
        protected set

    var type: ProductAggregateType = type
        protected set

    var isDisplay: Boolean = isDisplay
        protected set

    var data: FindProductDto = data
        protected set

    var createdDatetime: String = LocalDateTime.now().format(DATETIME_FORMATTER).toString()
        protected set

    var updatedDatetime: String = LocalDateTime.now().format(DATETIME_FORMATTER).toString()
        protected set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductAggregate

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "ProductAggregate(id=$id, productId=$productId, type=$type, isDisplay=$isDisplay, data=$data, " +
                "createdDatetime=$createdDatetime, updatedDatetime=$updatedDatetime)"
    }

    companion object {
        private val DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        fun create(productDto: FindProductDto, type: ProductAggregateType): ProductAggregate {
            return ProductAggregate(
                productId = productDto.productId,
                type = type,
                isDisplay = productDto.confirmStatus == "APPROVE",
                data = productDto
            )
        }
    }

    fun changeProductAggregateData(data: FindProductDto) {
        this.data = data
        this.isDisplay = data.confirmStatus == "APPROVE"
        this.updatedDatetime = LocalDateTime.now().format(DATETIME_FORMATTER).toString()
    }
}
