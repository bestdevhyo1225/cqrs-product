package com.hs.dto

data class UpdateProductDto(
    val id: Long,
    val name: String,
    val price: Int,
    val stockCount: Int,
)
