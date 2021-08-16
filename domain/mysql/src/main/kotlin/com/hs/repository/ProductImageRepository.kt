package com.hs.repository

import com.hs.entity.ProductImage

interface ProductImageRepository {
    fun save(productImage: ProductImage): ProductImage
    fun deleteByProductId(productId: Long)
}
