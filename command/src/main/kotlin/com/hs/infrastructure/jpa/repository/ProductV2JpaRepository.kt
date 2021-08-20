package com.hs.infrastructure.jpa.repository

import com.hs.entity.ProductV2
import com.hs.infrastructure.jpa.mapper.EntityMapper
import com.hs.infrastructure.jpa.persistence.ProductPersistence
import com.hs.repository.ProductV2Repository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
@Transactional
class ProductV2JpaRepository(private val entityManager: EntityManager) : ProductV2Repository {

    override fun save(product: ProductV2): ProductV2 {
        val productPersistence = ProductPersistence.create(
            name = product.getName(),
            price = product.getPrice(),
            stockQuantity = product.getStockQuantity(),
            imageUrls = product.getImageUrls()
        )

        entityManager.persist(productPersistence)

        product.changeId(id = productPersistence.id)

        return product
    }

    override fun update(product: ProductV2) {
        val productPersistence: ProductPersistence =
            entityManager.find(ProductPersistence::class.java, product.getId()!!)

        productPersistence.update(
            name = product.getName(),
            price = product.getPrice(),
            stockQuantity = product.getStockQuantity()
        )
    }

    override fun findProduct(id: Long): ProductV2? {
        val productPersistence: ProductPersistence? = entityManager.find(ProductPersistence::class.java, id)
        return EntityMapper.toProductEntity(productPersistence)
    }

    override fun findProductWithFetchJoin(id: Long): ProductV2? {
        val productPersistence: ProductPersistence? = entityManager
            .createQuery(
                "SELECT p FROM ProductPersistence p JOIN FETCH p.productImages WHERE p.id = :id",
                ProductPersistence::class.java
            )
            .setParameter("id", id)
            .singleResult

        return EntityMapper.toProductEntity(productPersistence)
    }
}
