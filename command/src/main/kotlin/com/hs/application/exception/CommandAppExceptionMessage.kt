package com.hs.application.exception

enum class CommandAppExceptionMessage(val localizedMessage: String) {
    NOT_FOUND_PRODUCT("[Command - Application] 해당 상품이 존재하지 않습니다. 상품 번호를 다시 확인해주세요!"),
    UNABLE_TO_PUBLISH_EVENT("[Command - Application] 생성된 상품의 ID가 Null입니다. Event를 발행할 수 없습니다."),
}
