package com.hs.repository

import com.hs.entity.ProductEventLog

interface ProductEventLogRepository {
    fun save(productEventLog: ProductEventLog): ProductEventLog
}
