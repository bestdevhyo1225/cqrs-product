package com.hs.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
data class FindProductAggregateDto(

    @JsonProperty("productId")
    val productId: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("price")
    val price: Int,

    @JsonProperty("stockQuantity")
    val stockQuantity: Int,

    @JsonProperty("imageUrls")
    val imageUrls: List<String>,

    @JsonProperty("createdDatetime")
    val createdDatetime: String,

    @JsonProperty("updatedDatetime")
    val updatedDatetime: String,
)
