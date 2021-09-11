package com.hs.jpa.repository

import com.hs.jpa.entity.Product
import com.hs.jpa.entity.ProductEventLogPersistence
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
@Transactional
class ProductEventLogJpaRepository(private val entityManager: EntityManager) : ProductEventLogRepository {

    override fun save(productId: Long, eventStatus: Product.EventStatus, message: String) {
        entityManager.persist(
            ProductEventLogPersistence.create(
                productId = productId,
                eventStatus = eventStatus,
                message = message
            )
        )
    }
}
