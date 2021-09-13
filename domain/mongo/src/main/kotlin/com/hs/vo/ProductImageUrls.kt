package com.hs.vo

class ProductImageUrls private constructor(
    private val productImageUrls: List<String>
) {

    override fun toString(): String {
        return "ProductImageUrls(productImageUrls=$productImageUrls)"
    }

    companion object {
        @JvmStatic
        fun create(productImageUrls: List<String>): ProductImageUrls {
            return ProductImageUrls(productImageUrls = productImageUrls)
        }
    }

    fun getProductImageUrls(): List<String> = productImageUrls
}
