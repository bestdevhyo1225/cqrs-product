package com.hs.config.redis

import com.hs.dto.FindProductAggregateDto
import com.hs.dto.FindProductDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
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

    @Bean
    fun redisCacheConnectionFactory(): RedisConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration(host, cachePort)

        redisStandaloneConfiguration.setPassword(password)

        return LettuceConnectionFactory(redisStandaloneConfiguration)
    }

    @Bean
    fun productAggregateRedisTemplate(): RedisTemplate<String, FindProductAggregateDto> {
        val redisTemplate = RedisTemplate<String, FindProductAggregateDto>()
        val stringRedisSerializer = StringRedisSerializer()
        val genericJackson2JsonRedisSerializer = GenericJackson2JsonRedisSerializer()

        redisTemplate.setConnectionFactory(redisCacheConnectionFactory())

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
            .entryTtl(Duration.ofSeconds(180L))

        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisCacheConnectionFactory())
            .cacheDefaults(defaultCacheConfiguration)
            .build()
    }
}
