package com.hs.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
data class FindPaginationDto(

    @JsonProperty("items")
    val items: List<Any>,

    @JsonProperty("page")
    val page: Int,

    @JsonProperty("pageSize")
    val pageSize: Int,

    @JsonProperty("totalCount")
    val totalCount: Long
)
