package com.hs.web.request

import javax.validation.constraints.NotBlank

data class UpdateProductConfirmRequest(
    @get:NotBlank(message = "comfirmStatus는 비어 있을 수 없습니다.")
    var comfirmStatus: String,
)
