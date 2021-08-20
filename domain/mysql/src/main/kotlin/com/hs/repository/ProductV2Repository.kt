package com.hs.repository

import com.hs.entity.ProductV2

interface ProductV2Repository {
    fun save(product: ProductV2): ProductV2
    fun saveAllImage(product: ProductV2, imageUrls: List<String>)
    fun update(product: ProductV2)
    fun updateStockQuantity(product: ProductV2)
    fun updateConfirmStatus(product: ProductV2)
    fun deleteImageByProductId(productId: Long)
    fun findProduct(id: Long): ProductV2?
    fun findProductWithFetchJoin(id: Long): ProductV2?
}
