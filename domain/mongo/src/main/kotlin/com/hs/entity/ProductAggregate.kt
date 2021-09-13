package com.hs.entity

import com.hs.exception.DomainMongoException
import com.hs.exception.DomainMongoExceptionMessage
import com.hs.vo.ProductAggregateId
import com.hs.vo.ProductDatetime
import com.hs.vo.ProductInfo

class ProductAggregate private constructor(
    productAggregateId: ProductAggregateId,
    productInfo: ProductInfo,
    isDisplay: Boolean,
    productDatetime: ProductDatetime,
) {

    enum class ConfirmStatus { WAIT, REJECT, APPROVE }

    var productAggregateId: ProductAggregateId = productAggregateId
        private set

    var productInfo: ProductInfo = productInfo
        private set

    var isDisplay: Boolean = isDisplay
        private set

    var productDatetime: ProductDatetime = productDatetime
        private set

    override fun toString(): String {
        return "ProductAggregate(productAggregateId=$productAggregateId, productInfo=$productInfo, " +
                "isDisplay=$isDisplay, productDatetime=$productDatetime)"
    }

    companion object {
        @JvmStatic
        fun create(
            productId: Long,
            name: String,
            price: Int,
            stockQuantity: Int,
            imageUrls: List<String>,
            confirmStatus: String
        ): ProductAggregate {
            return ProductAggregate(
                productAggregateId = ProductAggregateId.create(productId = productId),
                productInfo = ProductInfo.create(
                    name = name,
                    price = price,
                    stockQuantity = stockQuantity,
                    imageUrls = imageUrls
                ),
                isDisplay = isApproveConfirmStatus(value = confirmStatus),
                productDatetime = ProductDatetime.createWithZeroNanoOfSecond()
            )
        }

        @JvmStatic
        fun mapOf(
            id: String,
            productId: Long,
            productInfo: ProductInfo,
            isDisplay: Boolean,
            createdDatetime: String,
            updatedDatetime: String,
        ): ProductAggregate {
            return ProductAggregate(
                productAggregateId = ProductAggregateId.create(id = id, productId = productId),
                productInfo = productInfo,
                isDisplay = isDisplay,
                productDatetime = ProductDatetime.createByStringParams(
                    createdDatetime = createdDatetime,
                    updatedDatetime = updatedDatetime
                )
            )
        }

        @JvmStatic
        private fun isApproveConfirmStatus(value: String): Boolean {
            try {
                return ConfirmStatus.valueOf(value = value) == ConfirmStatus.APPROVE
            } catch (exception: Exception) {
                throw DomainMongoException(DomainMongoExceptionMessage.NOT_EXIST_PRODUCT_CONFIRM_STATUS)
            }
        }
    }

    fun changeProductAggregateData(
        name: String,
        price: Int,
        stockQuantity: Int,
        imageUrls: List<String>,
        confirmStatus: String
    ) {
        productInfo =
            ProductInfo.create(name = name, price = price, stockQuantity = stockQuantity, imageUrls = imageUrls)
        isDisplay = isApproveConfirmStatus(value = confirmStatus)
        productDatetime =
            ProductDatetime.createWithZeroNanoOfSecond(createdDatetime = productDatetime.getCreatedDatetime())
    }

    fun reflectIdAfterPersistence(id: String?) {
        productAggregateId =
            ProductAggregateId.createAfterPersistence(id = id, productId = productAggregateId.getProductId())
    }

    fun getProductName(): String = productInfo.getName()
    fun getProductPrice(): Int = productInfo.getPrice()
    fun getProductStockQuantity(): Int = productInfo.getStockQuantity()
    fun getProductImageUrls(): List<String> = productInfo.getImageUrls()

    fun getStringCreatedDatetime(): String = productDatetime.getStringCreatedDatetime()
    fun getStringUpdatedDatetime(): String = productDatetime.getStringUpdatedDatetime()
}
