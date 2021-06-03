package com.hs.web.response

data class SuccessResponse(
    val status: String = "success",
    val data: Any
)
