package com.hs.infrastructure.querydsl

import com.hs.entity.Product
import com.hs.repository.ProductQueryRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class ProductQueryRepositoryImpl(private val queryFactory: JPAQueryFactory) : ProductQueryRepository {

    override fun findProductWithFetchJoin(id: Long): Product? {
        TODO("Not yet implemented")
    }
}
