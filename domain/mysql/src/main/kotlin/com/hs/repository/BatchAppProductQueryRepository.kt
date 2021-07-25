package com.hs.repository

import java.time.LocalDateTime

interface BatchAppProductQueryRepository {
    fun findMinId(startDatetime: LocalDateTime, endDatetime: LocalDateTime): Long?
    fun findMaxId(startDatetime: LocalDateTime, endDatetime: LocalDateTime): Long?
}
