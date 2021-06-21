package com.hs.service

import com.hs.dto.FindProductDto

interface RestGetRequestor {
    suspend fun getProduct(productId: Long): FindProductDto
}
