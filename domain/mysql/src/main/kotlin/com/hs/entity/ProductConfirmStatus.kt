package com.hs.entity

import com.hs.exception.DomainMySqlException
import com.hs.message.CommandAppExceptionMessage

enum class ProductConfirmStatus() {
    WAIT,
    REJECT,
    APPROVAL;

    companion object {
        fun convertFromStringToProductConfirmStatus(value: String): ProductConfirmStatus {
            try {
                return valueOf(value = value)
            } catch (exception: Exception) {
                throw DomainMySqlException(exceptionMessage = CommandAppExceptionMessage.NOT_EXIST_PRODUCT_CONFIRM_STATUS)
            }
        }
    }
}
