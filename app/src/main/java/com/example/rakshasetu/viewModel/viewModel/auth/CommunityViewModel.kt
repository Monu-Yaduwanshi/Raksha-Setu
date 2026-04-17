package com.example.rakshasetu.viewModel.viewModel.auth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log
import com.google.firebase.Timestamp

// Data class for Join Request
data class JoinRequest(
    val requestId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val userPhone: String = "",
    val userRole: String = "", // resident, watchman
    val societyId: String = "",
    val societyName: String = "",
    val flatNumber: String = "", // for residents
    val blockNumber: String = "", // for residents
    val shift: String = "", // for watchmen
    val status: String = "pending", // pending, approved, rejected
    val requestedAt: Timestamp? = null,
    val respondedAt: Timestamp? = null,
    val respondedBy: String = ""
) {
    // Convert to Map for Firestore
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "requestId" to requestId,
            "userId" to userId,
            "userName" to userName,
            "userEmail" to userEmail,
            "userPhone" to userPhone,
            "userRole" to userRole,
            "societyId" to societyId,
            "societyName" to societyName,
            "flatNumber" to flatNumber,
            "blockNumber" to blockNumber,
            "shift" to shift,
            "status" to status
        )

        // Add timestamps if they exist
        requestedAt?.let { map["requestedAt"] = it }
        respondedAt?.let { map["respondedAt"] = it }
        if (respondedBy.isNotBlank()) map["respondedBy"] = respondedBy

        return map
    }

    companion object {
        // Create from Firestore document
        fun fromFirestore(document: com.google.firebase.firestore.DocumentSnapshot): JoinRequest {
            val data = document.data ?: return JoinRequest()
            return JoinRequest(
                requestId = document.id,
                userId = data["userId"] as? String ?: "",
                userName = data["userName"] as? String ?: "",
                userEmail = data["userEmail"] as? String ?: "",
                userPhone = data["userPhone"] as? String ?: "",
                userRole = data["userRole"] as? String ?: "",
                societyId = data["societyId"] as? String ?: "",
                societyName = data["societyName"] as? String ?: "",
                flatNumber = data["flatNumber"] as? String ?: "",
                blockNumber = data["blockNumber"] as? String ?: "",
                shift = data["shift"] as? String ?: "",
                status = data["status"] as? String ?: "pending",
                requestedAt = data["requestedAt"] as? Timestamp,
                respondedAt = data["respondedAt"] as? Timestamp,
                respondedBy = data["respondedBy"] as? String ?: ""
            )
        }
    }
}

class CommunityViewModel : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Society>>(emptyList())
    val searchResults: StateFlow<List<Society>> = _searchResults.asStateFlow()

    private val _joinRequests = MutableStateFlow<List<JoinRequest>>(emptyList())
    val joinRequests: StateFlow<List<JoinRequest>> = _joinRequests.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    companion object {
        private const val TAG = "CommunityViewModel"
    }

    /**
     * Search societies by name or area
     */
    fun searchSocieties(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Searching societies with query: $query")

            try {
                // Search by society name (contains query)
                val nameSnapshot = firestore.collection("societies")
                    .whereGreaterThanOrEqualTo("societyName", query)
                    .whereLessThanOrEqualTo("societyName", query + "\uf8ff")
                    .get()
                    .await()

                // Search by address (contains query)
                val addressSnapshot = firestore.collection("societies")
                    .whereGreaterThanOrEqualTo("address", query)
                    .whereLessThanOrEqualTo("address", query + "\uf8ff")
                    .get()
                    .await()

                // Combine results (remove duplicates)
                val results = mutableMapOf<String, Society>()

                nameSnapshot.documents.forEach { doc ->
                    val data = doc.data ?: return@forEach
                    val society = Society(
                        societyId = doc.id,
                        societyName = data["societyName"] as? String ?: "",
                        address = data["address"] as? String ?: "",
                        city = data["city"] as? String ?: "",
                        pincode = data["pincode"] as? String ?: "",
                        totalFlats = (data["totalFlats"] as? Long)?.toInt() ?: 0,
                        totalBlocks = (data["totalBlocks"] as? Long)?.toInt() ?: 0,
                        blocks = data["blocks"] as? List<String> ?: emptyList(),
                        createdBy = data["createdBy"] as? String ?: "",
                      //  createdAt = data["createdAt"] as? Long ?: 0
                    )
                    results[doc.id] = society
                }

                addressSnapshot.documents.forEach { doc ->
                    val data = doc.data ?: return@forEach
                    val society = Society(
                        societyId = doc.id,
                        societyName = data["societyName"] as? String ?: "",
                        address = data["address"] as? String ?: "",
                        city = data["city"] as? String ?: "",
                        pincode = data["pincode"] as? String ?: "",
                        totalFlats = (data["totalFlats"] as? Long)?.toInt() ?: 0,
                        totalBlocks = (data["totalBlocks"] as? Long)?.toInt() ?: 0,
                        blocks = data["blocks"] as? List<String> ?: emptyList(),
                        createdBy = data["createdBy"] as? String ?: "",
                     //   createdAt = data["createdAt"] as? Long ?: 0
                    )
                    results[doc.id] = society
                }

                _searchResults.value = results.values.toList()
                Log.d(TAG, "Found ${results.size} societies")

            } catch (e: Exception) {
                Log.e(TAG, "Error searching societies", e)
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Request to join a society
     */
    fun requestToJoin(
        userId: String,
        userName: String,
        userEmail: String,
        userPhone: String,
        userRole: String,
        societyId: String,
        societyName: String,
        flatNumber: String = "",
        blockNumber: String = "",
        shift: String = "",
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "User $userId requesting to join society $societyId")

            try {
                // Check if request already exists
                val existingRequest = firestore.collection("joinRequests")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("societyId", societyId)
                    .whereEqualTo("status", "pending")
                    .get()
                    .await()

                if (!existingRequest.isEmpty) {
                    _isLoading.value = false
                    onComplete(false, "You already have a pending request for this society")
                    return@launch
                }

                // Create join request
                val requestId = firestore.collection("joinRequests").document().id
                val request = JoinRequest(
                    requestId = requestId,
                    userId = userId,
                    userName = userName,
                    userEmail = userEmail,
                    userPhone = userPhone,
                    userRole = userRole,
                    societyId = societyId,
                    societyName = societyName,
                    flatNumber = flatNumber,
                    blockNumber = blockNumber,
                    shift = shift,
                    status = "pending",
                    requestedAt = Timestamp.now()
                )

                firestore.collection("joinRequests").document(requestId)
                    .set(request.toMap())
                    .await()

                Log.d(TAG, "Join request created: $requestId")

                _isLoading.value = false
                onComplete(true, requestId)

            } catch (e: Exception) {
                Log.e(TAG, "Error creating join request", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }

    /**
     * Get pending join requests for a society (Secretary only)
     */
    fun getPendingRequests(societyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Fetching pending requests for society: $societyId")

            try {
                val snapshot = firestore.collection("joinRequests")
                    .whereEqualTo("societyId", societyId)
                    .whereEqualTo("status", "pending")
                    .orderBy("requestedAt", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val requests = snapshot.documents.mapNotNull { doc ->
                    JoinRequest.fromFirestore(doc)
                }

                _joinRequests.value = requests
                Log.d(TAG, "Found ${requests.size} pending requests")

            } catch (e: Exception) {
                Log.e(TAG, "Error fetching pending requests", e)
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Approve or reject join request (Secretary only)
     */
    fun respondToRequest(
        requestId: String,
        userId: String,
        societyId: String,
        societyName: String,
        userRole: String,
        flatNumber: String,
        blockNumber: String,
        shift: String,
        approved: Boolean,
        respondedBy: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Responding to request $requestId: approved=$approved")

            try {
                // Update request status
                val requestUpdates = mutableMapOf<String, Any>(
                    "status" to if (approved) "approved" else "rejected",
                    "respondedAt" to Timestamp.now(),
                    "respondedBy" to respondedBy
                )

                firestore.collection("joinRequests").document(requestId)
                    .update(requestUpdates)
                    .await()

                if (approved) {
                    // Update user document with society info - FIXED TYPE MISMATCH
                    val userUpdates = mutableMapOf<String, Any>(
                        "societyId" to societyId,
                        "societyName" to societyName
                    )

                    if (userRole == "resident") {
                        userUpdates["flatNumber"] = flatNumber
                        userUpdates["blockNumber"] = blockNumber

                        // Create flat document
                        val flatData = mutableMapOf<String, Any>(
                            "flatNumber" to flatNumber,
                            "blockNumber" to blockNumber,
                            "societyId" to societyId,
                            "residentId" to userId,
                            "residentName" to "", // Will be updated when user completes profile
                            "isOccupied" to true,
                            "createdAt" to System.currentTimeMillis()
                        )

                        firestore.collection("flats").document("${societyId}_${blockNumber}_${flatNumber}")
                            .set(flatData)
                            .await()

                    } else if (userRole == "watchman") {
                        // Add to watchmen collection
                        val watchmanData = mutableMapOf<String, Any>(
                            "watchmanId" to userId,
                            "name" to "", // Will be updated when user completes profile
                            "phone" to "",
                            "societyId" to societyId,
                            "shift" to shift,
                            "isActive" to true,
                            "joinedAt" to System.currentTimeMillis()
                        )

                        firestore.collection("watchmen").document(userId)
                            .set(watchmanData)
                            .await()
                    }

                    // Update user document - using the correctly typed map
                    firestore.collection("users").document(userId)
                        .update(userUpdates)
                        .await()

                    Log.d(TAG, "User $userId updated with society info")
                }

                Log.d(TAG, "Request response processed successfully")
                _isLoading.value = false
                onComplete(true, null)

            } catch (e: Exception) {
                Log.e(TAG, "Error responding to request", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }

    /**
     * Get all approved members of a society
     */
    fun getSocietyMembers(
        societyId: String,
        onResult: (List<Map<String, String>>?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users")
                    .whereEqualTo("societyId", societyId)
                    .whereEqualTo("isActive", true)
                    .get()
                    .await()

                val members = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    mapOf(
                        "userId" to (data["userId"] as? String ?: ""),
                        "name" to (data["fullName"] as? String ?: ""),
                        "role" to (data["role"] as? String ?: ""),
                        "phone" to (data["phone"] as? String ?: ""),
                        "flatNumber" to (data["flatNumber"] as? String ?: "")
                    )
                }

                onResult(members)

            } catch (e: Exception) {
                Log.e(TAG, "Error getting society members", e)
                onResult(null)
            }
        }
    }

    /**
     * Check if user has pending request for a society
     */
    fun checkPendingRequest(
        userId: String,
        societyId: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("joinRequests")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("societyId", societyId)
                    .whereEqualTo("status", "pending")
                    .get()
                    .await()

                onResult(!snapshot.isEmpty)

            } catch (e: Exception) {
                Log.e(TAG, "Error checking pending request", e)
                onResult(false)
            }
        }
    }

    /**
     * Cancel pending request
     */
    fun cancelRequest(
        requestId: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestore.collection("joinRequests").document(requestId)
                    .delete()
                    .await()

                _isLoading.value = false
                onComplete(true, null)

            } catch (e: Exception) {
                Log.e(TAG, "Error canceling request", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }

    /**
     * Get society details by ID
     */
    fun getSocietyById(
        societyId: String,
        onResult: (Society?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("societies").document(societyId).get().await()
                if (snapshot.exists()) {
                    val data = snapshot.data ?: return@launch onResult(null)
                    val society = Society(
                        societyId = snapshot.id,
                        societyName = data["societyName"] as? String ?: "",
                        address = data["address"] as? String ?: "",
                        city = data["city"] as? String ?: "",
                        pincode = data["pincode"] as? String ?: "",
                        totalFlats = (data["totalFlats"] as? Long)?.toInt() ?: 0,
                        totalBlocks = (data["totalBlocks"] as? Long)?.toInt() ?: 0,
                        blocks = data["blocks"] as? List<String> ?: emptyList(),
                        createdBy = data["createdBy"] as? String ?: "",
                     //   createdAt = data["createdAt"] as? Long ?: 0
                    )
                    onResult(society)
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting society", e)
                onResult(null)
            }
        }
    }

    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}