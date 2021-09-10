package com.hs.message

enum class QueryAppExceptionMessage(val localizedMessage: String) {
    NOT_FOUND_PRODUCT("[Query - Application] 해당 상품이 존재하지 않습니다. 상품 번호를 다시 확인해주세요!"),
    PRODUCT_ID_IS_NULL_OR_BLANK("[Query - Domain] 생성된 상품의 ID가 없습니다."),
}
