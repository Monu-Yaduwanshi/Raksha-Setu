package com.example.rakshasetu.data.models

import com.google.firebase.Timestamp
//
//data class Visitor(
//    val visitorId: String = "",
//    val visitorName: String = "",
//    val visitorPhone: String = "",
//    val visitorPhotoUrl: String = "",
//    val flatNumber: String = "",
//    val residentId: String = "",
//    val watchmanId: String = "",
//    val watchmanName: String = "",
//    val purpose: String = "", // delivery, guest, cab, house help, maintenance
//    val organization: String = "",
//    val numberOfVisitors: Int = 1,
//    val vehicleNumber: String = "",
//    val entryTime: Timestamp? = null,
//    val exitTime: Timestamp? = null,
//    val status: String = "pending", // pending, approved, denied, leave_at_gate, completed
//    val notes: String = "",
//    val isPreApproved: Boolean = false,
//    val createdAt: Timestamp? = null
//)


data class Visitor(
    val visitorId: String = "",
    val visitorName: String = "",
    val visitorPhone: String = "",
    val visitorPhotoUrl: String = "",
    val flatNumber: String = "",
    val residentId: String = "",
    val watchmanId: String = "",
    val watchmanName: String = "",
    val purpose: String = "",
    val organization: String = "",
    val numberOfVisitors: Int = 1,
    val vehicleNumber: String = "",
    val entryTime: Timestamp? = null,
    val exitTime: Timestamp? = null,
    val status: String = "pending", // pending, approved, rejected, leave_at_gate
    val notes: String = "",
    val isPreApproved: Boolean = false,
    val createdAt: Timestamp? = null,

    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val imageUrl: String = "",


)




data class VisitorRequestItem(
    val id: String,
    val visitorName: String,
    val visitorPhone: String,
    val photoUrl: String,
    val flatNumber: String,
    val purpose: String,
    val organization: String,
    val numberOfVisitors: Int,
    val vehicleNumber: String,
    val entryTime: Timestamp?,
    val status: String,
    val watchmanName: String,
    val notes: String
)

data class PreApprovedGuestItem(
    val id: String,
    val guestName: String,
    val guestPhone: String,
    val flatNumber: String,
    val purpose: String,
    val schedule: String,
    val validFrom: Timestamp?,
    val validUntil: Timestamp?,
    val isActive: Boolean
)

data class HouseholdMemberItem(
    val id: String,
    val name: String,
    val relation: String,
    val phone: String,
    val age: Int
)