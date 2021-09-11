package com.hs.jpa.repository

import com.hs.jpa.entity.Product

interface ProductEventLogRepository {
    fun save(productId: Long, eventStatus: Product.EventStatus, message: String)
}
