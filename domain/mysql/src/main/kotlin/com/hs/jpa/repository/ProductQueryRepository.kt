package com.hs.jpa.repository

import com.hs.jpa.entity.Product

interface ProductQueryRepository {
    fun findProductWithFetchJoin(id: Long): Product?
}
