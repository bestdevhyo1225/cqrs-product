package com.hs.repository

import com.hs.entity.QProductPersistence
import com.hs.entity.QProductPersistence.*
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ProductQueryRepositoryImpl(private val queryFactory: JPAQueryFactory) : BatchAppProductQueryRepository {

    override fun findMinId(startDatetime: LocalDateTime, endDatetime: LocalDateTime): Long? {
        return queryFactory
            .select(productPersistence.id.min())
            .from(productPersistence)
            .where(productCreatedDateBetween(startDatetime, endDatetime))
            .fetchOne()
    }

    override fun findMaxId(startDatetime: LocalDateTime, endDatetime: LocalDateTime): Long? {
        return queryFactory
            .select(productPersistence.id.max())
            .from(productPersistence)
            .where(productCreatedDateBetween(startDatetime, endDatetime))
            .fetchOne()
    }

    private fun productCreatedDateBetween(startDatetime: LocalDateTime, endDatetime: LocalDateTime): BooleanExpression {
        return productPersistence.createdDate.between(startDatetime, endDatetime)
    }
}
