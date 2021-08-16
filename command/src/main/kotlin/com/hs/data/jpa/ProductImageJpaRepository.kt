package com.hs.data.jpa

import com.hs.entity.ProductImage
import com.hs.repository.ProductImageRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class ProductImageJpaRepository(private val entityManager: EntityManager) : ProductImageRepository {

    override fun saveAll(productImages: List<ProductImage>): List<ProductImage> {
        productImages.forEach { entityManager.persist(it) }
        return productImages
    }

    override fun deleteByProductId(productId: Long) {
        entityManager.createQuery("DELETE FROM ProductImage pi WHERE pi.product.id = :productId")
            .setParameter("productId", productId)
            .executeUpdate()
    }
}
