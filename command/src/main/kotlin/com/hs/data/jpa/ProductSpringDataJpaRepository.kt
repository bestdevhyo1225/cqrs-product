package com.hs.data.jpa

import com.hs.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductSpringDataJpaRepository : JpaRepository<Product, Long>