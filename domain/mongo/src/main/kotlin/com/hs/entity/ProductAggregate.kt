package com.hs.entity

import com.hs.vo.ProductAggregateId
import com.hs.vo.ProductDatetime
import com.hs.vo.ProductInfo

class ProductAggregate private constructor(
    productAggregateId: ProductAggregateId,
    isDisplay: Boolean,
    productInfo: ProductInfo,
    productDatetime: ProductDatetime,
) {

    var productAggregateId: ProductAggregateId = productAggregateId
        private set

    var isDisplay: Boolean = isDisplay
        private set

    var productInfo: ProductInfo = productInfo
        private set

    var productDatetime: ProductDatetime = productDatetime
        private set

    override fun toString(): String {
        return "ProductAggregate(productAggregateId=$productAggregateId, isDisplay=$isDisplay, " +
                "productInfo=$productInfo, productDatetime=$productDatetime)"
    }

    companion object {
        @JvmStatic
        fun create(productId: Long, confirmStatus: String, productInfo: ProductInfo): ProductAggregate {
            return ProductAggregate(
                productAggregateId = ProductAggregateId.create(productId = productId),
                isDisplay = confirmStatus == "APPROVE",
                productInfo = productInfo,
                productDatetime = ProductDatetime.createWithZeroNanoOfSecond()
            )
        }

        @JvmStatic
        fun mapOf(
            id: String,
            productId: Long,
            isDisplay: Boolean,
            productInfo: ProductInfo,
            createdDatetime: String,
            updatedDatetime: String,
        ): ProductAggregate {
            return ProductAggregate(
                productAggregateId = ProductAggregateId.create(id = id, productId = productId),
                isDisplay = isDisplay,
                productInfo = productInfo,
                productDatetime = ProductDatetime.createByStringParams(
                    createdDatetime = createdDatetime,
                    updatedDatetime = updatedDatetime
                )
            )
        }
    }

    fun reflectIdAfterPersistence(id: String?) {
        productAggregateId.reflectIdAfterPersistence(id = id)
    }

    fun changeProductAggregateData(productInfo: ProductInfo, confirmStatus: String) {
        this.productInfo = productInfo
        this.isDisplay = confirmStatus == "APPROVE"
        this.productDatetime =
            ProductDatetime.createWithZeroNanoOfSecond(createdDatetime = this.productDatetime.getCreatedDatetime())
    }

    fun getProductName(): String = productInfo.getName()
    fun getProductPrice(): Int = productInfo.getPrice()
    fun getProductStockQuantity(): Int = productInfo.getStockQuantity()
    fun getProductImageUrls(): List<String> = productInfo.getImageUrls()

    fun getStringCreatedDatetime(): String = productDatetime.getStringCreatedDatetime()
    fun getStringUpdatedDatetime(): String = productDatetime.getStringUpdatedDatetime()
}
