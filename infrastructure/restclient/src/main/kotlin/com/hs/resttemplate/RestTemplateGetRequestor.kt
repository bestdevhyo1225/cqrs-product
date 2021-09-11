package com.hs.infrastructure.rest.resttemplate

import com.hs.dto.FindProductDto
import com.hs.infrastructure.rest.RestGetRequestor
import com.hs.response.SuccessResponse
import com.hs.util.ParameterizedTypeReferenceUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

class RestTemplateGetRequestor(private val restTemplate: RestTemplate) : RestGetRequestor {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    /*
    * suspend 키워드가 있으면, 코루틴 컨텍스트 환경에서만 실행할 수 있다는 의미이다. 만약, 해당 키워드를 붙이지 않으면,
    * 어디에서나 실행할 수 있는 일반 메소드이다.
    * */
    override suspend fun getProduct(productId: Long): FindProductDto {
        val httpHeaders = HttpHeaders()
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        val responseEntity: ResponseEntity<SuccessResponse<FindProductDto>> = restTemplate.exchange(
            "http://localhost:9700/products/${productId}",
            HttpMethod.GET,
            HttpEntity(null, httpHeaders),
            ParameterizedTypeReferenceUtils.typeRef<SuccessResponse<FindProductDto>>()
        )

        logger.info("[ RestGetRequestor ] responseEntity : {}", responseEntity)

        return responseEntity.body!!.data
    }
}
