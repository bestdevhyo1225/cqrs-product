package com.hs.dto

data class FindProductDto(
    val productId: Long,
    val name: String,
    val price: Int,
    val stockQuantity: Int,
    val confirmStatus: String,
    val imageUrls: List<String>
)
