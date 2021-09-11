package com.hs.jpa.repository

import com.hs.jpa.entity.ProductAggregate

interface BatchAppProductAggregateRepository {
    fun findByProductId(productId: Long): ProductAggregate?
    fun insertAll(productAggregates: List<ProductAggregate>)
    fun saveAll(productAggregates: List<ProductAggregate>)
}
