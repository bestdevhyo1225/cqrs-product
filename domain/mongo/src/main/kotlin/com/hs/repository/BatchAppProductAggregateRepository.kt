package com.hs.repository

import com.hs.entity.ProductAggregate

interface BatchAppProductAggregateRepository {
    fun findByProductId(productId: Long): ProductAggregate?
    fun insertAll(productAggregates: List<ProductAggregate>)
    fun saveAll(productAggregates: List<ProductAggregate>)
}
