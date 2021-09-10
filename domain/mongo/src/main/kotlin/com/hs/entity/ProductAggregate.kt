package com.hs.entity

import com.hs.exception.DomainMongoException
import com.hs.message.QueryAppExceptionMessage
import com.hs.util.DatetimeFormatterUtils
import java.time.LocalDateTime

class ProductAggregate private constructor(
    id: String? = null,
    productId: Long,
    isDisplay: Boolean,
    productInfo: ProductInfo,
    createdDatetime: LocalDateTime = LocalDateTime.now(),
    updatedDatetime: LocalDateTime = LocalDateTime.now()
) {

    var id: String? = id
        private set

    var productId: Long = productId
        private set

    var isDisplay: Boolean = isDisplay
        private set

    var productInfo: ProductInfo = productInfo
        private set

    var createdDatetime: LocalDateTime = createdDatetime
        private set

    var updatedDatetime: LocalDateTime = updatedDatetime
        private set

    override fun toString(): String {
        return "ProductAggregate(id=$id, productId=$productId, isDisplay=$isDisplay, productInfo=$productInfo, " +
                "createdDatetime=$createdDatetime, updatedDatetime=$updatedDatetime)"
    }

    companion object {
        fun create(productInfo: ProductInfo, confirmStatus: String): ProductAggregate {
            return ProductAggregate(
                productId = productInfo.id,
                isDisplay = confirmStatus == "APPROVE",
                productInfo = productInfo
            )
        }

        fun mapOf(
            id: String,
            productId: Long,
            isDisplay: Boolean,
            productInfo: ProductInfo,
            createdDatetime: String,
            updatedDatetime: String
        ): ProductAggregate {
            return ProductAggregate(
                id = id,
                productId = productId,
                isDisplay = isDisplay,
                productInfo = productInfo,
                createdDatetime = LocalDateTime.parse(createdDatetime, DatetimeFormatterUtils.DATETIME_FORMATTER),
                updatedDatetime = LocalDateTime.parse(updatedDatetime, DatetimeFormatterUtils.DATETIME_FORMATTER)
            )
        }
    }

    fun changeProductAggregateData(productInfo: ProductInfo, confirmStatus: String) {
        this.productInfo = productInfo
        this.isDisplay = confirmStatus == "APPROVE"
        this.updatedDatetime = LocalDateTime.now()
    }

    fun reflectIdAfterPersistence(id: String?) {
        if (id == null || id.isBlank()) {
            throw DomainMongoException(exceptionMessage = QueryAppExceptionMessage.PRODUCT_ID_IS_NULL_OR_BLANK)
        }

        this.id = id
    }

    fun convertToStringCreatedDatetime(): String {
        return createdDatetime.format(DatetimeFormatterUtils.DATETIME_FORMATTER)
    }

    fun convertToStringUpdatedDatetime(): String {
        return updatedDatetime.format(DatetimeFormatterUtils.DATETIME_FORMATTER)
    }
}
