package com.hs.dto

data class CreateProductDto(
    val name: String,
    val price: Int,
    val stockCount: Int,
    val imageUrls: List<String>
)
