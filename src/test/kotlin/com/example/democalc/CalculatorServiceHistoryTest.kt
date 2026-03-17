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

    // T1: 나머지 연산 기본 동작
    @Test
    fun `나머지 연산 10 mod 3은 1_0이다`() {
        val response = calculatorService.calculate(CalculatorRequest(a = 10.0, b = 3.0, operator = "%"))
        assertThat(response.result).isEqualTo(1.0)
    }

    @Test
    fun `나머지 연산 부동소수점 10_5 mod 3_0은 1_5이다`() {
        val response = calculatorService.calculate(CalculatorRequest(a = 10.5, b = 3.0, operator = "%"))
        assertThat(response.result).isEqualTo(1.5)
    }

    @Test
    fun `나머지 연산 음수 -7 mod 3은 -1_0이다`() {
        val response = calculatorService.calculate(CalculatorRequest(a = -7.0, b = 3.0, operator = "%"))
        assertThat(response.result).isEqualTo(-1.0)
    }

    // T2: 0으로 나누기 예외
    @Test
    fun `나머지 연산에서 0으로 나누면 ArithmeticException이 발생한다`() {
        assertThatThrownBy {
            calculatorService.calculate(CalculatorRequest(a = 10.0, b = 0.0, operator = "%"))
        }.isInstanceOf(ArithmeticException::class.java)
            .hasMessage("0으로 나눌 수 없습니다")
    }

    // T3: 히스토리 저장
    @Test
    fun `나머지 연산 성공 후 히스토리에 저장된다`() {
        calculatorService.calculate(CalculatorRequest(a = 10.0, b = 3.0, operator = "%"))

        val history = historyService.getHistory()
        assertThat(history).hasSize(1)
        assertThat(history[0].operator).isEqualTo("%")
        assertThat(history[0].result).isEqualTo(1.0)
    }

    @Test
    fun `나머지 연산 0으로 나누기 예외 시 히스토리에 저장되지 않는다`() {
        assertThatThrownBy {
            calculatorService.calculate(CalculatorRequest(a = 10.0, b = 0.0, operator = "%"))
        }.isInstanceOf(ArithmeticException::class.java)

        val history = historyService.getHistory()
        assertThat(history).isEmpty()
    }
}
