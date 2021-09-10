package com.hs.exception

enum class DomainMongoExceptionMessage(val localizedMessage: String) {
    PRODUCT_ID_IS_NULL_OR_BLANK("[Query - Domain] 생성된 상품의 ID가 없습니다."),
}
