package com.hs.entity

import com.hs.dto.FindProductDto
import com.hs.util.DatetimeFormatterUtils
import java.time.LocalDateTime

class ProductAggregate private constructor(
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

    var createdDatetime: LocalDateTime = createdDatetime
        private set

    var updatedDatetime: LocalDateTime = updatedDatetime
        private set

    override fun toString(): String {
        return "ProductAggregate(id=$id, productId=$productId, type=$type, isDisplay=$isDisplay, data=$data, " +
                "createdDatetime=$createdDatetime, updatedDatetime=$updatedDatetime)"
    }

    companion object {
        fun create(productDto: FindProductDto, type: ProductAggregateType): ProductAggregate {
            return ProductAggregate(
                productId = productDto.productId,
                type = type,
                isDisplay = productDto.confirmStatus == "APPROVE",
                data = productDto
            )
        }

        fun toDomainEntity(
            id: String? = null,
            productId: Long,
            type: ProductAggregateType,
            isDisplay: Boolean,
            data: FindProductDto,
            createdDatetime: String,
            updatedDatetime: String
        ): ProductAggregate {
            return ProductAggregate(
                id = id,
                productId = productId,
                type = type,
                isDisplay = isDisplay,
                data = data,
                createdDatetime = LocalDateTime.parse(createdDatetime, DatetimeFormatterUtils.yyyyMMdd_HHmmss),
                updatedDatetime = LocalDateTime.parse(updatedDatetime, DatetimeFormatterUtils.yyyyMMdd_HHmmss)
            )
        }
    }

    fun changeProductAggregateData(data: FindProductDto) {
        this.data = data
        this.isDisplay = data.confirmStatus == "APPROVE"
        this.updatedDatetime = LocalDateTime.now()
    }

    fun reflectIdAfterPersistence(id: String?) {
        this.id = id
    }

    fun convertToStringCreatedDatetime(): String {
        return createdDatetime.format(DatetimeFormatterUtils.yyyyMMdd_HHmmss)
    }

    fun convertToStringUpdatedDatetime(): String {
        return updatedDatetime.format(DatetimeFormatterUtils.yyyyMMdd_HHmmss)
    }
}
