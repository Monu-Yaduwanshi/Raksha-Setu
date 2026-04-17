package com.example.rakshasetu.data.models

import com.google.firebase.Timestamp

//data class Society(
//    val societyId: String = "",
//    val societyName: String = "",
//    val address: String = "",
//    val city: String = "",
//    val pincode: String = "",
//    val totalFlats: Int = 0,
//    val totalBlocks: Int = 0,
//    val blocks: List<String> = emptyList(),
//    val createdBy: String = "",
//    val createdAt: Timestamp? = null,
//    val emergencyContacts: EmergencyContacts = EmergencyContacts()
//)

data class EmergencyContacts(
    val police: String = "",
    val ambulance: String = "",
    val fire: String = "",
    val security: String = ""
)

//data class Flat(
//    val flatId: String = "",
//    val flatNumber: String = "",
//    val blockNumber: String = "",
//    val societyId: String = "",
//    val residentId: String = "",
//    val residentName: String = "",
//    val residentPhone: String = "",
//    val isOccupied: Boolean = false,
//    val createdAt: Timestamp? = null
//)

//data class JoinRequest(
//    val requestId: String = "",
//    val userId: String = "",
//    val userName: String = "",
//    val userEmail: String = "",
//    val userPhone: String = "",
//    val userRole: String = "",
//    val societyId: String = "",
//    val societyName: String = "",
//    val flatNumber: String = "",
//    val blockNumber: String = "",
//    val shift: String = "",
//    val status: String = "pending",
//    val requestedAt: Timestamp? = null,
//    val respondedAt: Timestamp? = null,
//    val respondedBy: String = ""
//)


data class Society(
    val societyId: String = "",
    val societyName: String = "",
    val address: String = "",
    val city: String = "",
    val pincode: String = "",
    val totalFlats: Int = 0,
    val totalBlocks: Int = 0,
    val blocks: List<String> = emptyList(),
    val createdBy: String = "",
    val createdAt: Timestamp? = null   // ✅ FIXED
)

//data class Society(
//    val societyId: String = "",
//    val societyName: String = "",
//    val address: String = "",
//    val city: String = "",
//    val pincode: String = "",
//    val totalFlats: Int = 0,
//    val totalBlocks: Int = 0,
//    val blocks: List<String> = emptyList(),
//    val createdBy: String = "",
//    val createdAt: Timestamp? = null
//)

data class Flat(
    val flatId: String = "",
    val flatNumber: String = "",
    val blockNumber: String = "",
    val societyId: String = "",
    val residentId: String = "",
    val residentName: String = "",
    val residentPhone: String = "",
    //val isOccupied: Boolean = false,
    val occupied: Boolean = false,
    val createdAt: Timestamp? = null
)

data class JoinRequest(
    val requestId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val userPhone: String = "",
    val userRole: String = "",
    val societyId: String = "",
    val societyName: String = "",
    val flatNumber: String = "",
    val blockNumber: String = "",
    val shift: String = "",
    val status: String = "pending",
    val requestedAt: Timestamp? = null,
    val respondedAt: Timestamp? = null,
    val respondedBy: String = ""
)