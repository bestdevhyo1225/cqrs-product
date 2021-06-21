package com.hs.application.usecase

import com.hs.dto.FindProductDto
import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType
import com.hs.repository.ProductAggregateRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductAggregateCommand(
    private val productAggregateRepository: ProductAggregateRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    suspend fun createOrUpdate(productDto: FindProductDto) {
        val type = ProductAggregateType.FIND_PRODUCT
        var productAggregate: ProductAggregate? =
            productAggregateRepository.findByProductIdAndType(productId = productDto.productId, type = type)

        when (productAggregate) {
            null -> productAggregate = ProductAggregate.create(productDto = productDto, type = type)
            else -> productAggregate.changeProductAggregateData(data = productDto)
        }

        productAggregateRepository.save(productAggregate)

        logger.info("[ Service ] productAggregate : {}", productAggregate)
    }
}
