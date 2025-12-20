package com.shinythinking.broadcastautomation.presentation.base.component

fun formatDateToKorean(dateString: String): String {
    if (!dateString.matches(Regex("""\d{4}-\d{2}-\d{2}"""))) {
        return dateString
    }
    val parts = dateString.split("-")
    val month = parts[1].toInt()
    val day = parts[2].toInt()
    return "${month}월 ${day}일"
}

fun formatTimeToKorean(timeString: String): String {
    if (!timeString.matches(Regex("""\d{2}:\d{2}"""))) {
        return timeString
    }
    val parts = timeString.split(":")
    val hour = parts[0].toInt()
    val minute = parts[1].toInt()

    if (minute == 0) {
        return "${hour}시"
    }
    return "${hour}시 ${minute}분"
}
