package com.example.democalc

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CalculatorExceptionHandler {

    @ExceptionHandler(ArithmeticException::class, IllegalArgumentException::class)
    fun handleBadRequestException(e: RuntimeException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "잘못된 요청")))
    }
}
