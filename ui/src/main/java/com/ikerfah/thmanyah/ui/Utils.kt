package com.ikerfah.thmanyah.ui

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun formatSecondsToHoursAndMinutes(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60

    return when {
        hours > 0 && minutes > 0 -> "$hours h $minutes min"
        hours > 0 -> "$hours h"
        else -> "$minutes min"
    }
}

fun formatRelativeDateTime(dateTime: LocalDateTime): String {
    val now = LocalDateTime.now()
    val daysBetween = ChronoUnit.DAYS.between(dateTime.toLocalDate(), now.toLocalDate()).toInt()

    return when (daysBetween) {
        0 -> {
            val hoursBetween = ChronoUnit.HOURS.between(dateTime, now).toInt()
            when (hoursBetween) {
                0 -> "Just now"
                1 -> "1 hour ago"
                else -> "$hoursBetween hours ago"
            }
        }

        1 -> "Yesterday"
        else -> "$daysBetween days ago"
    }
}