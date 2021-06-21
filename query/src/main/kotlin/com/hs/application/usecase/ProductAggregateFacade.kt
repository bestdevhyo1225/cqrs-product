package com.hs.application.usecase

import com.hs.dto.FindProductDto
import com.hs.entity.ProductAggregate
import com.hs.service.RestGetRequestor
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class ProductAggregateFacade(
    private val restGetRequestor: RestGetRequestor,
    private val productAggregateQueryProcessor: ProductAggregateQueryProcessor,
    private val productAggregateCommandProcessor: ProductAggregateCommandProcessor
) {

    fun createOrUpdate(productId: Long) = runBlocking {
        val asyncProductDto: Deferred<FindProductDto> =
            async(Dispatchers.IO) { restGetRequestor.getProduct(productId = productId) }

        val asyncProductAggregate: Deferred<ProductAggregate?> =
            async(Dispatchers.IO) { productAggregateQueryProcessor.findProductAggregateForCommand(productId = productId) }

        val productDto: FindProductDto = asyncProductDto.await()

        when (val productAggregate: ProductAggregate? = asyncProductAggregate.await()) {
            null -> productAggregateCommandProcessor.create(productDto = productDto)
            else -> productAggregateCommandProcessor.update(
                productAggregate = productAggregate,
                productDto = productDto
            )
        }
    }
}
