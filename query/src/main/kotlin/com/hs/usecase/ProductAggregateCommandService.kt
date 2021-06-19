package com.hs.usecase

import com.hs.dto.FindProductAggregateDto
import com.hs.entity.ProductAggreagateRepository
import com.hs.entity.ProductAggreagte
import com.hs.entity.ProductAggregateType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductAggregateCommandService(
    private val productAggregateRepository: ProductAggreagateRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun createOrUpdate(productAggregateDto: FindProductAggregateDto) {
        val type = ProductAggregateType.FIND_PRODUCT
        var productAggreagte: ProductAggreagte? =
            productAggregateRepository.findByProductIdAndType(productId = productAggregateDto.productId, type = type)

        when (productAggreagte) {
            null -> productAggreagte = ProductAggreagte.create(productAggregateDto = productAggregateDto, type = type)
            else -> productAggreagte.changeProductAggregateData(data = productAggregateDto)
        }

        productAggregateRepository.save(productAggreagte)

        logger.info("[ Service ] productAggregate : {}", productAggreagte)
    }
}
