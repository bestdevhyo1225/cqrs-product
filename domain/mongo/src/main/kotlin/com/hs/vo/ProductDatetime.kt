package com.hs.vo

import com.hs.util.DatetimeFormatterUtils
import java.time.LocalDateTime

class ProductDatetime private constructor(
    private var createdDatetime: LocalDateTime,
    private var updatedDatetime: LocalDateTime,
) {

    override fun toString(): String {
        return "ProductDatetime(createdDatetime=$createdDatetime, updatedDatetime=$updatedDatetime)"
    }

    companion object {
        @JvmStatic
        fun createWithZeroNanoOfSecond(
            createdDatetime: LocalDateTime = LocalDateTime.now(),
            updatedDatetime: LocalDateTime = LocalDateTime.now()
        ): ProductDatetime {
            return ProductDatetime(
                createdDatetime = createdDatetime.withNano(0),
                updatedDatetime = updatedDatetime.withNano(0)
            )
        }

        @JvmStatic
        fun createByStringParams(
            createdDatetime: String,
            updatedDatetime: String
        ): ProductDatetime {
            return ProductDatetime(
                createdDatetime = toLocalDatetime(datetime = createdDatetime),
                updatedDatetime = toLocalDatetime(datetime = updatedDatetime)
            )
        }

        @JvmStatic
        private fun toLocalDatetime(datetime: String) =
            LocalDateTime.parse(datetime, DatetimeFormatterUtils.DATETIME_FORMATTER)
    }

    fun getStringCreatedDatetime(): String = createdDatetime.format(DatetimeFormatterUtils.DATETIME_FORMATTER)
    fun getStringUpdatedDatetime(): String = updatedDatetime.format(DatetimeFormatterUtils.DATETIME_FORMATTER)

    fun getCreatedDatetime(): LocalDateTime = createdDatetime
    fun getUpdatedDatetime(): LocalDateTime = updatedDatetime
}
