package com.hs.entity

import com.hs.exception.DomainMySqlException
import com.hs.message.CommandAppExceptionMessage
import java.time.LocalDateTime

class ProductV2(
    private var id: Long? = null,
    private var name: String,
    private var price: Int,
    private var stockQuantity: Int,
    private val imageUrls: List<String> = listOf(),
    private var confirmStatus: ProductConfirmStatus = ProductConfirmStatus.WAIT,
    private var createdDate: LocalDateTime = LocalDateTime.now(),
    private var updatedDate: LocalDateTime = LocalDateTime.now(),
    private var deletedDate: LocalDateTime? = null,
) {

    override fun toString(): String {
        return "ProductV2(" +
                "id=$id, name=$name, price=$price, stockQuantity=$stockQuantity, imageUrls=$imageUrls, " +
                "confirmStatus=$confirmStatus, createdDate=$createdDate, updatedDate=$updatedDate, " +
                "deletedDate=$deletedDate" +
                ")"
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

    fun update(name: String, price: Int, stockQuantity: Int) {
        this.name = name
        this.price = price
        this.stockQuantity = stockQuantity
        this.confirmStatus = ProductConfirmStatus.WAIT
        this.updatedDate = LocalDateTime.now()
    }

    fun updateConfirmStatus(confirmStatus: ProductConfirmStatus) {
        this.confirmStatus = confirmStatus
        this.updatedDate = LocalDateTime.now()
    }

    fun changeId(id: Long?) {
        this.id = id
    }

    fun getId(): Long? {
        return id
    }

    fun getName(): String {
        return name
    }

    fun getPrice(): Int {
        return price
    }

    fun getStockQuantity(): Int {
        return stockQuantity
    }

    fun getConfirmStatus(): ProductConfirmStatus {
        return confirmStatus
    }

    fun getImageUrls(): List<String> {
        return imageUrls
    }
}
