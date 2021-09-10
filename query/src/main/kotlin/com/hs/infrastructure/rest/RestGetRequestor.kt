package com.hs.infrastructure.rest

import com.hs.dto.FindProductDto

interface RestGetRequestor {
    suspend fun getProduct(productId: Long): FindProductDto
}
