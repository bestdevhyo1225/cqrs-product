package com.hs.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
data class FindProductAggregatePaginationDto(

    @JsonProperty("productId")
    val productId: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("price")
    val price: Int,
)
