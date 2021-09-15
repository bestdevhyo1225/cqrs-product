package com.hs.mongo.vo

class ProductData private constructor(
    private val name: String,
    private val price: Int,
    private val stockQuantity: Int,
    private val imageUrls: List<String>,
) {

    override fun toString(): String = "Product(name=$name, price=$price, " +
            "stockQuantity=$stockQuantity, imageUrls=$imageUrls)"

    companion object {
        @JvmStatic
        fun create(name: String, price: Int, stockQuantity: Int, imageUrls: List<String>): ProductData {
            return ProductData(name = name, price = price, stockQuantity = stockQuantity, imageUrls = imageUrls)
        }
    }

    fun getName(): String = name
    fun getPrice(): Int = price
    fun getStockQuantity(): Int = stockQuantity
    fun getImageUrls(): List<String> = imageUrls
}
