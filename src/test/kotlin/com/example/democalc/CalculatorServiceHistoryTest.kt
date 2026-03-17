package com.example.democalc

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy

class CalculatorServiceHistoryTest {

    private lateinit var historyService: CalculatorHistoryService
    private lateinit var calculatorService: CalculatorService

    @BeforeEach
    fun setup() {
        historyService = CalculatorHistoryService()
        calculatorService = CalculatorService(historyService)
    }

    @Test
    fun `성공적인 계산 후 히스토리에 저장된다`() {
        calculatorService.calculate(CalculatorRequest(a = 10.0, b = 5.0, operator = "+"))

        val history = historyService.getHistory()
        assertThat(history).hasSize(1)
        assertThat(history[0].a).isEqualTo(10.0)
        assertThat(history[0].b).isEqualTo(5.0)
        assertThat(history[0].operator).isEqualTo("+")
        assertThat(history[0].result).isEqualTo(15.0)
    }

    @Test
    fun `0으로 나누기 예외 발생 시 히스토리에 저장되지 않는다`() {
        assertThatThrownBy {
            calculatorService.calculate(CalculatorRequest(a = 10.0, b = 0.0, operator = "/"))
        }.isInstanceOf(ArithmeticException::class.java)

        val history = historyService.getHistory()
        assertThat(history).isEmpty()
    }

    @Test
    fun `잘못된 연산자 예외 발생 시 히스토리에 저장되지 않는다`() {
        assertThatThrownBy {
            calculatorService.calculate(CalculatorRequest(a = 10.0, b = 5.0, operator = "@"))
        }.isInstanceOf(IllegalArgumentException::class.java)

        val history = historyService.getHistory()
        assertThat(history).isEmpty()
    }
}
