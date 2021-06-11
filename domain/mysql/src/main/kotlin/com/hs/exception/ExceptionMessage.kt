package com.hs.exception

enum class ExceptionMessage(val str: String) {
    PRODUCT_ID_IS_NULL("생성된 상품의 ID가 Null입니다. Event를 발행할 수 없습니다."),
    HAVE_EXCEEDED_THE_QUANTITY_AVAILABLE_FOR_PURCHASE("구매할 수 있는 수량을 초과했습니다."),
    NOT_EXIST_PRODUCT_CONFIRM_STATUS("존재하지 않는 Confirm Status입니다.")
}
