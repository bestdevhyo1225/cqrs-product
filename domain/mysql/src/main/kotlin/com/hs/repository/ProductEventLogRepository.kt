package com.hs.repository

import com.hs.entity.Product

interface ProductEventLogRepository {
    fun save(productId: Long, eventStatus: Product.EventStatus, message: String)
}
