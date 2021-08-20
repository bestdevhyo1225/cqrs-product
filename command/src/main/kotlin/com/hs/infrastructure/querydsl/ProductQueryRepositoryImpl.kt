package com.hs.infrastructure.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ProductQueryRepositoryImpl(private val queryFactory: JPAQueryFactory) {
}
