package com.example.rakshasetu.data.models


import com.google.firebase.Timestamp
//
//data class Announcement(
//    val announcementId: String = "",
//    val title: String = "",
//    val message: String = "",
//    val priority: String = "normal", // normal, important, emergency
//    val createdBy: String = "",
//    val createdByName: String = "",
//    val societyId: String = "",
//    val createdAt: Timestamp? = null,
//    val expiryDate: Timestamp? = null,
//    val readBy: List<String> = emptyList()
//)


data class Announcement(
    val announcementId: String = "",
    val title: String = "",
    val message: String = "",
    val priority: String = "normal",
    val audience: String = "all",
    val createdBy: String = "",
    val createdByName: String = "",
    val societyId: String = "",
    val createdAt: Timestamp? = null,
    val expiryDate: Timestamp? = null,
    val readBy: List<String> = emptyList()
)