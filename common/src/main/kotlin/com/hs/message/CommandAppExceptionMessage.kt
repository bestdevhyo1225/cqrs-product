package com.hs.message

enum class CommandAppExceptionMessage(val localizedMessage: String) {
    NOT_FOUND_PRODUCT("[Command - Application] 해당 상품이 존재하지 않습니다. 상품 번호를 다시 확인해주세요!"),
    PRODUCT_ID_IS_NULL("[Command - Domain] 생성된 상품의 ID가 Null입니다. Event를 발행할 수 없습니다."),
    HAVE_EXCEEDED_THE_QUANTITY_AVAILABLE_FOR_PURCHASE("[Command - Domain] 구매할 수 있는 수량을 초과했습니다."),
    NOT_EXIST_PRODUCT_CONFIRM_STATUS("[Command - Domain] 존재하지 않는 Confirm Status입니다.")
}
