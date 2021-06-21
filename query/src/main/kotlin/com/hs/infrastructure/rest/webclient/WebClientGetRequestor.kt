package com.hs.infrastructure.rest.webclient

import com.hs.dto.FindProductDto
import com.hs.infrastructure.rest.RestGetRequestor
import com.hs.response.SuccessResponse
import org.springframework.http.ResponseEntity

class WebClientGetRequestor : RestGetRequestor {

    /*
    * suspend 키워드가 있으면, 코루틴 컨텍스트 환경에서만 실행할 수 있다는 의미이다. 만약, 해당 키워드를 붙이지 않으면,
    * 어디에서나 실행할 수 있는 일반 메소드이다.
    * */
    override suspend fun getProductAggregate(productId: Long): ResponseEntity<SuccessResponse<FindProductDto>> {
        TODO("Not yet implemented")
    }
}
