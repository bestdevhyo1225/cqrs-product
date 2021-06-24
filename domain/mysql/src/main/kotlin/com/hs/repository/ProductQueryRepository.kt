package com.hs.repository

import com.hs.dto.FindProductDto

interface ProductQueryRepository {
    fun findProductAggregate(id: Long): FindProductDto?
}
