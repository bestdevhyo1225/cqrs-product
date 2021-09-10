package com.hs.entity

import com.hs.exception.DomainMongoException
import com.hs.message.QueryAppExceptionMessage
import com.hs.vo.ProductDatetime

class ProductAggregate private constructor(
    id: String? = null,
    productId: Long,
    isDisplay: Boolean,
    productInfo: ProductInfo,
    productDatetime: ProductDatetime,
) {

    var id: String? = id
        private set

    var productId: Long = productId
        private set

    var isDisplay: Boolean = isDisplay
        private set

    var productInfo: ProductInfo = productInfo
        private set

    var productDatetime: ProductDatetime = productDatetime
        private set

    override fun toString(): String {
        return "ProductAggregate(id=$id, productId=$productId, isDisplay=$isDisplay, productInfo=$productInfo, " +
                "productDatetime=$productDatetime)"
    }

    companion object {
        fun create(
            confirmStatus: String,
            productInfo: ProductInfo
        ): ProductAggregate {
            return ProductAggregate(
                productId = productInfo.id,
                isDisplay = confirmStatus == "APPROVE",
                productInfo = productInfo,
                productDatetime = ProductDatetime.create()
            )
        }

        fun mapOf(
            id: String,
            productId: Long,
            isDisplay: Boolean,
            productInfo: ProductInfo,
            createdDatetime: String,
            updatedDatetime: String,
        ): ProductAggregate {
            return ProductAggregate(
                id = id,
                productId = productId,
                isDisplay = isDisplay,
                productInfo = productInfo,
                productDatetime = ProductDatetime.mapOf(
                    createdDatetime = createdDatetime,
                    updatedDatetime = updatedDatetime
                )
            )
        }
    }

    fun changeProductAggregateData(productInfo: ProductInfo, confirmStatus: String) {
        this.productInfo = productInfo
        this.isDisplay = confirmStatus == "APPROVE"
        this.productDatetime.chanageUpdatedDatetime()
    }

    fun reflectIdAfterPersistence(id: String?) {
        if (id == null || id.isBlank()) {
            throw DomainMongoException(exceptionMessage = QueryAppExceptionMessage.PRODUCT_ID_IS_NULL_OR_BLANK)
        }

        this.id = id
    }

    fun convertToStringCreatedDatetime(): String {
        return productDatetime.toCreatedStringDatetime()
    }

    fun convertToStringUpdatedDatetime(): String {
        return productDatetime.toUpdatedStringDatetime()
    }
}
