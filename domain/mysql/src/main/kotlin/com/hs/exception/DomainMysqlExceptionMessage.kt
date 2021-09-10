package com.hs.exception

enum class DomainMysqlExceptionMessage(val localizedMessage: String) {
    HAVE_EXCEEDED_THE_QUANTITY_AVAILABLE_FOR_PURCHASE("[Command - Domain] 구매할 수 있는 수량을 초과했습니다."),
    NOT_EXIST_PRODUCT_CONFIRM_STATUS("[Command - Domain] 존재하지 않는 Confirm Status입니다."),
    PRODUCT_ID_IS_NULL("[Command - Domain] 생성된 상품의 ID가 Null입니다."),
}
