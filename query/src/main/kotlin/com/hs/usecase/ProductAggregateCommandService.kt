package com.hs.usecase

import com.hs.dto.FindProductAggregateDto
import com.hs.entity.ProductAggreagateRepository
import com.hs.entity.ProductAggreagte
import com.hs.entity.ProductAggregateType
import org.springframework.stereotype.Service

@Service
class CommandService(
    private val productAggregateRepository: ProductAggreagateRepository
) {

    fun createProductAggregate(productAggregateDto: FindProductAggregateDto) {
        val productAggreagte =
            ProductAggreagte.create(productAggregateDto = productAggregateDto, type = ProductAggregateType.FIND_PRODUCT)

        productAggregateRepository.insert(productAggreagte)
    }
}
