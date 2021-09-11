package com.hs.repository

import com.hs.entity.Product
import com.hs.entity.QProductImagePersistence.productImagePersistence
import com.hs.entity.QProductPersistence.productPersistence
import com.hs.entity.ProductPersistence
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ProductQuerydslRepository(private val queryFactory: JPAQueryFactory) : ProductQueryRepository {

    override fun findProductWithFetchJoin(id: Long): Product? {
        val productPersistence: ProductPersistence? = queryFactory
            .selectFrom(productPersistence)
            .join(productPersistence.productImages, productImagePersistence).fetchJoin()
            .where(productPersistence.id.eq(id))
            .fetchOne()

        productPersistence ?: return null

        return Product.mapOf(
            id = productPersistence.id!!,
            name = productPersistence.name,
            price = productPersistence.price,
            stockQuantity = productPersistence.stockQuantity,
            imageUrls = productPersistence.createImageUrls(),
            confirmStatus = productPersistence.confirmStatus,
            createdDate = productPersistence.createdDate,
            updatedDate = productPersistence.updatedDate,
            deletedDate = productPersistence.deletedDate
        )
    }
}
