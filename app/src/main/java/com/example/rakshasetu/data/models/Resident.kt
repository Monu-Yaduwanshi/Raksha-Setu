package com.example.rakshasetu.data.models



data class Resident(
    val documentId: String = "",
    val fullName: String = "",
    val flatNumber: String = "",
    val phone: String = "",
    val email: String = "",
    val role: String = "resident",
    val societyId: String = ""
)
