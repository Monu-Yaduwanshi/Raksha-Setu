package com.example.rakshasetu.data.models

// Remove @Parcelize and Parcelable implementation
data class User(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "", // "secretary", "watchman", "resident"
    val societyId: String = "",
    val societyName: String = "",
    val flatNumber: String = "",
    val blockNumber: String = "",
    val profileImageUrl: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLogin: Long = 0,
    val fcmToken: String = "",
    val createdBy: String = "" // Secretary who created this account
)