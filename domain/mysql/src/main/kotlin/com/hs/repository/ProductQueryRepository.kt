package com.hs.repository

import com.hs.dto.FindProductAggregateDto

interface ProductQueryRepository {
    fun findProductAggregate(id: Long): FindProductAggregateDto?
}
