package com.hs.entity

class ProductInfo private constructor(
    val id: Long,
    val name: String,
    val price: Int,
    val stockQuantity: Int,
    val imageUrls: List<String> = listOf(),
) {

    override fun toString(): String {
        return "ProductInfo(id=$id, name=$name, price=$price, stockQuantity=$stockQuantity, imageUrls=$imageUrls)"
    }

    companion object {
        fun create(id: Long, name: String, price: Int, stockQuantity: Int, imageUrls: List<String>): ProductInfo {
            return ProductInfo(
                id = id,
                name = name,
                price = price,
                stockQuantity = stockQuantity,
                imageUrls = imageUrls
            )
        }
    }
}
