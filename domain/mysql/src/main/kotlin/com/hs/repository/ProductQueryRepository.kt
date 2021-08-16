package com.hs.repository

import com.hs.entity.Product

interface ProductQueryRepository {
    fun findProduct(id: Long): Product?
    fun findProductWithFetchJoin(id: Long): Product?
}
