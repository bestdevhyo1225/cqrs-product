package com.hs.util

import org.springframework.core.ParameterizedTypeReference

class ParameterizedTypeReferenceUtils {
    companion object {
        inline fun <reified T : Any> typeRef(): ParameterizedTypeReference<T> {
            return object : ParameterizedTypeReference<T>() {}
        }
    }
}
