package com.hs.config.redis

import org.springframework.cache.Cache
import org.springframework.cache.interceptor.CacheOperationInvocationContext
import org.springframework.cache.interceptor.CacheResolver
import org.springframework.data.redis.cache.RedisCacheManager

class ProductAggregateCacheResolver(
    private val redisCacheManager: RedisCacheManager
) : CacheResolver {

    companion object {
        private const val PRODUCT_AGGREGATES_CACHE = "productAggregates"
    }

    override fun resolveCaches(context: CacheOperationInvocationContext<*>): MutableCollection<out Cache> {
        val caches: MutableCollection<Cache> = mutableListOf()

        redisCacheManager.getCache(PRODUCT_AGGREGATES_CACHE)?.let { caches.add(it) }

        return caches
    }
}
