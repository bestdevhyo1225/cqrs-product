package com.hs.repository

import com.hs.entity.ProductEventLog
import org.springframework.data.jpa.repository.JpaRepository

interface ProductEventLogRepository : JpaRepository<ProductEventLog, Long>
