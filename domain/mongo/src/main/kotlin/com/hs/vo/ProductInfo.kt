package com.hs.vo

class ProductInfo private constructor(
    private val name: String,
    private val price: Int,
    private val stockQuantity: Int,
    private val productImageUrls: ProductImageUrls,
) {

    override fun toString(): String {
        return "ProductInfo(name=$name, price=$price, stockQuantity=$stockQuantity, productImageUrls=$productImageUrls)"
    }

    companion object {
        @JvmStatic
        fun create(name: String, price: Int, stockQuantity: Int, productImageUrls: ProductImageUrls): ProductInfo {
            return ProductInfo(
                name = name,
                price = price,
                stockQuantity = stockQuantity,
                productImageUrls = productImageUrls
            )
        }
    }

    fun getName(): String = name
    fun getPrice(): Int = price
    fun getStockQuantity(): Int = stockQuantity
    fun getProductImageUrls(): List<String> = productImageUrls.getProductImageUrls()
}
