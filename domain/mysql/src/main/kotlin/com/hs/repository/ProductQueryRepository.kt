package com.hs.repository

import com.hs.dto.FindProductDto
import com.hs.entity.Product

interface ProductQueryRepository {
    fun findProductAggregate(id: Long): FindProductDto?
    fun findProduct(id: Long): Product?
}
