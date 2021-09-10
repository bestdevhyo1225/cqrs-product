package com.hs.vo

import com.hs.util.DatetimeFormatterUtils
import java.time.LocalDateTime

class ProductDatetime private constructor(
    private val createdDatetime: LocalDateTime = LocalDateTime.now(),
    private var updatedDatetime: LocalDateTime = LocalDateTime.now(),
) {

    override fun toString(): String {
        return "ProductDatetime(createdDatetime=$createdDatetime, updatedDatetime=$updatedDatetime)"
    }

    companion object {
        fun create(
            createdDatetime: LocalDateTime = LocalDateTime.now(),
            updatedDatetime: LocalDateTime = LocalDateTime.now()
        ): ProductDatetime {
            return ProductDatetime(createdDatetime = createdDatetime, updatedDatetime = updatedDatetime)
        }

        fun mapOf(createdDatetime: String, updatedDatetime: String): ProductDatetime {
            return ProductDatetime(
                createdDatetime = LocalDateTime.parse(createdDatetime, DatetimeFormatterUtils.DATETIME_FORMATTER),
                updatedDatetime = LocalDateTime.parse(updatedDatetime, DatetimeFormatterUtils.DATETIME_FORMATTER)
            )
        }
    }

    fun chanageUpdatedDatetime() {
        this.updatedDatetime = LocalDateTime.now()
    }

    fun toCreatedStringDatetime(): String = createdDatetime.format(DatetimeFormatterUtils.DATETIME_FORMATTER)
    fun toUpdatedStringDatetime(): String = updatedDatetime.format(DatetimeFormatterUtils.DATETIME_FORMATTER)
}
