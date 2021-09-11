package com.hs.webclient

import com.hs.RestGetRequestor
import com.hs.dto.FindProductDto

class WebClientGetRequestor : RestGetRequestor {

    /*
    * suspend 키워드가 있으면, 코루틴 컨텍스트 환경에서만 실행할 수 있다는 의미이다. 만약, 해당 키워드를 붙이지 않으면,
    * 어디에서나 실행할 수 있는 일반 메소드이다.
    * */
    override suspend fun getProduct(productId: Long): FindProductDto {
        TODO("Not yet implemented")
    }
}
