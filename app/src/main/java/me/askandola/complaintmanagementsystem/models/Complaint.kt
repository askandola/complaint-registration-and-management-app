package me.askandola.complaintmanagementsystem.models

import java.sql.Date
import java.sql.Timestamp

data class Complaint(
    val title: String = "",
    val desc: String = "",
    val date: String = "",
    val verdict: String = "",
    val status: String = "",
    val user: User = User()
)
