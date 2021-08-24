package com.hs.infrastructure.mongo.repository

import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType
import com.hs.infrastructure.mongo.entity.ProductAggregateDocument
import com.hs.infrastructure.mongo.mapper.ProductAggregateMapper
import com.hs.repository.QueryAppProductAggregateRepository
import org.springframework.data.domain.PageImpl
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

    override fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggregate? {
        val criteria = Criteria
            .where("productId").isEqualTo(productId)
            .and("type").isEqualTo(type)

        val productAggregateDocument = mongoOperations.findOne(Query(criteria), ProductAggregateDocument::class.java)

        return ProductAggregateMapper.toDomainEntity(productAggregateDocument = productAggregateDocument)
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

        val productAggregateDocument = mongoOperations.findOne(Query(criteria), ProductAggregateDocument::class.java)

        return ProductAggregateMapper.toDomainEntity(productAggregateDocument = productAggregateDocument)
    }

    override fun findAllByTypeAndIsDisplay(
        type: ProductAggregateType,
        isDisplay: Boolean,
        page: Int,
        pageSize: Int,
    ): Pair<List<ProductAggregate>, Long> {
        val criteria = Criteria
            .where("type").isEqualTo(type)
            .and("isDisplay").isEqualTo(isDisplay)

        val query = Query(criteria)
            .with(Sort.by(Sort.Direction.DESC, "productId", "createdDatetime"))
            .with(PageRequest.of(page, pageSize))

        val productAggregates: List<ProductAggregate> = mongoOperations
            .find(query, ProductAggregateDocument::class.java)
            .map { ProductAggregateMapper.toDomainEntity(productAggregateDocument = it)!! }

        val totalCount: Long = mongoOperations.count(query, ProductAggregate::class.java)

        return Pair(first = productAggregates, second = totalCount)
    }

    override fun insert(productAggregate: ProductAggregate): ProductAggregate? {
        println("------ insert -------")

        val productAggregateDocument =
            mongoOperations.insert(ProductAggregateMapper.toDocument(productAggregate = productAggregate))

        productAggregate.reflectIdAfterPersistence(id = productAggregateDocument.id)

        return productAggregate
    }

    override fun save(productAggregate: ProductAggregate): ProductAggregate? {
        println("------ save -------")

        mongoOperations.save(ProductAggregateMapper.toDocument(productAggregate = productAggregate))

        return productAggregate
    }
}
