package com.hs.repository

import com.hs.entity.Product

interface ProductQueryRepository {
    fun findProductWithFetchJoin(id: Long): Product?
}
