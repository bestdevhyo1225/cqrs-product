package com.hs.infrastructure.jpa.repository

import com.hs.entity.ProductEventLogV2
import com.hs.infrastructure.jpa.mapper.ProductEventLogMapper
import com.hs.repository.ProductEventLogV2Repository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
@Transactional
class ProductEventLogV2JpaRepository(private val entityManager: EntityManager) : ProductEventLogV2Repository {

    override fun save(productEventLog: ProductEventLogV2): ProductEventLogV2 {
        val productEventLogPersistence = ProductEventLogMapper.toPersistenceEntity(productEventLog = productEventLog)

        entityManager.persist(productEventLogPersistence)

        productEventLog.reflectIdAfterPersistence(id = productEventLogPersistence.id)

        return productEventLog
    }
}
