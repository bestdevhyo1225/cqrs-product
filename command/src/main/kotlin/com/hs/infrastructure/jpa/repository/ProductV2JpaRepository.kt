package com.hs.infrastructure.jpa.repository

import com.hs.entity.ProductConfirmStatus
import com.hs.entity.ProductV2
import com.hs.infrastructure.jpa.mapper.ProductMapper
import com.hs.infrastructure.jpa.persistence.ProductPersistence
import com.hs.repository.ProductV2Repository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
@Transactional
class ProductV2JpaRepository(private val entityManager: EntityManager) : ProductV2Repository {

    override fun save(product: ProductV2): ProductV2 {
        val productPersistence = ProductMapper.toPersistenceEntity(product = product)

        entityManager.persist(productPersistence)

        product.reflectIdAfterPersistence(id = productPersistence.id)

        return product
    }

    override fun update(product: ProductV2) {
        val productPersistence: ProductPersistence? = findOne(product.id!!)

        productPersistence?.update(
            name = product.name,
            price = product.price,
            stockQuantity = product.stockQuantity
        )
    }

    override fun updateStockQuantity(id: Long?, stockQuantity: Int) {
        val productPersistence: ProductPersistence? = findOne(id = id!!)

        productPersistence?.decreaseStockCount(stockQuantity = stockQuantity)
    }

    override fun updateConfirmStatus(id: Long?, confirmStatus: ProductConfirmStatus) {
        val productPersistence: ProductPersistence? = findOne(id = id!!)

        productPersistence?.updateConfirmStatus(confirmStatus = confirmStatus)
    }

    override fun findProduct(id: Long): ProductV2? {
        val productPersistence: ProductPersistence? = findOne(id = id)

        return ProductMapper.toDomainEntity(productPersistence = productPersistence)
    }

    override fun findProductWithFetchJoin(id: Long): ProductV2? {
        val productPersistence: ProductPersistence? = entityManager
            .createQuery(
                "SELECT p FROM ProductPersistence p JOIN FETCH p.productImages WHERE p.id = :id",
                ProductPersistence::class.java
            )
            .setParameter("id", id)
            .singleResult

        return ProductMapper.toDomainEntity(productPersistence = productPersistence, usedFetchJoin = true)
    }

    private fun findOne(id: Long): ProductPersistence? {
        return entityManager.find(ProductPersistence::class.java, id)
    }
}
