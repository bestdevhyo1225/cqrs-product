package com.hs.infrastructure.mongo

import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType
import com.hs.repository.QueryAppProductAggregateRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class ProductAggregateRepositoryImpl(private val mongoOperations: MongoOperations) :
    QueryAppProductAggregateRepository {

    override fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggregate? {
        val criteria = Criteria
            .where("productId").isEqualTo(productId)
            .and("type").isEqualTo(type)

        return mongoOperations.findOne(Query(criteria), ProductAggregate::class.java)
    }

    override fun findByProductIdAndTypeAndIsDisplay(
        productId: Long,
        type: ProductAggregateType,
        isDisplay: Boolean
    ): ProductAggregate? {
        val criteria = Criteria
            .where("productId").isEqualTo(productId)
            .and("type").isEqualTo(type)
            .and("isDisplay").isEqualTo(isDisplay)

        return mongoOperations.findOne(Query(criteria), ProductAggregate::class.java)
    }

    override fun findAllByTypeAndIsDisplay(
        type: ProductAggregateType,
        isDisplay: Boolean,
        pageable: Pageable
    ): Page<ProductAggregate> {
        val criteria = Criteria
            .where("type").isEqualTo(type)
            .and("isDisplay").isEqualTo(isDisplay)

        val query = Query(criteria)
            .with(Sort.by(Sort.Direction.DESC, "productId", "createdDatetime"))
            .with(pageable)

        val productAggregates: List<ProductAggregate> = mongoOperations.find(query, ProductAggregate::class.java)
        val totalCount: Long = mongoOperations.count(query, ProductAggregate::class.java)

        return PageImpl(productAggregates, pageable, totalCount)
    }

    override fun insert(productAggregate: ProductAggregate): ProductAggregate? {
        return mongoOperations.insert(productAggregate)
    }

    override fun save(productAggregate: ProductAggregate): ProductAggregate? {
        return mongoOperations.save(productAggregate)
    }
}
