package com.hs.util

import org.springframework.core.ParameterizedTypeReference

object ParameterizedTypeReferenceUtils {
    inline fun <reified T : Any> typeRef(): ParameterizedTypeReference<T> {
        return object : ParameterizedTypeReference<T>() {}
    }
}
