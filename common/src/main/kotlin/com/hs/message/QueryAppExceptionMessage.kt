package com.hs.message

enum class QueryAppExceptionMessage(val localizedMessage: String) {
    NOT_FOUND_PRODUCT("[Query - Application] 해당 상품이 존재하지 않습니다. 상품 번호를 다시 확인해주세요!"),
    NOT_FOUND_PRODUCT_BY_CREATE_OR_UPDATE("[Query - Application] 해당 상품은 현재 확인할 수 없는 상태입니다. (생성 or 수정된 상태)"),
}
