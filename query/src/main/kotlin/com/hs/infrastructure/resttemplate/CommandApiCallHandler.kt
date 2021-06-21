package com.hs.infrastructure.resttemplate

import com.hs.dto.FindProductDto
import com.hs.response.SuccessResponse
import com.hs.util.ParameterizedTypeReferenceUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class CommandApiCallHandler(private val restTemplate: RestTemplate) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun getProductAggregate(url: String): ResponseEntity<SuccessResponse<FindProductDto>> {
        val httpHeaders = HttpHeaders()
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        val responseEntity: ResponseEntity<SuccessResponse<FindProductDto>> = restTemplate.exchange(
            url,
            HttpMethod.GET,
            HttpEntity(null, httpHeaders),
            ParameterizedTypeReferenceUtils.typeRef<SuccessResponse<FindProductDto>>()
        )

        logger.info("[ Api Call Handler ] responseEntity : {}", responseEntity)

        return responseEntity
    }
}
