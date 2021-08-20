package com.hs.repository

import com.hs.entity.ProductEventLogV2

interface ProductEventLogV2Repository {
    fun save(productEventLog: ProductEventLogV2): ProductEventLogV2
}
