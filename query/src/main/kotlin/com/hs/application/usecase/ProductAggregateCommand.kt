package com.hs.application.usecase

import com.hs.dto.FindProductDto
import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType.FIND_PRODUCT
import com.hs.repository.QueryAppProductAggregateRepository
import com.hs.service.RestGetRequestor
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service

@Service
class ProductAggregateCommand(
    private val restGetRequestor: RestGetRequestor,
    private val productAggregateRepository: QueryAppProductAggregateRepository,
) {

    @Caching(
        evict = [
            CacheEvict(value = ["productAggregates"], key = "#productId", cacheManager = "redisCacheManager")
        ]
    )
    fun createOrUpdate(productId: Long) = runBlocking {
        val asyncProductDto: Deferred<FindProductDto> =
            async(Dispatchers.IO) { restGetRequestor.getProduct(productId = productId) }

        val asyncProductAggregate: Deferred<ProductAggregate?> =
            async(Dispatchers.IO) { findProductAggregate(productId = productId) }

        val productDto: FindProductDto = asyncProductDto.await()
        when (val productAggregate: ProductAggregate? = asyncProductAggregate.await()) {
            null -> {
                productAggregateRepository.insert(
                    productAggregate = ProductAggregate.create(
                        productDto = productDto,
                        type = FIND_PRODUCT
                    )
                )
            }
            else -> {
                productAggregate.changeProductAggregateData(data = productDto)
                productAggregateRepository.save(productAggregate = productAggregate)
            }
        }
    }

    suspend fun findProductAggregate(productId: Long): ProductAggregate? {
        return productAggregateRepository.findByProductIdAndType(productId = productId, type = FIND_PRODUCT)
    }
}
