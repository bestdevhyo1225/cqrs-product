package com.hs.config.redis

import com.hs.dto.FindPaginationDto
import org.springframework.cache.Cache
import org.springframework.cache.interceptor.CacheOperationInvocationContext
import org.springframework.cache.interceptor.CacheResolver
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.core.RedisTemplate

class ProductAggregatePageCacheResolver(
    private val redisCacheManager: RedisCacheManager,
    private val productAggregatePageRedisTemplate: RedisTemplate<String, FindPaginationDto>
) : CacheResolver {

    override fun resolveCaches(context: CacheOperationInvocationContext<*>): MutableCollection<out Cache> {
        val productId: Long = "${context.args[0]}".toLong()

        val caches: MutableCollection<Cache> = mutableListOf()
        val cache: Cache = redisCacheManager.getCache(RedisConfig.PRODUCT_AGGREGATE_PAGE_CACHE_NAME) ?: return caches

        caches.add(cache)

        return caches
    }
}
