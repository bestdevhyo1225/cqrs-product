package com.hs.resolver

import com.hs.config.RedisConfig
import com.hs.dto.FindPaginationDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.Cache
import org.springframework.cache.interceptor.CacheOperationInvocationContext
import org.springframework.cache.interceptor.CacheResolver
import org.springframework.dao.DataAccessException
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions

class ProductAggregatePageCacheEvictResolver(
    private val redisCacheManager: RedisCacheManager,
    private val productAggregatePageRedisTemplate: RedisTemplate<String, FindPaginationDto>
) : CacheResolver {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun resolveCaches(context: CacheOperationInvocationContext<*>): MutableCollection<out Cache> {
        val productId: Long = "${context.args[0]}".toLong()

        val caches: MutableCollection<Cache> = mutableListOf()
        val cache: Cache = redisCacheManager.getCache(RedisConfig.PRODUCT_AGGREGATE_PAGE_CACHE_NAME) ?: return caches

        caches.add(cache)

        return caches
    }

    private fun deletePageCache(productId: Long) {
        productAggregatePageRedisTemplate.execute {
            RedisCallback<String> {
                fun doInRedis(connection: RedisConnection): String {
                    try {
                        val scanOptions = ScanOptions.scanOptions()
                            .match("${RedisConfig.PRODUCT_AGGREGATE_PAGE_CACHE_NAME}::*")
                            .count(200)
                            .build()

                        val entries: Cursor<ByteArray> = connection.scan(scanOptions)

                        while (entries.hasNext()) {
                            val entry: ByteArray? = entries.next()
                        }
                    } catch (exception: DataAccessException) {
                        logger.error("DataAccessException : {}", exception.localizedMessage)
                    }

                    return ""
                }

                return@RedisCallback ""
            }
        }
    }
}
