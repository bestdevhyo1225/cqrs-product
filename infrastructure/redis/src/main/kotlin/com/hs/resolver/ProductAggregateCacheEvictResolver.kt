package com.hs.infrastructure.redis.resolver

import com.hs.config.redis.RedisConfig
import com.hs.dto.FindProductAggregateDto
import org.springframework.cache.Cache
import org.springframework.cache.interceptor.CacheOperationInvocationContext
import org.springframework.cache.interceptor.CacheResolver
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer

class ProductAggregateCacheEvictResolver(
    private val redisCacheManager: RedisCacheManager,
    private val productAggregateRedisTemplate: RedisTemplate<String, FindProductAggregateDto>
) : CacheResolver {

    override fun resolveCaches(context: CacheOperationInvocationContext<*>): MutableCollection<out Cache> {
        delete(productId = context.args[0].toString())

        return mutableListOf(redisCacheManager.getCache(RedisConfig.PRODUCT_AGGREGATE_CACHE_NAME)!!)
    }

    private fun delete(productId: String) {
        val keySerializer: RedisSerializer<String> = productAggregateRedisTemplate.stringSerializer

        productAggregateRedisTemplate.executePipelined { connection: RedisConnection ->
            (0 until RedisConfig.PRODUCT_AGGREGATE_COPY_COUNT)
                .map { "${RedisConfig.PRODUCT_AGGREGATE_CACHE_NAME}::copy-${it}::${productId}" }
                .forEach { connection.del(keySerializer.serialize(it)) }

            return@executePipelined null
        }
    }
}
