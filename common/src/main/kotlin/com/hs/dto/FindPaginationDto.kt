package com.hs.dto

data class FindPaginationDto<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalCount: Long
)
