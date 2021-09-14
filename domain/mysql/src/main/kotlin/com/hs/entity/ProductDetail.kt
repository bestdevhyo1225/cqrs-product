package com.hs.entity

import com.hs.exception.DomainMySqlException
import com.hs.exception.DomainMysqlExceptionMessage
import java.time.LocalDateTime

class ProductDetail private constructor(
    private var name: String,
    private var price: Int,
    private var stockQuantity: Int,
    private var confirmStatus: ConfirmStatus,
) {

    enum class ConfirmStatus { WAIT, REJECT, APPROVE }

    override fun toString(): String =
        "ProductDetail(name=$name, price=$price, stockQuantity=$stockQuantity, confirmStatus=$confirmStatus)"

    companion object {
        @JvmStatic
        fun create(
            name: String,
            price: Int,
            stockQuantity: Int,
            confirmStatus: ConfirmStatus = ConfirmStatus.WAIT
        ): ProductDetail {
            return ProductDetail(
                name = name,
                price = price,
                stockQuantity = stockQuantity,
                confirmStatus = confirmStatus
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

    fun changeProductDetail(name: String, price: Int, stockQuantity: Int) {
        this.name = name
        this.price = price
        this.stockQuantity = stockQuantity
        this.confirmStatus = ConfirmStatus.WAIT
    }

    fun decreaseStockCount(stockQuantity: Int) {
        if (this.stockQuantity - stockQuantity <= 0) {
            throw DomainMySqlException(DomainMysqlExceptionMessage.HAVE_EXCEEDED_THE_QUANTITY_AVAILABLE_FOR_PURCHASE)
        }

        this.stockQuantity -= stockQuantity
    }

    fun updateConfirmStatus(confirmStatus: ConfirmStatus) {
        this.confirmStatus = confirmStatus
    }

    fun getName(): String = name
    fun getPrice(): Int = price
    fun getStockQuantity(): Int = stockQuantity
    fun getConfirmStatus(): ConfirmStatus = confirmStatus
}
