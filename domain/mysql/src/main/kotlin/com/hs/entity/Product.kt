package com.hs.entity

import com.hs.exception.DomainMySqlException
import com.hs.message.CommandAppExceptionMessage
import java.time.LocalDateTime

class Product private constructor(
    id: Long? = null,
    name: String,
    price: Int,
    stockQuantity: Int,
    imageUrls: List<String> = listOf(),
    confirmStatus: ConfirmStatus = ConfirmStatus.WAIT,
    createdDate: LocalDateTime = LocalDateTime.now(),
    updatedDate: LocalDateTime = LocalDateTime.now(),
    deletedDate: LocalDateTime? = null,
) {

    enum class ConfirmStatus { WAIT, REJECT, APPROVE }

    var id: Long? = id
        private set

    var name: String = name
        private set

    var price: Int = price
        private set

    var stockQuantity: Int = stockQuantity
        private set

    var imageUrls: List<String> = imageUrls
        private set

    var confirmStatus: ConfirmStatus = confirmStatus
        private set

    var createdDate: LocalDateTime = createdDate
        private set

    var updatedDate: LocalDateTime = updatedDate
        private set

    var deletedDate: LocalDateTime? = deletedDate
        private set

    override fun toString(): String {
        return "Product(" +
                "id=$id, name=$name, price=$price, stockQuantity=$stockQuantity, imageUrls=$imageUrls, " +
                "confirmStatus=$confirmStatus, createdDate=$createdDate, updatedDate=$updatedDate, " +
                "deletedDate=$deletedDate" +
                ")"
    }

    companion object {
        fun create(id: Long? = null, name: String, price: Int, stockQuantity: Int, imageUrls: List<String>): Product {
            return Product(id = id, name = name, price = price, stockQuantity = stockQuantity, imageUrls = imageUrls)
        }

        fun convertFromPersistenceEntity(
            id: Long? = null,
            name: String,
            price: Int,
            stockQuantity: Int,
            imageUrls: List<String> = listOf(),
            confirmStatus: ConfirmStatus,
            createdDate: LocalDateTime,
            updatedDate: LocalDateTime,
            deletedDate: LocalDateTime?
        ): Product {
            return Product(
                id = id,
                name = name,
                price = price,
                stockQuantity = stockQuantity,
                imageUrls = imageUrls,
                confirmStatus = confirmStatus,
                createdDate = createdDate,
                updatedDate = updatedDate,
                deletedDate = deletedDate
            )
        }

        fun convertFromStringTypeToEnumType(value: String): ConfirmStatus {
            try {
                return ConfirmStatus.valueOf(value = value)
            } catch (exception: Exception) {
                throw DomainMySqlException(exceptionMessage = CommandAppExceptionMessage.NOT_EXIST_PRODUCT_CONFIRM_STATUS)
            }
        }
    }

    fun reflectIdAfterPersistence(id: Long?) {
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
            throw DomainMySqlException(
                exceptionMessage = CommandAppExceptionMessage.HAVE_EXCEEDED_THE_QUANTITY_AVAILABLE_FOR_PURCHASE
            )
        }

        this.stockQuantity -= stockQuantity
        this.updatedDate = LocalDateTime.now()
    }

    fun updateConfirmStatus(confirmStatus: ConfirmStatus) {
        this.confirmStatus = confirmStatus
        this.updatedDate = LocalDateTime.now()
    }
}
