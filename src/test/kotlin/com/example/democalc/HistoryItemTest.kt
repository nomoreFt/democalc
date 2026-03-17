package com.example.democalc

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDateTime

class HistoryItemTest {

    @Test
    fun `HistoryItem은 id, a, b, operator, result, createdAt 필드를 가진다`() {
        val now = LocalDateTime.now()
        val item = HistoryItem(
            id = 1L,
            a = 10.0,
            b = 5.0,
            operator = "+",
            result = 15.0,
            createdAt = now
        )

        assertThat(item.id).isEqualTo(1L)
        assertThat(item.a).isEqualTo(10.0)
        assertThat(item.b).isEqualTo(5.0)
        assertThat(item.operator).isEqualTo("+")
        assertThat(item.result).isEqualTo(15.0)
        assertThat(item.createdAt).isEqualTo(now)
    }
}
