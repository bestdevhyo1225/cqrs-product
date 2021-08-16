package com.hs.data.jpa

import com.hs.entity.Product
import com.hs.repository.ProductRepository
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : ProductRepository, JpaRepository<Product, Long>
