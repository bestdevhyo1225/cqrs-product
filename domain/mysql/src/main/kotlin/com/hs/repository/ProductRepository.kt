package com.hs.repository

import com.hs.entity.Product

interface ProductRepository {
    fun save(product: Product): Product
}
