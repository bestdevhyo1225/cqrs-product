package com.hs.usecase

import com.hs.dto.FindProductAggregateDto
import com.hs.entity.ProductAggregateRepository
import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductAggregateCommand(
    private val productAggregateRepository: ProductAggregateRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun createOrUpdate(productAggregateDto: FindProductAggregateDto) {
        val type = ProductAggregateType.FIND_PRODUCT
        var productAggregate: ProductAggregate? =
            productAggregateRepository.findByProductIdAndType(productId = productAggregateDto.productId, type = type)

        when (productAggregate) {
            null -> productAggregate = ProductAggregate.create(productAggregateDto = productAggregateDto, type = type)
            else -> productAggregate.changeProductAggregateData(data = productAggregateDto)
        }

        productAggregateRepository.save(productAggregate)

        logger.info("[ Service ] productAggregate : {}", productAggregate)
    }
}
