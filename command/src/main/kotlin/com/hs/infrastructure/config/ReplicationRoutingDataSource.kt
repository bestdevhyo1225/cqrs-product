package com.hs.infrastructure.config

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

class ReplicationRoutingDataSource : AbstractRoutingDataSource() {

    override fun determineCurrentLookupKey(): ReplicationDataSourceType {
        return if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            ReplicationDataSourceType.READ
        } else {
            ReplicationDataSourceType.WRITE
        }
    }
}
