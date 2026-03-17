package com.example.democalc

import org.hamcrest.Matchers.isA
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CalculatorControllerTest {

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

    @Test
    fun `덧셈 연산 POST 요청 시 올바른 결과를 반환한다`() {
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 10, "b": 5, "operator": "+"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.result") { value(15.0) }
        }
    }

    @Test
    fun `뺄셈 연산 POST 요청 시 올바른 결과를 반환한다`() {
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 10, "b": 5, "operator": "-"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.result") { value(5.0) }
        }
    }

    @Test
    fun `곱셈 연산 POST 요청 시 올바른 결과를 반환한다`() {
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 10, "b": 5, "operator": "*"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.result") { value(50.0) }
        }
    }

    @Test
    fun `나눗셈 연산 POST 요청 시 올바른 결과를 반환한다`() {
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 10, "b": 5, "operator": "/"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.result") { value(2.0) }
        }
    }

    @Test
    fun `0으로 나누기 시 400 에러를 반환한다`() {
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 10, "b": 0, "operator": "/"}"""
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `나머지 연산 POST 요청 시 올바른 결과를 반환한다`() {
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 10, "b": 3, "operator": "%"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.result") { value(1.0) }
        }
    }

    @Test
    fun `잘못된 연산자 입력 시 400 에러를 반환한다`() {
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 10, "b": 5, "operator": "@"}"""
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `GET api history 요청 시 히스토리가 없으면 빈 배열과 200 OK를 반환한다`() {
        mockMvc.get("/api/history").andExpect {
            status { isOk() }
            jsonPath("$") { isArray() }
            jsonPath("$.length()") { value(0) }
        }
    }

    @Test
    fun `GET api history 요청 시 여러 계산 후 최신순으로 정렬된 목록을 반환한다`() {
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 1, "b": 1, "operator": "+"}"""
        }.andExpect { status { isOk() } }
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 2, "b": 2, "operator": "*"}"""
        }.andExpect { status { isOk() } }
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 3, "b": 3, "operator": "-"}"""
        }.andExpect { status { isOk() } }

        mockMvc.get("/api/history").andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(3) }
            jsonPath("$[0].operator") { value("-") }
            jsonPath("$[1].operator") { value("*") }
            jsonPath("$[2].operator") { value("+") }
        }
    }

    @Test
    fun `GET api history 요청 시 저장된 히스토리 항목의 필드가 올바르게 반환된다`() {
        mockMvc.post("/api/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"a": 10, "b": 5, "operator": "+"}"""
        }.andExpect { status { isOk() } }

        mockMvc.get("/api/history").andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(1) }
            jsonPath("$[0].a") { value(10.0) }
            jsonPath("$[0].b") { value(5.0) }
            jsonPath("$[0].operator") { value("+") }
            jsonPath("$[0].result") { value(15.0) }
            jsonPath("$[0].id") { value(isA(Number::class.java)) }
            jsonPath("$[0].createdAt") { value(isA(String::class.java)) }
        }
    }
}
