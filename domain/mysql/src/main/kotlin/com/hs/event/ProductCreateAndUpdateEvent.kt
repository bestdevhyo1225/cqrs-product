package com.hs.event

import com.hs.entity.ProductCommandCode
import com.hs.entity.Product

data class ProductCreateAndUpdateEvent(
    val productId: Long,
    val productCommandCode: ProductCommandCode,
    val product: Product
) {
    override fun toString(): String =
        "Product(name=${product.name}, " +
                "price=${product.price}, " +
                "stockQuantity=${product.stockQuantity}, " +
                "imageUrls=${product.imageUrls}"
}
