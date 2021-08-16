package com.hs.data.jpa

import com.hs.entity.ProductEventLog
import org.springframework.data.jpa.repository.JpaRepository

interface ProductEventLogSpringDataJpaRepository : JpaRepository<ProductEventLog, Long>
