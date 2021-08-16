package com.hs.data.jpa.repository

import com.hs.entity.ProductEventLog
import com.hs.repository.ProductEventLogRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
@Transactional
class ProductEventLogJpaRepository(private val entityManager: EntityManager) : ProductEventLogRepository {

    override fun save(productEventLog: ProductEventLog): ProductEventLog {
        entityManager.persist(productEventLog)
        return productEventLog
    }
}
