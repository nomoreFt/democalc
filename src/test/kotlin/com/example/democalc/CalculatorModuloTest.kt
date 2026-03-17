package com.example.democalc

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

class CalculatorModuloTest {

    private lateinit var historyService: CalculatorHistoryService
    private lateinit var calculatorService: CalculatorService

    @BeforeEach
    fun setup() {
        historyService = CalculatorHistoryService()
        calculatorService = CalculatorService(historyService)
    }

    // T1: 기본 동작
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CalculatorModuloApiTest {

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    lateinit var historyService: CalculatorHistoryService

    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        historyService.clearAll()
    }

    // T4: API 통합 — % by zero (기본 성공 케이스는 CalculatorControllerTest에 존재)
    @Test
    fun `나머지 연산에서 0으로 나누기 시 400 에러를 반환한다`() {
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 10, "b": 0, "operator": "%"}"""
        }.andExpect {
            status { isBadRequest() }
        }
    }

    // 히스토리 필터: % 연산자 지원 확인
    @Test
    fun `나머지 연산 후 히스토리 필터링으로 퍼센트 항목만 조회된다`() {
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 10, "b": 3, "operator": "%"}"""
        }.andExpect { status { isOk() } }
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 10, "b": 2, "operator": "+"}"""
        }.andExpect { status { isOk() } }

        mockMvc.get("/api/history") { param("operator", "%") }.andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(1) }
            jsonPath("$[0].operator") { value("%") }
        }
    }
}
