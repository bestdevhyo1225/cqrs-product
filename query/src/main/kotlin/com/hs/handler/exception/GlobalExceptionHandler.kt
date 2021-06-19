package com.hs.handler.exception

import com.hs.exception.DomainMongoException
import com.hs.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.RuntimeException
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [NoSuchElementException::class])
    fun handle(exception: NoSuchElementException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(status = HttpStatus.NOT_FOUND.reasonPhrase, message = exception.localizedMessage),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(
        value = [
            DomainMongoException::class,
            ConstraintViolationException::class
        ]
    )
    fun handle(exception: RuntimeException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(status = HttpStatus.BAD_REQUEST.reasonPhrase, message = exception.localizedMessage),
            HttpStatus.BAD_REQUEST
        )
    }

}

@ExceptionHandler(value = [MethodArgumentNotValidException::class])
fun handle(exception: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {

    return ResponseEntity(
        ErrorResponse(
            status = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = exception.bindingResult.allErrors[0].defaultMessage ?: ""
        ),
        HttpStatus.BAD_REQUEST
    )
}
