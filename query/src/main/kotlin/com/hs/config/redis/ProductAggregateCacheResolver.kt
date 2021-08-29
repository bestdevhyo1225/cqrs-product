package com.hs.config.redis

import com.hs.dto.FindProductAggregateDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.Cache
import org.springframework.cache.interceptor.CacheOperationInvocationContext
import org.springframework.cache.interceptor.CacheResolver
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

class ProductAggregateCacheResolver(
    private val redisCacheManager: RedisCacheManager,
    private val productAggregateRedisTemplate: RedisTemplate<String, FindProductAggregateDto>
) : CacheResolver {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        private const val PRODUCT_AGGREGATES_CACHE_NAME = "productAggregates"
    }

    override fun resolveCaches(context: CacheOperationInvocationContext<*>): MutableCollection<out Cache> {
        val key: String = "$PRODUCT_AGGREGATES_CACHE_NAME::" + context.args[0]

        if (isExpired(key = key)) updateExpire(key = key)

        val caches: MutableCollection<Cache> = mutableListOf()

        val cache: Cache = redisCacheManager.getCache(PRODUCT_AGGREGATES_CACHE_NAME) ?: return caches

        caches.add(cache)

        return caches
    }

    private fun isExpired(key: String, expireTimeGapMs: Long = 3000): Boolean {
        val currentExpireTimeMS: Long = productAggregateRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS)

        if (currentExpireTimeMS < 0) return false

        return currentExpireTimeMS - Math.random() * expireTimeGapMs <= 0
    }

    private fun updateExpire(key: String) {
        if (productAggregateRedisTemplate.expire(key, RedisConfig.PRODUCT_AGGREGATE_TTL, TimeUnit.SECONDS)) {
            logger.info("The expiration time of key has been updated (key : {})", key)
        }
    }
}
