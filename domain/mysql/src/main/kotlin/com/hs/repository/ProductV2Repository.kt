package com.hs.repository

import com.hs.entity.ProductV2

interface ProductV2Repository {
    fun save(product: ProductV2): ProductV2
    fun update(product: ProductV2)
    fun findProduct(id: Long): ProductV2?
    fun findProductWithFetchJoin(id: Long): ProductV2?
}
