package com.example.rakshasetu.ui.resident


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rakshasetu.data.models.HouseholdMemberItem
import com.example.rakshasetu.data.models.PreApprovedGuestItem
import com.example.rakshasetu.data.models.Visitor
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class ResidentViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // State flows
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _userName = MutableStateFlow("Resident")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _flatNumber = MutableStateFlow("")
    val flatNumber: StateFlow<String> = _flatNumber.asStateFlow()

    private val _societyId = MutableStateFlow("")
    val societyId: StateFlow<String> = _societyId.asStateFlow()

    private val _pendingVisitors = MutableStateFlow<List<Visitor>>(emptyList())
    val pendingVisitors: StateFlow<List<Visitor>> = _pendingVisitors.asStateFlow()

    private val _visitorHistory = MutableStateFlow<List<Visitor>>(emptyList())
    val visitorHistory: StateFlow<List<Visitor>> = _visitorHistory.asStateFlow()

    // ✅ ADDED: Pre-approved guests state
    private val _preApprovedGuests = MutableStateFlow<List<PreApprovedGuestItem>>(emptyList())
    val preApprovedGuests: StateFlow<List<PreApprovedGuestItem>> = _preApprovedGuests.asStateFlow()

    // ✅ ADDED: Household members state
    private val _householdMembers = MutableStateFlow<List<HouseholdMemberItem>>(emptyList())
    val householdMembers: StateFlow<List<HouseholdMemberItem>> = _householdMembers.asStateFlow()

    companion object {
        private const val TAG = "ResidentViewModel"
    }

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser ?: return@launch
                Log.d(TAG, "Loading user data for UID: ${currentUser.uid}")

                val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
                if (userDoc.exists()) {
                    _userName.value = userDoc.getString("fullName") ?: "Resident"
                    _flatNumber.value = userDoc.getString("flatNumber") ?: ""
                    _societyId.value = userDoc.getString("societyId") ?: ""

                    val residentId = currentUser.uid
                    Log.d(TAG, "Resident loaded - ID: $residentId, Flat: ${_flatNumber.value}")

                    loadPendingVisitors(residentId)
                    loadVisitorHistory(residentId)
                    loadPreApprovedGuests()
                    loadHouseholdMembers()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading user data", e)
            }
        }
    }
    // Add this to ResidentViewModel.kt temporarily for debugging
    fun debugCheckFirestoreData() {
        val currentUserId = auth.currentUser?.uid ?: return

        Log.d(TAG, "========== DEBUG FIRESTORE DATA ==========")
        Log.d(TAG, "Current User UID: $currentUserId")

        // Check all visitors without filter
        firestore.collection("visitors")
            .get()
            .addOnSuccessListener { snapshot ->
                Log.d(TAG, "Total visitors in collection: ${snapshot.size()}")
                for (doc in snapshot.documents) {
                    val residentId = doc.getString("residentId")
                    val visitorName = doc.getString("visitorName")
                    val status = doc.getString("status")
                    Log.d(TAG, "  Visitor: $visitorName, residentId: $residentId, status: $status")
                    Log.d(TAG, "  Match: ${residentId == currentUserId}")
                }
            }
    }
    fun loadPendingVisitors(residentId: String) {
        Log.d(TAG, "loadPendingVisitors called for residentId: $residentId")

        firestore.collection("visitors")
            .whereEqualTo("residentId", residentId)
            .whereEqualTo("status", "pending")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error loading pending visitors", error)
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null

                    val entryTimeValue = data["entryTime"]
                    val entryTimestamp = when (entryTimeValue) {
                        is Timestamp -> entryTimeValue
                        is Long -> Timestamp(Date(entryTimeValue))
                        else -> null
                    }

                    // ✅ IMPORTANT: Map visitorPhotoUrl correctly
                    val photoUrl = data["visitorPhotoUrl"] as? String ?: ""
                    Log.d(TAG, "Loading visitor: ${data["visitorName"]}, Photo URL: ${photoUrl.take(50)}")

                    Visitor(
                        id = doc.id,
                        name = data["visitorName"] as? String ?: "",
                        phone = data["visitorPhone"] as? String ?: "",
                        flatNumber = data["flatNumber"] as? String ?: "",
                        purpose = data["purpose"] as? String ?: "",
                        imageUrl = photoUrl,
                        visitorPhotoUrl = photoUrl,  // ✅ Set both fields
                        status = data["status"] as? String ?: "pending",
                        entryTime = entryTimestamp,
                        residentId = data["residentId"] as? String ?: "",
                        organization = data["organization"] as? String ?: "",
                        numberOfVisitors = (data["numberOfVisitors"] as? Long)?.toInt() ?: 1,
                        vehicleNumber = data["vehicleNumber"] as? String ?: "",
                        watchmanName = data["watchmanName"] as? String ?: "",
                        notes = data["notes"] as? String ?: ""
                    )
                } ?: emptyList()

                Log.d(TAG, "Pending visitors: ${list.size}")
                _pendingVisitors.value = list
            }
    }
    /**
     * Load pending visitors with real-time listener
     */
//    fun loadPendingVisitors(residentId: String) {
//        Log.d(TAG, "loadPendingVisitors called for residentId: $residentId")
//
//        firestore.collection("visitors")
//            .whereEqualTo("residentId", residentId)
//            .whereEqualTo("status", "pending")
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    Log.e(TAG, "Error loading pending visitors", error)
//                    return@addSnapshotListener
//                }
//
//                val list = snapshot?.documents?.mapNotNull { doc ->
//                    val data = doc.data ?: return@mapNotNull null
//
//                    val entryTimeValue = data["entryTime"]
//                    val entryTimestamp = when (entryTimeValue) {
//                        is Timestamp -> entryTimeValue
//                        is Long -> Timestamp(Date(entryTimeValue))
//                        else -> null
//                    }
//
//                    Visitor(
//                        id = doc.id,
//                        name = data["visitorName"] as? String ?: "",
//                        phone = data["visitorPhone"] as? String ?: "",
//                        flatNumber = data["flatNumber"] as? String ?: "",
//                        purpose = data["purpose"] as? String ?: "",
//                        imageUrl = data["visitorPhotoUrl"] as? String ?: "",
//                        status = data["status"] as? String ?: "pending",
//                        entryTime = entryTimestamp,
//                        residentId = data["residentId"] as? String ?: "",
//                        organization = data["organization"] as? String ?: "",
//                        numberOfVisitors = (data["numberOfVisitors"] as? Long)?.toInt() ?: 1,
//                        vehicleNumber = data["vehicleNumber"] as? String ?: "",
//                        watchmanName = data["watchmanName"] as? String ?: "",
//                        notes = data["notes"] as? String ?: ""
//                    )
//                } ?: emptyList()
//
//                Log.d(TAG, "Pending visitors: ${list.size}")
//                _pendingVisitors.value = list
//            }
//    }

    /**
     * Load visitor history with real-time listener
     */
//    fun loadVisitorHistory(residentId: String) {
//        firestore.collection("visitors")
//            .whereEqualTo("residentId", residentId)
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    Log.e(TAG, "Error loading visitor history", error)
//                    return@addSnapshotListener
//                }
//
//                val list = snapshot?.documents?.mapNotNull { doc ->
//                    val data = doc.data ?: return@mapNotNull null
//
//                    val entryTimeValue = data["entryTime"]
//                    val entryTimestamp = when (entryTimeValue) {
//                        is Timestamp -> entryTimeValue
//                        is Long -> Timestamp(Date(entryTimeValue))
//                        else -> null
//                    }
//
//                    Visitor(
//                        id = doc.id,
//                        name = data["visitorName"] as? String ?: "",
//                        phone = data["visitorPhone"] as? String ?: "",
//                        flatNumber = data["flatNumber"] as? String ?: "",
//                        purpose = data["purpose"] as? String ?: "",
//                        imageUrl = data["visitorPhotoUrl"] as? String ?: "",
//                        status = data["status"] as? String ?: "pending",
//                        entryTime = entryTimestamp,
//                        residentId = data["residentId"] as? String ?: "",
//                        organization = data["organization"] as? String ?: "",
//                        numberOfVisitors = (data["numberOfVisitors"] as? Long)?.toInt() ?: 1,
//                        vehicleNumber = data["vehicleNumber"] as? String ?: "",
//                        watchmanName = data["watchmanName"] as? String ?: "",
//                        notes = data["notes"] as? String ?: ""
//                    )
//                } ?: emptyList()
//
//                _visitorHistory.value = list
//            }
//    }
// Also update loadVisitorHistory similarly:
    fun loadVisitorHistory(residentId: String) {
        firestore.collection("visitors")
            .whereEqualTo("residentId", residentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error loading visitor history", error)
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null

                    val entryTimeValue = data["entryTime"]
                    val entryTimestamp = when (entryTimeValue) {
                        is Timestamp -> entryTimeValue
                        is Long -> Timestamp(Date(entryTimeValue))
                        else -> null
                    }

                    val photoUrl = data["visitorPhotoUrl"] as? String ?: ""

                    Visitor(
                        id = doc.id,
                        name = data["visitorName"] as? String ?: "",
                        phone = data["visitorPhone"] as? String ?: "",
                        flatNumber = data["flatNumber"] as? String ?: "",
                        purpose = data["purpose"] as? String ?: "",
                        imageUrl = photoUrl,
                        visitorPhotoUrl = photoUrl,
                        status = data["status"] as? String ?: "pending",
                        entryTime = entryTimestamp,
                        residentId = data["residentId"] as? String ?: "",
                        organization = data["organization"] as? String ?: "",
                        numberOfVisitors = (data["numberOfVisitors"] as? Long)?.toInt() ?: 1,
                        vehicleNumber = data["vehicleNumber"] as? String ?: "",
                        watchmanName = data["watchmanName"] as? String ?: "",
                        notes = data["notes"] as? String ?: ""
                    )
                } ?: emptyList()

                _visitorHistory.value = list
            }
    }
    /**
     * Get single visitor by ID
     */
//    suspend fun getVisitorById(visitorId: String): Visitor? {
//        return try {
//            val doc = firestore.collection("visitors").document(visitorId).get().await()
//            val data = doc.data ?: return null
//
//            Visitor(
//                id = doc.id,
//                name = data["visitorName"] as? String ?: "",
//                phone = data["visitorPhone"] as? String ?: "",
//                flatNumber = data["flatNumber"] as? String ?: "",
//                purpose = data["purpose"] as? String ?: "",
//                imageUrl = data["visitorPhotoUrl"] as? String ?: "",
//                visitorPhotoUrl = data["visitorPhotoUrl"] as? String ?: "",  // ✅ Map this field
//                status = data["status"] as? String ?: "pending",
//                entryTime = (data["entryTime"] as? Timestamp) ?: (data["entryTime"] as? Long)?.let { Timestamp(Date(it)) },
//                residentId = data["residentId"] as? String ?: "",
//                organization = data["organization"] as? String ?: "",
//                numberOfVisitors = (data["numberOfVisitors"] as? Long)?.toInt() ?: 1,
//                vehicleNumber = data["vehicleNumber"] as? String ?: "",
//                watchmanName = data["watchmanName"] as? String ?: "",
//                notes = data["notes"] as? String ?: ""
//            )
//        } catch (e: Exception) {
//            Log.e(TAG, "Error getting visitor by ID", e)
//            null
//        }
//    }
    suspend fun getVisitorById(visitorId: String): Visitor? {
        return try {
            val doc = firestore.collection("visitors").document(visitorId).get().await()
            val data = doc.data ?: return null

            val entryTimeValue = data["entryTime"]
            val entryTimestamp = when (entryTimeValue) {
                is Timestamp -> entryTimeValue
                is Long -> Timestamp(Date(entryTimeValue))
                else -> null
            }

            Visitor(
                id = doc.id,
                name = data["visitorName"] as? String ?: "",
                phone = data["visitorPhone"] as? String ?: "",
                flatNumber = data["flatNumber"] as? String ?: "",
                purpose = data["purpose"] as? String ?: "",
               visitorPhotoUrl = data["visitorPhotoUrl"] as? String ?: "",  // ✅ Map this field

                imageUrl = data["visitorPhotoUrl"] as? String ?: "",
                status = data["status"] as? String ?: "pending",
                entryTime = entryTimestamp,
                residentId = data["residentId"] as? String ?: "",
                organization = data["organization"] as? String ?: "",
                numberOfVisitors = (data["numberOfVisitors"] as? Long)?.toInt() ?: 1,
                vehicleNumber = data["vehicleNumber"] as? String ?: "",
                watchmanName = data["watchmanName"] as? String ?: "",
                notes = data["notes"] as? String ?: ""
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error getting visitor by ID", e)
            null
        }
    }

    /**
     * Approve visitor
     */
    fun approveVisitor(visitorId: String) {
        updateVisitorStatus(visitorId, "approved")
    }

    /**
     * Reject visitor
     */
    fun rejectVisitor(visitorId: String) {
        updateVisitorStatus(visitorId, "rejected")
    }

    /**
     * Leave at gate
     */
    fun leaveAtGate(visitorId: String) {
        updateVisitorStatus(visitorId, "leave_at_gate")
    }

    /**
     * Update visitor status
     */
    private fun updateVisitorStatus(visitorId: String, status: String) {
        firestore.collection("visitors")
            .document(visitorId)
            .update(
                mapOf(
                    "status" to status,
                    "actionTakenAt" to Timestamp.now()
                )
            )
            .addOnSuccessListener {
                Log.d(TAG, "Visitor $status successfully: $visitorId")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error updating visitor status", e)
                _errorMessage.value = e.message
            }
    }

    // ==================== PRE-APPROVED GUESTS FUNCTIONS ====================

    /**
     * ✅ Load pre-approved guests from Firestore
     */
    fun loadPreApprovedGuests() {
        val userId = auth.currentUser?.uid ?: return

        Log.d(TAG, "Loading pre-approved guests for user: $userId")

        firestore.collection("preApprovedGuests")
            .whereEqualTo("residentId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error loading pre-approved guests", error)
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    PreApprovedGuestItem(
                        id = doc.id,
                        guestName = doc.getString("guestName") ?: "",
                        guestPhone = doc.getString("guestPhone") ?: "",
                        flatNumber = doc.getString("flatNumber") ?: "",
                        purpose = doc.getString("purpose") ?: "",
                        schedule = doc.getString("schedule") ?: "",
                        validFrom = doc.getTimestamp("validFrom"),
                        validUntil = doc.getTimestamp("validUntil"),
                        isActive = doc.getBoolean("isActive") ?: true
                    )
                } ?: emptyList()

                _preApprovedGuests.value = list
                Log.d(TAG, "Pre-approved guests loaded: ${list.size}")
            }
    }

    /**
     * ✅ Add pre-approved guest
     */
    fun addPreApprovedGuest(
        guestName: String,
        guestPhone: String,
        purpose: String,
        schedule: String,
        validFrom: Date,
        validUntil: Date,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onComplete(false, "User not logged in")
            return
        }

        val data = hashMapOf(
            "guestName" to guestName,
            "guestPhone" to guestPhone,
            "purpose" to purpose,
            "schedule" to schedule,
            "validFrom" to Timestamp(validFrom),
            "validUntil" to Timestamp(validUntil),
            "residentId" to userId,
            "flatNumber" to _flatNumber.value,
            "isActive" to true,
            "createdAt" to Timestamp.now()
        )

        firestore.collection("preApprovedGuests")
            .add(data)
            .addOnSuccessListener {
                Log.d(TAG, "Pre-approved guest added successfully")
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding pre-approved guest", e)
                onComplete(false, e.message)
            }
    }

    /**
     * ✅ Delete pre-approved guest
     */
    fun deletePreApprovedGuest(
        guestId: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        firestore.collection("preApprovedGuests")
            .document(guestId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Pre-approved guest deleted successfully")
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error deleting pre-approved guest", e)
                onComplete(false, e.message)
            }
    }

    // ==================== HOUSEHOLD MEMBERS FUNCTIONS ====================

    /**
     * ✅ Load household members from Firestore
     */
    fun loadHouseholdMembers() {
        val userId = auth.currentUser?.uid ?: return

        Log.d(TAG, "Loading household members for user: $userId")

        firestore.collection("householdMembers")
            .whereEqualTo("residentId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error loading household members", error)
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    HouseholdMemberItem(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        relation = doc.getString("relation") ?: "",
                        phone = doc.getString("phone") ?: "",
                        age = doc.getLong("age")?.toInt() ?: 0
                    )
                } ?: emptyList()

                _householdMembers.value = list
                Log.d(TAG, "Household members loaded: ${list.size}")
            }
    }

    /**
     * ✅ Add household member
     */
    fun addHouseholdMember(
        name: String,
        relation: String,
        phone: String,
        age: Int,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onComplete(false, "User not logged in")
            return
        }

        val data = hashMapOf(
            "name" to name,
            "relation" to relation,
            "phone" to phone,
            "age" to age,
            "residentId" to userId,
            "flatNumber" to _flatNumber.value,
            "createdAt" to Timestamp.now()
        )

        firestore.collection("householdMembers")
            .add(data)
            .addOnSuccessListener {
                Log.d(TAG, "Household member added successfully")
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding household member", e)
                onComplete(false, e.message)
            }
    }

    /**
     * ✅ Delete household member
     */
    fun deleteHouseholdMember(
        memberId: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        firestore.collection("householdMembers")
            .document(memberId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Household member deleted successfully")
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error deleting household member", e)
                onComplete(false, e.message)
            }
    }

    // ==================== NOTIFICATIONS ====================

    /**
     * ✅ Load notifications for resident (optional)
     */
    fun loadNotifications(residentId: String) {
        Log.d(TAG, "loadNotifications called for residentId: $residentId")
        // This can be implemented later for notification features
        // For now, it's a placeholder to avoid compilation errors
    }

    // ==================== HELPER FUNCTIONS ====================

    fun formatTime(timestamp: Timestamp?): String {
        if (timestamp == null) return ""
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(timestamp.toDate())
    }

    fun formatDate(timestamp: Timestamp?): String {
        if (timestamp == null) return ""
        val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return format.format(timestamp.toDate())
    }

    fun clearError() {
        _errorMessage.value = null
    }
}