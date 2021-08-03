package com.hs.data.querydsl

import com.hs.dto.FindProductDto
import com.hs.entity.Product
import com.hs.entity.ProductImage
import com.hs.entity.QProduct.product
import com.hs.entity.QProductImage.productImage
import com.hs.repository.ProductQueryRepository
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ProductQueryRepositoryImpl(private val queryFactory: JPAQueryFactory) : ProductQueryRepository {

    override fun findProductAggregate(id: Long): FindProductDto? {
        val productGroup: Map<Product, List<ProductImage>> = queryFactory
            .from(product)
            .innerJoin(product.productImages, productImage)
            .where(productIdEq(id))
            .transform(groupBy(product).`as`(list(productImage)))

        return productGroup.entries
            .map { entry ->
                FindProductDto(
                    productId = entry.key.id!!,
                    name = entry.key.name,
                    price = entry.key.price,
                    stockQuantity = entry.key.stockQuantity,
                    confirmStatus = entry.key.confirmStatus.toString(),
                    imageUrls = entry.value.map { productImage -> productImage.url }
                )
            }.firstOrNull()
    }

    override fun findProduct(id: Long): Product? {
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