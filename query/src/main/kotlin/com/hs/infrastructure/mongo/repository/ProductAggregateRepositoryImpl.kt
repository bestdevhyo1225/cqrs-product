package com.hs.infrastructure.mongo.repository

import com.hs.entity.ProductAggregate
import com.hs.infrastructure.mongo.persistence.ProductAggregateDocument
import com.hs.repository.QueryAppProductAggregateRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class ProductAggregateRepositoryImpl(private val mongoOperations: MongoOperations) :
    QueryAppProductAggregateRepository {

    override fun findByProductId(productId: Long): ProductAggregate? {
        val criteria = Criteria.where("productId").isEqualTo(productId)

        val productAggregateDocument =
            mongoOperations.findOne(Query(criteria), ProductAggregateDocument::class.java) ?: return null

        return ProductAggregate.mapOf(
            id = productAggregateDocument.id!!,
            productId = productAggregateDocument.productId,
            isDisplay = productAggregateDocument.isDisplay,
            data = productAggregateDocument.data,
            createdDatetime = productAggregateDocument.createdDatetime,
            updatedDatetime = productAggregateDocument.updatedDatetime
        )
    }

    override fun findByProductIdAndIsDisplay(productId: Long, isDisplay: Boolean): ProductAggregate? {
        val criteria = Criteria
            .where("productId").isEqualTo(productId)
            .and("isDisplay").isEqualTo(isDisplay)

        val productAggregateDocument =
            mongoOperations.findOne(Query(criteria), ProductAggregateDocument::class.java) ?: return null

        return ProductAggregate.mapOf(
            id = productAggregateDocument.id!!,
            productId = productAggregateDocument.productId,
            isDisplay = productAggregateDocument.isDisplay,
            data = productAggregateDocument.data,
            createdDatetime = productAggregateDocument.createdDatetime,
            updatedDatetime = productAggregateDocument.updatedDatetime
        )
    }

    override fun findAllByIsDisplay(
        isDisplay: Boolean,
        page: Int,
        pageSize: Int
    ): Pair<List<ProductAggregate>, Long> {
        val criteria = Criteria.where("isDisplay").isEqualTo(isDisplay)

        val query = Query(criteria)
            .with(Sort.by(Sort.Direction.DESC, "productId", "createdDatetime"))
            .with(PageRequest.of(page, pageSize))

        val productAggregates: List<ProductAggregate> = mongoOperations
            .find(query, ProductAggregateDocument::class.java)
            .map {
                ProductAggregate.mapOf(
                    id = it.id!!,
                    productId = it.productId,
                    isDisplay = it.isDisplay,
                    data = it.data,
                    createdDatetime = it.createdDatetime,
                    updatedDatetime = it.updatedDatetime
                )
            }

        val totalCount: Long = mongoOperations.count(Query(criteria), ProductAggregateDocument::class.java)

        return Pair(first = productAggregates, second = totalCount)
    }

    override fun insert(productAggregate: ProductAggregate): ProductAggregate? {
        val productAggregateDocument = mongoOperations.insert(
            ProductAggregateDocument.create(
                id = productAggregate.id,
                productId = productAggregate.productId,
                isDisplay = productAggregate.isDisplay,
                data = productAggregate.data,
                createdDatetime = productAggregate.createdDatetime,
                updatedDatetime = productAggregate.updatedDatetime
            )
        )

        productAggregate.reflectIdAfterPersistence(id = productAggregateDocument.id)

        return productAggregate
    }

    override fun save(productAggregate: ProductAggregate): ProductAggregate? {
        mongoOperations.save(
            ProductAggregateDocument.create(
                id = productAggregate.id,
                productId = productAggregate.productId,
                isDisplay = productAggregate.isDisplay,
                data = productAggregate.data,
                createdDatetime = productAggregate.createdDatetime,
                updatedDatetime = productAggregate.updatedDatetime
            )
        )

        return productAggregate
    }
}
