package com.example.rakshasetu.data.models


import com.google.firebase.Timestamp

data class Notification(
    val notificationId: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "", // visitor_request, visitor_approved, announcement, emergency, join_request
    val targetId: String = "",
    val isRead: Boolean = false,
    val createdAt: Timestamp? = null


)

//data class Emergency(
//    val emergencyId: String = "",
//    val senderId: String = "",
//    val senderName: String = "",
//    val message: String = "",
//    val societyId: String = "",
//    val status: String = "active", // active, resolved
//    val createdAt: Timestamp? = null
//)