package com.example.democalc

import java.time.LocalDateTime

data class HistoryItem(
    val id: Long,
    val a: Double,
    val b: Double,
    val operator: String,
    val result: Double,
    val createdAt: LocalDateTime
)
