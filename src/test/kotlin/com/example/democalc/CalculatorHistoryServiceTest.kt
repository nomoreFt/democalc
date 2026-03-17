package com.example.democalc

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class CalculatorHistoryServiceTest {

    private lateinit var historyService: CalculatorHistoryService

    @BeforeEach
    fun setup() {
        historyService = CalculatorHistoryService()
    }

    @Test
    fun `히스토리 저장 후 조회하면 저장된 항목이 반환된다`() {
        historyService.save(a = 10.0, b = 5.0, operator = "+", result = 15.0)

        val history = historyService.getHistory()

        assertThat(history).hasSize(1)
        assertThat(history[0].a).isEqualTo(10.0)
        assertThat(history[0].b).isEqualTo(5.0)
        assertThat(history[0].operator).isEqualTo("+")
        assertThat(history[0].result).isEqualTo(15.0)
    }

    @Test
    fun `히스토리는 최신순으로 반환된다`() {
        historyService.save(a = 1.0, b = 1.0, operator = "+", result = 2.0)
        historyService.save(a = 2.0, b = 2.0, operator = "*", result = 4.0)
        historyService.save(a = 3.0, b = 3.0, operator = "-", result = 0.0)

        val history = historyService.getHistory()

        assertThat(history[0].operator).isEqualTo("-")
        assertThat(history[1].operator).isEqualTo("*")
        assertThat(history[2].operator).isEqualTo("+")
    }

    @Test
    fun `히스토리 삭제 후 조회하면 빈 목록이 반환된다`() {
        historyService.save(a = 10.0, b = 5.0, operator = "+", result = 15.0)
        historyService.clearAll()

        val history = historyService.getHistory()

        assertThat(history).isEmpty()
    }

    @Test
    fun `id는 저장 순서대로 자동 증가한다`() {
        historyService.save(a = 1.0, b = 1.0, operator = "+", result = 2.0)
        historyService.save(a = 2.0, b = 2.0, operator = "+", result = 4.0)

        val history = historyService.getHistory()

        // 최신순이므로 첫 번째가 id=2, 두 번째가 id=1
        assertThat(history[0].id).isEqualTo(2L)
        assertThat(history[1].id).isEqualTo(1L)
    }

    @Test
    fun `최대 100건을 초과하면 오래된 항목이 제거된다`() {
        repeat(101) { i ->
            historyService.save(a = i.toDouble(), b = 1.0, operator = "+", result = (i + 1).toDouble())
        }

        val history = historyService.getHistory()

        assertThat(history).hasSize(100)
        // 가장 최신 항목(a=100.0)이 첫 번째여야 한다
        assertThat(history[0].a).isEqualTo(100.0)
        // 가장 오래된 항목(a=0.0)은 제거되었어야 한다
        assertThat(history.last().a).isEqualTo(1.0)
    }
}
