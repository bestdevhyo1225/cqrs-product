package com.hs.event

data class ChangeProductEvent(
    val name: String,
    val price: Int,
    val stockCount: Int
)
