package com.hs.entity

import com.hs.exception.DomainMySqlException
import com.hs.exception.DomainMysqlExceptionMessage
import com.hs.vo.ProductImageUrls
import java.time.LocalDateTime

class Product private constructor(
    id: Long? = null,
    name: String,
    price: Int,
    stockQuantity: Int,
    confirmStatus: ConfirmStatus,
    imageUrls: ProductImageUrls,
    createdDate: LocalDateTime = LocalDateTime.now(),
    updatedDate: LocalDateTime = LocalDateTime.now(),
    deletedDate: LocalDateTime? = null,
) {

    enum class ConfirmStatus { WAIT, REJECT, APPROVE }
    enum class EventStatus { INSERT, UPDATE, UPDATE_IMAGE, CHANGE_CONFIRM_STATUS, DECREASE_STOCK_QUANTITY, DELETE }

    var id: Long? = id
        private set

    var name: String = name
        private set

    var price: Int = price
        private set

    var stockQuantity: Int = stockQuantity
        private set

    var confirmStatus: ConfirmStatus = confirmStatus
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
        "Product(id=$id, name=$name, price=$price, stockQuantity=$stockQuantity, confirmStatus=$confirmStatus," +
                "imageUrls=$imageUrls, createdDate=$createdDate, updatedDate=$updatedDate, deletedDate=$deletedDate)"

    companion object {
        @JvmStatic
        fun create(id: Long? = null, name: String, price: Int, stockQuantity: Int, imageUrls: List<String>): Product {
            return Product(
                id = id,
                name = name,
                price = price,
                stockQuantity = stockQuantity,
                confirmStatus = ConfirmStatus.WAIT,
                imageUrls = ProductImageUrls.create(productImageUrls = imageUrls)
            )
        }

        @JvmStatic
        fun mapOf(
            id: Long,
            name: String,
            price: Int,
            stockQuantity: Int,
            confirmStatus: ConfirmStatus,
            imageUrls: List<String>,
            createdDate: LocalDateTime,
            updatedDate: LocalDateTime,
            deletedDate: LocalDateTime?
        ): Product {
            return Product(
                id = id,
                name = name,
                price = price,
                stockQuantity = stockQuantity,
                confirmStatus = confirmStatus,
                imageUrls = ProductImageUrls.create(productImageUrls = imageUrls),
                createdDate = createdDate,
                updatedDate = updatedDate,
                deletedDate = deletedDate
            )
        }

        @JvmStatic
        fun convertFromStringToEnumValue(value: String): ConfirmStatus {
            try {
                return ConfirmStatus.valueOf(value = value)
            } catch (exception: Exception) {
                throw DomainMySqlException(DomainMysqlExceptionMessage.NOT_EXIST_PRODUCT_CONFIRM_STATUS)
            }
        }
    }

    fun reflectIdAfterPersistence(id: Long?) {
        if (id == null) {
            throw DomainMySqlException(DomainMysqlExceptionMessage.PRODUCT_ID_IS_NULL)
        }

        this.id = id
    }

    fun update(name: String, price: Int, stockQuantity: Int) {
        this.name = name
        this.price = price
        this.stockQuantity = stockQuantity
        this.confirmStatus = ConfirmStatus.WAIT
        this.updatedDate = LocalDateTime.now()
    }

    fun decreaseStockCount(stockQuantity: Int) {
        if (this.stockQuantity - stockQuantity <= 0) {
            throw DomainMySqlException(DomainMysqlExceptionMessage.HAVE_EXCEEDED_THE_QUANTITY_AVAILABLE_FOR_PURCHASE)
        }

        this.stockQuantity -= stockQuantity
        this.updatedDate = LocalDateTime.now()
    }

    fun updateConfirmStatus(confirmStatus: ConfirmStatus) {
        this.confirmStatus = confirmStatus
        this.updatedDate = LocalDateTime.now()
    }
}
