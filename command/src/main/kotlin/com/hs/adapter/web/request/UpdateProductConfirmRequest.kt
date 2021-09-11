package com.hs.adapter.web.request

import javax.validation.constraints.NotBlank

data class UpdateProductConfirmRequest(
    @field:NotBlank(message = "confirmStatus는 비어 있을 수 없습니다.")
    var confirmStatus: String,
)
