package com.hs.vo

import com.hs.entity.ProductAggregate

data class UpsertProductAggregateVo(
    val productAggregate: ProductAggregate,
    val isNew: Boolean
)
