package com.hs.data

import com.hs.dto.FindProductAggregateDto
import com.hs.entity.Product
import com.hs.entity.ProductImage
import com.hs.entity.QProduct.product
import com.hs.entity.QProductImage.productImage
import com.hs.repository.ProductQueryRepository
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ProductQueryRepositoryImpl(private val queryFactory: JPAQueryFactory) : ProductQueryRepository {

    override fun findProductAggregate(id: Long): FindProductAggregateDto? {
        val productGroup: Map<Product, List<ProductImage>> = queryFactory
            .from(product)
            .innerJoin(product.productImages, productImage)
            .where(product.id.eq(id))
            .transform(groupBy(product).`as`(list(productImage)))

        return productGroup.entries
            .map { entry ->
                FindProductAggregateDto(
                    productId = entry.key.id!!,
                    name = entry.key.name,
                    price = entry.key.price,
                    stockCount = entry.key.stockCount,
                    imageUrls = entry.value.map { productImage -> productImage.url }
                )
            }.firstOrNull()
    }
}
