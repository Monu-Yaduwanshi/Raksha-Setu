package com.example.rakshasetu.core.utils


import java.text.SimpleDateFormat
import java.util.*

fun getTimeAgo(date: Date): String {
    val now = Date()
    val diffInSeconds = (now.time - date.time) / 1000

    return when {
        diffInSeconds < 60 -> "Just now"
        diffInSeconds < 3600 -> "${diffInSeconds / 60} min ago"
        diffInSeconds < 86400 -> "${diffInSeconds / 3600} hours ago"
        else -> "${diffInSeconds / 86400} days ago"
    }
}

fun isToday(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_YEAR)
    val todayYear = calendar.get(Calendar.YEAR)

    calendar.time = date
    return calendar.get(Calendar.DAY_OF_YEAR) == today &&
            calendar.get(Calendar.YEAR) == todayYear
}

fun isThisWeek(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.time = date
    return calendar.get(Calendar.WEEK_OF_YEAR) == currentWeek &&
            calendar.get(Calendar.YEAR) == currentYear
}

fun isThisMonth(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.time = date
    return calendar.get(Calendar.MONTH) == currentMonth &&
            calendar.get(Calendar.YEAR) == currentYear
}

fun isDateMatch(date: Date, dateStr: String): Boolean {
    val parts = dateStr.split("/")
    if (parts.size != 3) return false

    val day = parts[0].toIntOrNull() ?: return false
    val month = parts[1].toIntOrNull() ?: return false
    val year = parts[2].toIntOrNull() ?: return false

    val calendar = Calendar.getInstance()
    calendar.time = date

    return calendar.get(Calendar.DAY_OF_MONTH) == day &&
            calendar.get(Calendar.MONTH) + 1 == month &&
            calendar.get(Calendar.YEAR) == year
}

fun String.capitalize(): String {
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}