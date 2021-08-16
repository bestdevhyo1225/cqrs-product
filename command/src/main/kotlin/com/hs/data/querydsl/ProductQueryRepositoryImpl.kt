package com.hs.data.querydsl

import com.hs.entity.Product
import com.hs.entity.QProduct.product
import com.hs.entity.QProductImage.productImage
import com.hs.repository.ProductQueryRepository
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ProductQueryRepositoryImpl(private val queryFactory: JPAQueryFactory) : ProductQueryRepository {

    override fun findProductWithFetchJoin(id: Long): Product? {
        return queryFactory
            .selectFrom(product)
            .innerJoin(product.productImages, productImage).fetchJoin()
            .where(productIdEq(id))
            .fetchOne()
    }

    private fun productIdEq(id: Long): BooleanExpression {
        return product.id.eq(id)
    }
}
