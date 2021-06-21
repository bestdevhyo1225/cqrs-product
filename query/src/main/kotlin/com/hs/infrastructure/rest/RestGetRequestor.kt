package com.hs.infrastructure.rest

import com.hs.dto.FindProductDto
import com.hs.response.SuccessResponse
import org.springframework.http.ResponseEntity

interface RestGetRequestor {
    suspend fun getProductAggregate(productId: Long): ResponseEntity<SuccessResponse<FindProductDto>>
}
