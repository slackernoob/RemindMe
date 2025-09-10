package com.example.remindme.core.utils

import java.text.SimpleDateFormat

//fun formatDueDate(selectedDateMillis: Long?): String {
//    return selectedDateMillis?.let { millis ->
//        val formatted = java.text.SimpleDateFormat("yyyy-MM-dd").format(millis)
//        "(Press to Edit) Due Date: $formatted"
//    } ?: "(Press to Edit) No Due Date"
//}


fun formatDueDateToEditString(selectedDateMillis: Long?): String {
    return if (selectedDateMillis != null) {
        val formatted = SimpleDateFormat("yyyy-MM-dd").format(selectedDateMillis)
        "(Press to Edit) Due Date: $formatted"
    } else {
        "(Press to Edit) No Due Date"
    }
}

//fun formatDueDateReadable(selectedDateMillis: Long?): String? {
//    return selectedDateMillis?.let {
//        SimpleDateFormat("EEE, dd MMM yyyy").format(it)
//    }
//}

fun getFormattedDueDateText(selectedDateMillis: Long?): String {
    return if (selectedDateMillis != null) {
        val formatted = SimpleDateFormat("EEE, dd MMM yyyy").format(selectedDateMillis)
        "Due Date: $formatted"
    } else {
        "No Due Date Selected"
    }
}
//    return selectedDateMillis?.let {
//        val formatted = SimpleDateFormat("EEE, dd MMM yyyy").format(it)
//        "Due Date: $formatted"
//    } ?:



fun getDueDateWithNullCheck(dateDue: Long?): Long {
    return dateDue ?: -1L
}

fun getTimeDueWithDateDue(dateDue: Long): Long {
    if (dateDue == -1L) {
        return -1L
    } else {
        return dateDue - 3_600_000
    }
}