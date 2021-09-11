package com.hs.dto

import com.hs.jpa.entity.ProductAggregate

data class UpsertProductAggregateDto(
    val productAggregate: ProductAggregate,
    val isNew: Boolean
)
