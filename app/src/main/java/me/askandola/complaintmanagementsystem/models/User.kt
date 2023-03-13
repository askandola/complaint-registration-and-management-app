package me.askandola.complaintmanagementsystem.models

data class User(
    val uid: String = "",
    val name: String? = "",
    val email: String? = "",
    val admin: String = ""
)
