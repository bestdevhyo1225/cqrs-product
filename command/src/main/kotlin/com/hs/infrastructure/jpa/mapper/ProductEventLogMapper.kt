package com.hs.infrastructure.jpa.mapper

import com.hs.entity.ProductEventLogV2
import com.hs.infrastructure.jpa.persistence.ProductEventLogPersistence

class ProductEventLogMapper {
    companion object {
        fun toDomainEntity(productEventLogPersistence: ProductEventLogPersistence): ProductEventLogV2? {
            TODO()
        }

        fun toPersistenceEntity(productEventLog: ProductEventLogV2): ProductEventLogPersistence {
            TODO()
        }
    }
}
