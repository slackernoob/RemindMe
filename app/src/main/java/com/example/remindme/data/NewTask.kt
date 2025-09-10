package com.example.remindme.data

data class NewTask (
    val name: String,
    val description: String? = null,
    val dateDue: Long,
    val timeDue: Long? = null,
)