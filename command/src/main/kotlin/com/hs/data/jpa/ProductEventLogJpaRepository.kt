package com.hs.data.jpa

import com.hs.entity.ProductEventLog
import com.hs.repository.ProductEventLogRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class ProductEventLogJpaRepository(private val entityManager: EntityManager) : ProductEventLogRepository {

    override fun save(productEventLog: ProductEventLog): ProductEventLog {
        entityManager.persist(productEventLog)
        return productEventLog
    }
}
