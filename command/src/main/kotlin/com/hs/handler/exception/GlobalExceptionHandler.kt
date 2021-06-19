package com.hs.handler.exception

import com.hs.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [NoSuchElementException::class])
    fun handle(exception: NoSuchElementException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(status = HttpStatus.NOT_FOUND.reasonPhrase, message = exception.localizedMessage),
            HttpStatus.NOT_FOUND
        )
    }
}
