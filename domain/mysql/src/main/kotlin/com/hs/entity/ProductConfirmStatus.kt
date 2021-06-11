package com.hs.entity

import com.hs.exception.DomainModuleException
import com.hs.exception.ExceptionMessage

enum class ProductConfirmStatus() {
    WAIT,
    REJECT,
    APPROVAL;

    companion object {
        fun convertFromStringToProductConfirmStatus(value: String): ProductConfirmStatus {
            try {
                return valueOf(value = value)
            } catch (exception: Exception) {
                throw DomainModuleException(exceptionMessage = ExceptionMessage.NOT_EXIST_PRODUCT_CONFIRM_STATUS)
            }
        }
    }
}
