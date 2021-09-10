package com.hs.util

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern

class DatetimeFormatterUtils {
    companion object {
        val yyyyMMdd_HHmmss: DateTimeFormatter = ofPattern("yyyy-MM-dd HH:mm:ss")
    }
}
