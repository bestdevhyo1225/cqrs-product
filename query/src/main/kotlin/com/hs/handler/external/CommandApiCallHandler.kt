package com.hs.handler.external

import com.hs.dto.FindProductAggregateDto
import com.hs.response.SuccessResponse
import com.hs.util.ParameterizedTypeReferenceUtils
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class CommandApiCallHandler(private val restTemplate: RestTemplate) {

    fun getProductAggregate(url: String): ResponseEntity<SuccessResponse<FindProductAggregateDto>> {
        val httpHeaders = HttpHeaders()
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        return restTemplate.exchange(
            url,
            HttpMethod.GET,
            HttpEntity(null, httpHeaders),
            ParameterizedTypeReferenceUtils.typeRef<SuccessResponse<FindProductAggregateDto>>()
        )
    }
}
