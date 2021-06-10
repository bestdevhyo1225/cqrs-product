package com.hs.dto

data class FindProductAggregateDto(
    val productId: Long,
    val name: String,
    val price: Int,
    val stockQuantity: Int,
    val imageUrls: List<String>
)
