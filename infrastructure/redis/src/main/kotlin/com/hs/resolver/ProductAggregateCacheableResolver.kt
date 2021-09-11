package com.hs.resolver

import com.hs.config.RedisConfig
import com.hs.dto.FindProductAggregateDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.Cache
import org.springframework.cache.interceptor.CacheOperationInvocationContext
import org.springframework.cache.interceptor.CacheResolver
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

class ProductAggregateCacheableResolver(
    private val redisCacheManager: RedisCacheManager,
    private val productAggregateRedisTemplate: RedisTemplate<String, FindProductAggregateDto>
) : CacheResolver {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun resolveCaches(context: CacheOperationInvocationContext<*>): MutableCollection<out Cache> {
        val key = "${RedisConfig.PRODUCT_AGGREGATE_CACHE_NAME}::${context.args[0]}::${context.args[1]}"

        if (isExpired(key = key)) updateExpire(key = key)

        return mutableListOf(redisCacheManager.getCache(RedisConfig.PRODUCT_AGGREGATE_CACHE_NAME)!!)
    }

    private fun isExpired(key: String, expireTimeGapMs: Long = 3000): Boolean {
        val currentExpireTimeMS: Long = productAggregateRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS)
        return currentExpireTimeMS >= 0 && currentExpireTimeMS - Math.random() * expireTimeGapMs <= 0
    }

    private fun updateExpire(key: String) {
        if (productAggregateRedisTemplate.expire(key, RedisConfig.PRODUCT_AGGREGATE_TTL, TimeUnit.SECONDS)) {
            logger.info("The expiration time of key has been updated (key : {})", key)
        }
    }
}
