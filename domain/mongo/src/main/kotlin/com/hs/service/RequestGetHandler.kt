package com.hs.service

import com.hs.dto.FindProductDto

interface RequestGetHandler {
    suspend fun asyncGetProduct(productId: Long): FindProductDto
}
