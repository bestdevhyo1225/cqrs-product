package com.hs.web.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class CreateProductRequest(
    @get:NotBlank(message = "name은 비어 있을 수 없습니다.")
    var name: String,

    @get:Positive(message = "price는 0 또는 음수가 될 수 없습니다.")
    var price: Int,

    @get:PositiveOrZero(message = "음수가 될 수 없습니다.")
    var stockCount: Int,

    var imageUrls: List<String>
)
