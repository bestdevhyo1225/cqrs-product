package com.hs.application.usecase

import com.hs.dto.FindProductDto
import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType.FIND_PRODUCT
import com.hs.repository.ProductAggregateRepository
import org.springframework.stereotype.Service

@Service
class ProductAggregateCommandProcessor(
    private val productAggregateRepository: ProductAggregateRepository,
) {

    fun create(productDto: FindProductDto) {
        productAggregateRepository.save(ProductAggregate.create(productDto = productDto, type = FIND_PRODUCT))
    }

    fun update(productAggregate: ProductAggregate, productDto: FindProductDto) {
        productAggregate.changeProductAggregateData(data = productDto)
        productAggregateRepository.save(productAggregate)
    }
}
