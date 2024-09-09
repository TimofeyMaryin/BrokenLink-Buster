package com.broken.link.buster.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BrokenLinkModel(
    val link: String,
    val statusLink: Int = getBrokenLinkStatusToInt(BrokenLinkStatus.WHITE), // 0 - серая; 1 - белая
    val statusAppInLink: Int = getBrokenLinkStatusAppInLink(BrokenLinkStatusAppInLink.REVIEW),
    val timeOfCreation: String = dateToString(),
    var timeCrashes: String = dateToString(),
    var isActive: Boolean = true,
)


enum class BrokenLinkStatus { WHITE, GRAY }
enum class BrokenLinkStatusAppInLink { REVIEW, RELEASE, BAN }

fun getBrokenLinkStatusAppInLink(status: BrokenLinkStatusAppInLink): Int {
    return when (status) {
        BrokenLinkStatusAppInLink.REVIEW -> 0
        BrokenLinkStatusAppInLink.RELEASE -> 1
        BrokenLinkStatusAppInLink.BAN -> -1
    }
}

fun getBrokenLinkStatusToInt(status: BrokenLinkStatus): Int {
    return when(status) {
        BrokenLinkStatus.WHITE -> 1
        BrokenLinkStatus.GRAY -> 0
    }
}


// Функция для преобразования текущей даты в строку
fun dateToString(): String {
    val currentDate = Date()
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return formatter.format(currentDate)
}

fun dateToString(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return formatter.format(date)
}



// Функция для преобразования строки в дату
fun stringToDate(dateString: String): Date {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return formatter.parse(dateString)
}