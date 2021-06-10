package com.hs.dto

data class CreateProductDto(
    val name: String,
    val price: Int,
    val stockQuantity: Int,
    val imageUrls: List<String>
)
