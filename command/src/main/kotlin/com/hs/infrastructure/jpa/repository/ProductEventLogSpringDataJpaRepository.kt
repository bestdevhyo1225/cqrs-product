package com.hs.infrastructure.jpa.repository

import com.hs.infrastructure.jpa.persistence.ProductEventLogPersistence
import org.springframework.data.jpa.repository.JpaRepository

interface ProductEventLogSpringDataJpaRepository : JpaRepository<ProductEventLogPersistence, Long>
