package com.hs.repository

import com.hs.entity.Product

interface ProductRepository {
    fun save(product: Product): Product
    fun saveAllImage(product: Product, imageUrls: List<String>)
    fun update(product: Product)
    fun updateStockQuantity(product: Product)
    fun updateConfirmStatus(product: Product)
    fun deleteImageByProductId(productId: Long)
    fun findProduct(id: Long): Product?
    fun findProductWithFetchJoin(id: Long): Product?
}
