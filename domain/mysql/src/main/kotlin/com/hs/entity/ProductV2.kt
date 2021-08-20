package com.hs.entity

import com.hs.exception.DomainMySqlException
import com.hs.message.CommandAppExceptionMessage
import java.time.LocalDateTime

class ProductV2(
    id: Long? = null,
    name: String,
    price: Int,
    stockQuantity: Int,
    imageUrls: List<String> = listOf(),
    confirmStatus: ProductConfirmStatus = ProductConfirmStatus.WAIT,
    createdDate: LocalDateTime = LocalDateTime.now(),
    updatedDate: LocalDateTime = LocalDateTime.now(),
    deletedDate: LocalDateTime? = null,
) {

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

    var confirmStatus: ProductConfirmStatus = confirmStatus
        private set

    var createdDate: LocalDateTime = createdDate
        private set

    var updatedDate: LocalDateTime = updatedDate
        private set

    var deletedDate: LocalDateTime? = deletedDate
        private set

    override fun toString(): String {
        return "ProductV2(" +
                "id=$id, name=$name, price=$price, stockQuantity=$stockQuantity, imageUrls=$imageUrls, " +
                "confirmStatus=$confirmStatus, createdDate=$createdDate, updatedDate=$updatedDate, " +
                "deletedDate=$deletedDate" +
                ")"
    }

    fun reflectIdAfterPersistence(id: Long?) {
        this.id = id
    }

    fun update(name: String, price: Int, stockQuantity: Int) {
        this.name = name
        this.price = price
        this.stockQuantity = stockQuantity
        this.confirmStatus = ProductConfirmStatus.WAIT
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

    fun updateConfirmStatus(confirmStatus: ProductConfirmStatus) {
        this.confirmStatus = confirmStatus
        this.updatedDate = LocalDateTime.now()
    }
}
