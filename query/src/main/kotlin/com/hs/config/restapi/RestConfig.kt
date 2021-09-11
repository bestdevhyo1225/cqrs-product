package com.hs.config.restapi

import com.hs.RestGetRequestor
import com.hs.resttemplate.RestTemplateGetRequestor
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class RestConfig {

    @Bean
    fun restGetRequestor(): RestGetRequestor {
        val restTemplate: RestTemplate = RestTemplateBuilder()
            .requestFactory { BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory()) }
            .setConnectTimeout(Duration.ofMillis(5000))
            .setReadTimeout(Duration.ofMillis(5000))
            .build()

        return RestTemplateGetRequestor(restTemplate = restTemplate)
    }
}
