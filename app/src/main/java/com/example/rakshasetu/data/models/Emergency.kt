

package com.example.rakshasetu.data.models
import com.google.firebase.Timestamp

//data class Emergency(
//    val emergencyId: String = "",
//    val senderId: String = "",
//    val senderName: String = "",
//    val senderFlat: String = "",
//    val message: String = "",
//    val location: String = "",
//    val latitude: Double = 0.0,
//    val longitude: Double = 0.0,
//    val status: String = "active", // active, resolved
//    val acknowledgedBy: List<String> = emptyList(),
//    val createdAt: Timestamp? = null,
//    val resolvedAt: Timestamp? = null
//)
data class Emergency(
    val emergencyId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderFlat: String = "",
    val message: String = "",
    val societyId: String = "",
    val status: String = "active",
    val createdAt: Timestamp? = null
)