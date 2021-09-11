package com.hs.config.redis

import com.hs.dto.FindPaginationDto
import com.hs.dto.FindProductAggregateDto
import com.hs.infrastructure.redis.keygenerator.ProductAggregateCacheKeyGenerator
import com.hs.infrastructure.redis.resolver.ProductAggregateCacheEvictResolver
import com.hs.infrastructure.redis.resolver.ProductAggregatePageCacheEvictResolver
import com.hs.infrastructure.redis.resolver.ProductAggregateCacheableResolver
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.CacheResolver
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableCaching(proxyTargetClass = true)
class RedisConfig(
    @Value("\${spring.redis.host}")
    private val host: String,

    @Value("\${spring.redis.cache.port}")
    private val cachePort: Int,

    @Value("\${spring.redis.password}")
    private val password: String,
) {

    companion object {
        const val DEFAULT_TTL: Long = 60L
        const val PRODUCT_AGGREGATE_TTL: Long = 180L
        const val PRODUCT_AGGREGATE_PAGE_TTL: Long = 120L
        const val PRODUCT_AGGREGATE_CACHE_NAME = "productAggregate"
        const val PRODUCT_AGGREGATE_PAGE_CACHE_NAME = "productAggregatePage"
        const val PRODUCT_AGGREGATE_COPY_COUNT: Int = 5
    }

    @Bean
    fun redisCacheConnectionFactory(): RedisConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration(host, cachePort)

        redisStandaloneConfiguration.setPassword(password)

        return LettuceConnectionFactory(redisStandaloneConfiguration)
    }

    @Bean
    fun productAggregateRedisTemplate(): RedisTemplate<String, FindProductAggregateDto> {
        val redisTemplate = RedisTemplate<String, FindProductAggregateDto>()

        redisTemplate.setConnectionFactory(redisCacheConnectionFactory())

        val stringRedisSerializer = StringRedisSerializer()
        val genericJackson2JsonRedisSerializer = GenericJackson2JsonRedisSerializer()

        redisTemplate.keySerializer = stringRedisSerializer
        redisTemplate.valueSerializer = genericJackson2JsonRedisSerializer

        redisTemplate.hashKeySerializer = stringRedisSerializer
        redisTemplate.hashValueSerializer = genericJackson2JsonRedisSerializer

        return redisTemplate
    }

    @Bean
    fun productAggregatePageRedisTemplate(): RedisTemplate<String, FindPaginationDto> {
        val redisTemplate = RedisTemplate<String, FindPaginationDto>()

        redisTemplate.setConnectionFactory(redisCacheConnectionFactory())

        val stringRedisSerializer = StringRedisSerializer()
        val genericJackson2JsonRedisSerializer = GenericJackson2JsonRedisSerializer()

        redisTemplate.keySerializer = stringRedisSerializer
        redisTemplate.valueSerializer = genericJackson2JsonRedisSerializer

        redisTemplate.hashKeySerializer = stringRedisSerializer
        redisTemplate.hashValueSerializer = genericJackson2JsonRedisSerializer

        return redisTemplate
    }

    @Bean
    fun redisCacheManager(): RedisCacheManager {
        val defaultCacheConfiguration = RedisCacheConfiguration
            .defaultCacheConfig()
            .disableCachingNullValues()
            .serializeKeysWith(fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(fromSerializer(GenericJackson2JsonRedisSerializer()))
            .entryTtl(Duration.ofSeconds(DEFAULT_TTL))

        val cacheConfigurations: MutableMap<String, RedisCacheConfiguration> = HashMap()

        cacheConfigurations[PRODUCT_AGGREGATE_CACHE_NAME] =
            defaultCacheConfiguration.entryTtl(Duration.ofSeconds(PRODUCT_AGGREGATE_TTL))

        cacheConfigurations[PRODUCT_AGGREGATE_PAGE_CACHE_NAME] =
            defaultCacheConfiguration.entryTtl(Duration.ofSeconds(PRODUCT_AGGREGATE_PAGE_TTL))

        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisCacheConnectionFactory())
            .cacheDefaults(defaultCacheConfiguration)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }

    @Bean
    fun productAggregateCacheKeyGenerator(): KeyGenerator {
        return ProductAggregateCacheKeyGenerator()
    }

    @Bean
    fun productAggregateCacheableResolver(): CacheResolver {
        return ProductAggregateCacheableResolver(
            redisCacheManager = redisCacheManager(),
            productAggregateRedisTemplate = productAggregateRedisTemplate()
        )
    }

    @Bean
    fun productAggregateCacheEvictResolver(): CacheResolver {
        return ProductAggregateCacheEvictResolver(
            redisCacheManager = redisCacheManager(),
            productAggregateRedisTemplate = productAggregateRedisTemplate()
        )
    }


    @Bean
    fun productAggregatePageCacheEvictResolver(): CacheResolver {
        return ProductAggregatePageCacheEvictResolver(
            redisCacheManager = redisCacheManager(),
            productAggregatePageRedisTemplate = productAggregatePageRedisTemplate()
        )
    }
}
