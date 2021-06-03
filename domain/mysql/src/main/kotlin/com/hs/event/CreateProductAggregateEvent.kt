package com.hs.event

data class CreateProductAggregateEvent(
    val name: String,
    val price: Int,
    val stockCount: Int,
    val productImageUrls: List<String>
)
