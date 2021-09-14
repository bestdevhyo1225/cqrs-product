package com.hs.entity

import com.hs.exception.DomainMySqlException
import com.hs.exception.DomainMysqlExceptionMessage
import com.hs.vo.ProductImageUrls
import java.time.LocalDateTime

class Product private constructor(
    id: Long? = null,
    detail: ProductDetail,
    imageUrls: ProductImageUrls,
    createdDate: LocalDateTime = LocalDateTime.now(),
    updatedDate: LocalDateTime = LocalDateTime.now(),
    deletedDate: LocalDateTime? = null,
) {

    enum class EventStatus { INSERT, UPDATE, UPDATE_IMAGE, CHANGE_CONFIRM_STATUS, DECREASE_STOCK_QUANTITY, DELETE }

    var id: Long? = id
        private set

    var detail: ProductDetail = detail
        private set

    var imageUrls: ProductImageUrls = imageUrls
        private set

    var createdDate: LocalDateTime = createdDate
        private set

    var updatedDate: LocalDateTime = updatedDate
        private set

    var deletedDate: LocalDateTime? = deletedDate
        private set

    override fun toString(): String =
        "Product(id=$id, detail=$detail, imageUrls=$imageUrls, " +
                "createdDate=$createdDate, updatedDate=$updatedDate, deletedDate=$deletedDate)"

    companion object {
        @JvmStatic
        fun create(id: Long? = null, name: String, price: Int, stockQuantity: Int, imageUrls: List<String>): Product {
            return Product(
                id = id,
                detail = ProductDetail.create(name = name, price = price, stockQuantity = stockQuantity),
                imageUrls = ProductImageUrls.create(productImageUrls = imageUrls)
            )
        }

        @JvmStatic
        fun mapOf(
            id: Long,
            name: String,
            price: Int,
            stockQuantity: Int,
            confirmStatus: ProductDetail.ConfirmStatus,
            imageUrls: List<String>,
            createdDate: LocalDateTime,
            updatedDate: LocalDateTime,
            deletedDate: LocalDateTime?
        ): Product {
            return Product(
                id = id,
                detail = ProductDetail.create(
                    name = name,
                    price = price,
                    stockQuantity = stockQuantity,
                    confirmStatus = confirmStatus
                ),
                imageUrls = ProductImageUrls.create(productImageUrls = imageUrls),
                createdDate = createdDate,
                updatedDate = updatedDate,
                deletedDate = deletedDate
            )
        }
    }

    fun reflectIdAfterPersistence(id: Long?) {
        if (id == null) {
            throw DomainMySqlException(DomainMysqlExceptionMessage.PRODUCT_ID_IS_NULL)
        }

        this.id = id
    }

    fun update(name: String, price: Int, stockQuantity: Int) {
        this.detail.changeProductDetail(name = name, price = price, stockQuantity = stockQuantity)
        this.updatedDate = LocalDateTime.now()
    }

    fun decreaseStockCount(stockQuantity: Int) {
        this.detail.decreaseStockCount(stockQuantity = stockQuantity)
        this.updatedDate = LocalDateTime.now()
    }

    fun updateConfirmStatus(confirmStatus: ProductDetail.ConfirmStatus) {
        this.detail.updateConfirmStatus(confirmStatus = confirmStatus)
        this.updatedDate = LocalDateTime.now()
    }
}
