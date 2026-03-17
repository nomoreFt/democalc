package com.example.democalc

import org.springframework.stereotype.Service

@Service
class CalculatorService(
    private val historyService: CalculatorHistoryService
) {

    fun calculate(request: CalculatorRequest): CalculatorResponse {
        val result = computeResult(request)
        historyService.save(a = request.a, b = request.b, operator = request.operator, result = result)
        return CalculatorResponse(result)
    }

    private fun computeResult(request: CalculatorRequest): Double = when (request.operator) {
        "+" -> request.a + request.b
        "-" -> request.a - request.b
        "*" -> request.a * request.b
        "/" -> {
            requireNonZeroDivisor(request.b)
            request.a / request.b
        }
        "%" -> {
            requireNonZeroDivisor(request.b)
            request.a % request.b
        }
        else -> throw IllegalArgumentException("지원하지 않는 연산자입니다: ${request.operator}")
    }

    private fun requireNonZeroDivisor(b: Double) {
        if (b == 0.0) throw ArithmeticException("0으로 나눌 수 없습니다")
    }
}
