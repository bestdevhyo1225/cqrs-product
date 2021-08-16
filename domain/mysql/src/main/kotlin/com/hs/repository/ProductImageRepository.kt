package com.hs.repository

import com.hs.entity.ProductImage

interface ProductImageRepository {
    fun saveAll(productImages: List<ProductImage>): List<ProductImage>
    fun deleteByProductId(productId: Long)
}
