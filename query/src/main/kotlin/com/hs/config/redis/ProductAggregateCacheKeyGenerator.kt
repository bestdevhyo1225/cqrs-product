package com.hs.config.redis

import org.springframework.cache.interceptor.KeyGenerator
import java.lang.reflect.Method

class ProductAggregateCacheKeyGenerator : KeyGenerator {

    override fun generate(target: Any, method: Method, vararg params: Any?): Any {
        TODO("Not yet implemented")
    }
}
