package com.example.rakshasetu.data.models


import com.google.firebase.Timestamp


data class PreApprovedGuest(
    val guestId: String = "",
    val guestName: String = "",
    val guestPhone: String = "",
    val flatNumber: String = "",
    val residentId: String = "",
    val purpose: String = "",
    val schedule: String = "",
    val validFrom: Timestamp? = null,
    val validUntil: Timestamp? = null,
    val isActive: Boolean = true,
    val createdAt: Timestamp? = null
)

data class HouseholdMember(
    val memberId: String = "",
    val name: String = "",
    val relation: String = "",
    val phone: String = "",
    val age: Int = 0,
    val residentId: String = "",
    val flatNumber: String = "",
    val isActive: Boolean = true,
    val createdAt: Timestamp? = null
)
