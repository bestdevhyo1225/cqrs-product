package com.hs.entity

import com.hs.dto.FindProductDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ProductAggregate(
    id: String? = null,
    productId: Long,
    type: ProductAggregateType,
    isDisplay: Boolean,
    data: FindProductDto,
    createdDatetime: LocalDateTime = LocalDateTime.now(),
    updatedDatetime: LocalDateTime = LocalDateTime.now()
) {

    var id: String? = id
        private set

    var productId: Long = productId
        private set

    var type: ProductAggregateType = type
        private set

    var isDisplay: Boolean = isDisplay
        private set

    var data: FindProductDto = data
        private set

    var createdDatetime: String = createdDatetime.format(DATETIME_FORMATTER).toString()
        private set

    var updatedDatetime: String = updatedDatetime.format(DATETIME_FORMATTER).toString()
        private set

    override fun toString(): String {
        return "ProductAggregate(id=$id, productId=$productId, type=$type, isDisplay=$isDisplay, data=$data, " +
                "createdDatetime=$createdDatetime, updatedDatetime=$updatedDatetime)"
    }

    companion object {
        val DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

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

    fun reflectIdAfterPersistence(id: String?) {
        this.id = id
    }
}
