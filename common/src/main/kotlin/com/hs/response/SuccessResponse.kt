package com.hs.response

data class SuccessResponse<T>(
    val status: String = "OK",
    val data: T
)
