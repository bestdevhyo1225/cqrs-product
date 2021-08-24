package com.hs.infrastructure.querydsl

import com.hs.entity.Product
import com.hs.infrastructure.jpa.persistence.ProductPersistence
import com.hs.infrastructure.jpa.persistence.QProductImagePersistence.productImagePersistence
import com.hs.infrastructure.jpa.persistence.QProductPersistence.productPersistence
import com.hs.repository.ProductQueryRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ProductQueryRepositoryImpl(private val queryFactory: JPAQueryFactory) : ProductQueryRepository {

    override fun findProductWithFetchJoin(id: Long): Product? {
        val productPersistence: ProductPersistence? = queryFactory
            .selectFrom(productPersistence)
            .join(productPersistence.productImages, productImagePersistence).fetchJoin()
            .where(productPersistence.id.eq(id))
            .fetchOne()

        productPersistence ?: return null

        return Product(
            id = productPersistence.id,
            name = productPersistence.name,
            price = productPersistence.price,
            stockQuantity = productPersistence.stockQuantity,
            imageUrls = productPersistence.productImages.map { it.url },
            confirmStatus = productPersistence.confirmStatus,
            createdDate = productPersistence.createdDate,
            updatedDate = productPersistence.updatedDate,
            deletedDate = productPersistence.deletedDate
        )
    }
}
