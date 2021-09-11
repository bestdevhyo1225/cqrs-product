package com.hs.repository

import com.hs.entity.Product
import com.hs.entity.ProductEventLogPersistence
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
