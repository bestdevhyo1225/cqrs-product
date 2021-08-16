package com.hs.data.jpa

import com.hs.entity.Product
import com.hs.repository.ProductRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
@Transactional
class ProductJpaRepository(private val entityManager: EntityManager) : ProductRepository {

    override fun save(product: Product): Product {
        entityManager.persist(product)
        return product
    }
}
