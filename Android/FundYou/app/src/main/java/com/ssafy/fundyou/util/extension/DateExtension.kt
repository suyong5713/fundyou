package com.ssafy.fundyou.util.extension

import java.util.Calendar
import java.util.Locale

fun getDeadLineByEndDate(endDate: Long): Int {
    val currentDay = System.currentTimeMillis()
    val deadLine = (endDate - currentDay).toDouble() / (1000 * 60 * 60 * 24)
    return if (deadLine < 0) -1 else deadLine.toInt()
}