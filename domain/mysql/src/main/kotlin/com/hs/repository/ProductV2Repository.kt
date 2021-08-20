package com.hs.repository

import com.hs.entity.ProductConfirmStatus
import com.hs.entity.ProductV2

interface ProductV2Repository {
    fun save(product: ProductV2): ProductV2
    fun update(product: ProductV2)
    fun updateStockQuantity(id: Long?, stockQuantity: Int)
    fun updateConfirmStatus(id: Long?, confirmStatus: ProductConfirmStatus)
    fun findProduct(id: Long): ProductV2?
    fun findProductWithFetchJoin(id: Long): ProductV2?
}
