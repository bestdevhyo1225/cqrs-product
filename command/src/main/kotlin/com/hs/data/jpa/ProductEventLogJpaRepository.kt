package com.hs.data.jpa

import com.hs.entity.ProductEventLog
import com.hs.repository.ProductEventLogRepository
import org.springframework.data.jpa.repository.JpaRepository

interface ProductEventLogJpaRepository : ProductEventLogRepository, JpaRepository<ProductEventLog, Long>
