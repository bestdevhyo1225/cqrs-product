package com.hs.infrastructure.jpa.repository

import com.hs.entity.Product
import com.hs.infrastructure.jpa.mapper.ProductMapper
import com.hs.infrastructure.jpa.persistence.ProductImagePersistence
import com.hs.infrastructure.jpa.persistence.ProductPersistence
import com.hs.repository.ProductRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
@Transactional
class ProductJpaRepository(private val entityManager: EntityManager) : ProductRepository {

    override fun save(product: Product): Product {
        val productPersistence = ProductMapper.toPersistenceEntity(product = product)

        entityManager.persist(productPersistence)

        product.reflectIdAfterPersistence(id = productPersistence.id)

        return product
    }

    override fun saveAllImage(product: Product, imageUrls: List<String>) {
        imageUrls.forEach { imageUrl ->
            val productPersistence: ProductPersistence? = getReferenceOne(id = product.id!!)
            val productImagePersistence = ProductImagePersistence(url = imageUrl, product = productPersistence!!)

            entityManager.persist(productImagePersistence)
        }
    }

    override fun update(product: Product) {
        val productPersistence: ProductPersistence? = getReferenceOne(product.id!!)

        productPersistence?.update(
            name = product.name,
            price = product.price,
            stockQuantity = product.stockQuantity
        )
    }

    override fun updateStockQuantity(product: Product) {
        val productPersistence: ProductPersistence? = getReferenceOne(id = product.id!!)

        productPersistence?.decreaseStockCount(stockQuantity = product.stockQuantity)
    }

    override fun updateConfirmStatus(product: Product) {
        val productPersistence: ProductPersistence? = getReferenceOne(id = product.id!!)

        productPersistence?.updateConfirmStatus(confirmStatus = product.confirmStatus)
    }

    override fun deleteImageByProductId(productId: Long) {
        entityManager.createQuery("DELETE FROM ProductImagePersistence pi WHERE pi.product.id = :productId")
            .setParameter("productId", productId)
            .executeUpdate()
    }

    override fun findProduct(id: Long): Product? {
        val productPersistence: ProductPersistence? = findOne(id = id)

        return ProductMapper.toDomainEntity(productPersistence = productPersistence)
    }

    override fun findProductWithFetchJoin(id: Long): Product? {
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

    private fun getReferenceOne(id: Long): ProductPersistence? {
        return entityManager.getReference(ProductPersistence::class.java, id)
    }
}
