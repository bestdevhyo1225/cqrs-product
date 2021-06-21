package com.hs.application.usecase

import com.hs.service.RestGetRequestor
import com.hs.dto.FindProductDto
import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType.FIND_PRODUCT
import com.hs.repository.ProductAggregateRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductAggregateCommandProcessor(
    private val restGetRequestor: RestGetRequestor,
    private val productAggregateRepository: ProductAggregateRepository,
    private val productAggregateQueryProcessor: ProductAggregateQueryProcessor
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun createOrUpdate(productId: Long) = runBlocking {
        val asyncProductDto: Deferred<FindProductDto> =
            async(Dispatchers.IO) { restGetRequestor.asyncGetProduct(productId = productId) }

        val asyncProductAggregate: Deferred<ProductAggregate?> =
            async(Dispatchers.IO) { productAggregateQueryProcessor.asyncFindProductAggregate(productId = productId) }

        val productDto: FindProductDto = asyncProductDto.await()
        var productAggregate: ProductAggregate? = asyncProductAggregate.await()

        when (productAggregate) {
            null -> productAggregate = ProductAggregate.create(productDto = productDto, type = FIND_PRODUCT)
            else -> productAggregate.changeProductAggregateData(data = productDto)
        }

        productAggregateRepository.save(productAggregate)

        logger.info("[ ProductAggregateCommandProcessor ] productAggregate : {}", productAggregate)
    }
}
