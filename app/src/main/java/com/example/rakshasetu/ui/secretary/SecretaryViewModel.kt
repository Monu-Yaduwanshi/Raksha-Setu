package com.example.rakshasetu.ui.secretary

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rakshasetu.data.models.*
import com.example.rakshasetu.viewModel.viewModel.auth.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Data class for Block Range
data class BlockRange(
    val blockName: String = "",
    val startFlat: String = "",
    val endFlat: String = ""
)

class SecretaryViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // State flows
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _society = MutableStateFlow<Society?>(null)
    val society: StateFlow<Society?> = _society.asStateFlow()

    private val _flats = MutableStateFlow<List<Flat>>(emptyList())
    val flats: StateFlow<List<Flat>> = _flats.asStateFlow()

    private val _residents = MutableStateFlow<List<User>>(emptyList())
    val residents: StateFlow<List<User>> = _residents.asStateFlow()

    private val _watchmen = MutableStateFlow<List<User>>(emptyList())
    val watchmen: StateFlow<List<User>> = _watchmen.asStateFlow()

    private val _joinRequests = MutableStateFlow<List<JoinRequest>>(emptyList())
    val joinRequests: StateFlow<List<JoinRequest>> = _joinRequests.asStateFlow()

    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    private val _allVisitors = MutableStateFlow<List<Visitor>>(emptyList())
    val allVisitors: StateFlow<List<Visitor>> = _allVisitors.asStateFlow()

    companion object {
        private const val TAG = "SecretaryViewModel"
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    private fun fromFirestore(document: DocumentSnapshot): User {
        val data = document.data ?: return User()
        return User(
            userId = document.id,
            fullName = data["fullName"] as? String ?: "",
            email = data["email"] as? String ?: "",
            phone = data["phone"] as? String ?: "",
            role = data["role"] as? String ?: "",
            societyId = data["societyId"] as? String ?: "",
            societyName = data["societyName"] as? String ?: "",
            flatNumber = data["flatNumber"] as? String ?: "",
            blockNumber = data["blockNumber"] as? String ?: "",
            profileImageUrl = data["profileImageUrl"] as? String ?: "",
            isActive = data["isActive"] as? Boolean ?: true,
            createdAt = data["createdAt"] as? Long ?: System.currentTimeMillis(),
            lastLogin = data["lastLogin"] as? Long ?: 0,
            fcmToken = data["fcmToken"] as? String ?: "",
            createdBy = data["createdBy"] as? String ?: ""
        )
    }

    fun loadSecretarySociety() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = auth.currentUser ?: return@launch
                val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
                val societyId = userDoc.getString("societyId")
                Log.d(TAG, "loadSecretarySociety - societyId from user: $societyId")

                if (!societyId.isNullOrEmpty()) {
                    loadSociety(societyId)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading secretary society", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createSociety(
        societyName: String,
        address: String,
        city: String,
        pincode: String,
        totalFlats: Int,
        totalBlocks: Int,
        blocks: List<String>,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = auth.currentUser ?: throw Exception("User not logged in")

                val existingSociety = firestore.collection("societies")
                    .whereEqualTo("createdBy", currentUser.uid)
                    .get()
                    .await()

                if (!existingSociety.isEmpty) {
                    _isLoading.value = false
                    onComplete(false, "You already have a society")
                    return@launch
                }

                val societyId = firestore.collection("societies").document().id
                val society = Society(
                    societyId = societyId,
                    societyName = societyName,
                    address = address,
                    city = city,
                    pincode = pincode,
                    totalFlats = totalFlats,
                    totalBlocks = totalBlocks,
                    blocks = blocks,
                    createdBy = currentUser.uid,
                    createdAt = Timestamp.now()
                )

                firestore.collection("societies").document(societyId).set(society).await()

                firestore.collection("users").document(currentUser.uid)
                    .update(mapOf(
                        "societyId" to societyId,
                        "societyName" to societyName
                    ))
                    .await()

                _society.value = society
                _isLoading.value = false
                onComplete(true, societyId)

            } catch (e: Exception) {
                Log.e(TAG, "Error creating society", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }

    fun updateSociety(
        societyId: String,
        societyName: String,
        address: String,
        city: String,
        pincode: String,
        totalFlats: Int,
        totalBlocks: Int,
        blocks: List<String>,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val data = mapOf(
                    "societyName" to societyName,
                    "address" to address,
                    "city" to city,
                    "pincode" to pincode,
                    "totalFlats" to totalFlats,
                    "totalBlocks" to totalBlocks,
                    "blocks" to blocks
                )

                firestore.collection("societies")
                    .document(societyId)
                    .update(data)
                    .await()

                onResult(true, null)

            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun updateBlocks(
        societyId: String,
        blocks: List<String>,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestore.collection("societies")
                    .document(societyId)
                    .update("blocks", blocks)
                    .await()

                onComplete(true, "Blocks updated successfully")

            } catch (e: Exception) {
                onComplete(false, e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load society data - FIXED with proper logging
     */
    fun loadSociety(societyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d(TAG, "🔵 loadSociety called with ID: $societyId")

                val snapshot = firestore.collection("societies")
                    .document(societyId)
                    .get()
                    .await()

                if (snapshot.exists()) {
                    val data = snapshot.data
                    if (data == null) {
                        Log.e(TAG, "❌ Society data is null for ID: $societyId")
                        _society.value = null
                        return@launch
                    }

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
                        createdAt = data["createdAt"] as? Timestamp
                    )

                    _society.value = society
                    Log.d(TAG, "✅ Society loaded: ${society.societyName} with ID: ${society.societyId}")
                    Log.d(TAG, "📋 Society blocks: ${society.blocks.joinToString()}")

                } else {
                    Log.e(TAG, "❌ Society document NOT FOUND for ID: $societyId")
                    _society.value = null
                }

                // Load all related data
                loadFlats(societyId)
                loadResidents(societyId)
                loadWatchmen(societyId)
                loadJoinRequests(societyId)
                loadAnnouncements(societyId)
                loadAllVisitors(societyId)

            } catch (e: Exception) {
                Log.e(TAG, "🔥 Error loading society", e)
                _errorMessage.value = e.message
                _society.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load flats - FIXED with DEBUG logging
     */
    fun loadFlats(societyId: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "🏢 loadFlats called for societyId: $societyId")

                val snapshot = firestore.collection("flats")
                    .whereEqualTo("societyId", societyId)
                    .get()
                    .await()

                Log.d(TAG, "📊 Flats query returned ${snapshot.size()} documents")
                val flatsList = snapshot.documents.map { doc ->
                    val occupiedValue = doc.getBoolean("occupied") ?: false
//                    val occupiedValue =
//                        doc.getBoolean("occupied")
//                            ?: doc.getBoolean("isOccupied")
//                            ?: false

                    Flat(
                        flatId = doc.getString("flatId") ?: doc.id,
                        blockNumber = doc.getString("blockNumber") ?: "",
                        flatNumber = doc.getString("flatNumber") ?: "",
                        occupied = occupiedValue,
                        residentId = doc.getString("residentId") ?: "",
                        residentName = doc.getString("residentName") ?: "",
                        residentPhone = doc.getString("residentPhone") ?: "",
                        societyId = doc.getString("societyId") ?: ""
                    )
                }

                _flats.value = flatsList
                Log.d(TAG, "✅ Loaded ${_flats.value.size} flats for society $societyId")

            } catch (e: Exception) {
                Log.e(TAG, "🔥 Error loading flats", e)
            }
        }
    }

    fun loadResidents(societyId: String) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users")
                    .whereEqualTo("societyId", societyId)
                    .whereEqualTo("role", "resident")
                    .get()
                    .await()

                _residents.value = snapshot.documents.mapNotNull { fromFirestore(it) }
                Log.d(TAG, "Loaded ${_residents.value.size} residents")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading residents", e)
            }
        }
    }

    fun loadWatchmen(societyId: String) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users")
                    .whereEqualTo("societyId", societyId)
                    .whereEqualTo("role", "watchman")
                    .get()
                    .await()

                _watchmen.value = snapshot.documents.mapNotNull { fromFirestore(it) }
                Log.d(TAG, "Loaded ${_watchmen.value.size} watchmen")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading watchmen", e)
            }
        }
    }

    fun addFlats(
        societyId: String,
        block: String,
        startFlat: Int,
        endFlat: Int,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val batch = firestore.batch()
                var successCount = 0

                for (flatNum in startFlat..endFlat) {
                    val flatNumber = flatNum.toString().padStart(3, '0')
                    val flatId = "${societyId}_${block}_${flatNumber}"

                    val flat = Flat(
                        flatId = flatId,
                        flatNumber = flatNumber,
                        blockNumber = block,
                        societyId = societyId,
                        occupied = false,
                        createdAt = Timestamp.now()
                    )

                    batch.set(firestore.collection("flats").document(flatId), flat)
                    successCount++
                }

                batch.commit().await()

                firestore.collection("societies").document(societyId)
                    .update("totalFlats", (endFlat - startFlat + 1))
                    .await()

                loadFlats(societyId)
                _isLoading.value = false
                onComplete(true, "Added $successCount flats")

            } catch (e: Exception) {
                Log.e(TAG, "Error adding flats", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }

    fun loadJoinRequests(societyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = firestore.collection("joinRequests")
                    .whereEqualTo("societyId", societyId)
                    .whereEqualTo("status", "pending")
                    .orderBy("requestedAt", Query.Direction.DESCENDING)
                    .get()
                    .await()

                _joinRequests.value = snapshot.documents.mapNotNull {
                    it.toObject(JoinRequest::class.java)
                }
                Log.d(TAG, "Loaded ${_joinRequests.value.size} join requests")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading join requests", e)
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun respondToRequest(
        request: JoinRequest,
        approved: Boolean,
        respondedBy: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestore.collection("joinRequests").document(request.requestId)
                    .update(mapOf(
                        "status" to if (approved) "approved" else "rejected",
                        "respondedAt" to Timestamp.now(),
                        "respondedBy" to respondedBy
                    ))
                    .await()

                if (approved) {
                    val userUpdates = mutableMapOf<String, Any>(
                        "societyId" to request.societyId,
                        "societyName" to request.societyName
                    )

                    if (request.userRole == "resident") {
                        userUpdates["flatNumber"] = request.flatNumber
                        userUpdates["blockNumber"] = request.blockNumber

                        val flatId = "${request.societyId}_${request.blockNumber}_${request.flatNumber}"
                        firestore.collection("flats").document(flatId)
                            .update(mapOf(
                                "residentId" to request.userId,
                                "residentName" to request.userName.ifBlank { "No Name" },
                                //"residentName" to request.userName,
                                "residentPhone" to request.userPhone,
                                "occupied" to true
                                //"Occupied" to true
                            ))
                            .await()
                    }
                    firestore.collection("users").document(request.userId)
                        .update(userUpdates)
                        .await()

// ✅ ADD HERE
                    loadResidents(request.societyId)
                    loadFlats(request.societyId)
//                    firestore.collection("users").document(request.userId)
//                        .update(userUpdates)
//                        .await()

                    createNotification(
                        userId = request.userId,
                        title = "Join Request Approved",
                        message = "Your request to join ${request.societyName} has been approved",
                        type = "join_approved"
                    )
                }

                loadJoinRequests(request.societyId)
                _isLoading.value = false
                onComplete(true, null)

            } catch (e: Exception) {
                Log.e(TAG, "Error responding to request", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }

    fun sendAnnouncement(
        title: String,
        message: String,
        priority: String,
        audience: String,
        societyId: String,
        createdByName: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = auth.currentUser ?: throw Exception("User not logged in")

                val announcementId = firestore.collection("announcements").document().id
                val announcement = Announcement(
                    announcementId = announcementId,
                    title = title,
                    message = message,
                    priority = priority,
                    audience = audience,
                    createdBy = currentUser.uid,
                    createdByName = createdByName,
                    societyId = societyId,
                    createdAt = Timestamp.now()
                )

                firestore.collection("announcements").document(announcementId)
                    .set(announcement)
                    .await()

                val usersQuery = when (audience) {
                    "residents" -> firestore.collection("users")
                        .whereEqualTo("societyId", societyId)
                        .whereEqualTo("role", "resident")
                    "watchmen" -> firestore.collection("users")
                        .whereEqualTo("societyId", societyId)
                        .whereEqualTo("role", "watchman")
                    else -> firestore.collection("users")
                        .whereEqualTo("societyId", societyId)
                }

                val users = usersQuery.get().await()
                users.documents.forEach { userDoc ->
                    createNotification(
                        userId = userDoc.id,
                        title = "New Announcement: $title",
                        message = message.take(100) + if (message.length > 100) "..." else "",
                        type = "announcement",
                        targetId = announcementId
                    )
                }

                _isLoading.value = false
                onComplete(true, announcementId)

            } catch (e: Exception) {
                Log.e(TAG, "Error sending announcement", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }

    fun loadAnnouncements(societyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = firestore.collection("announcements")
                    .whereEqualTo("societyId", societyId)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .get()
                    .await()

                _announcements.value = snapshot.documents.mapNotNull {
                    it.toObject(Announcement::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading announcements", e)
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAnnouncement(announcementId: String, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestore.collection("announcements").document(announcementId).delete().await()
                _isLoading.value = false
                onComplete(true, null)
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting announcement", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }

    fun loadAllVisitors(societyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = firestore.collection("visitors")
                    .whereEqualTo("societyId", societyId)
                    .orderBy("entryTime", Query.Direction.DESCENDING)
                    .limit(100)
                    .get()
                    .await()

                _allVisitors.value = snapshot.documents.mapNotNull {
                    it.toObject(Visitor::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading visitors", e)
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeResident(
        residentId: String,
        flatId: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val batch = firestore.batch()

                batch.update(firestore.collection("flats").document(flatId),
                    mapOf("occupied" to false, "residentId" to "", "residentName" to ""))

                batch.update(firestore.collection("users").document(residentId),
                    mapOf("societyId" to "", "flatNumber" to "", "blockNumber" to ""))

                batch.commit().await()

                val currentSociety = _society.value
                currentSociety?.societyId?.let {
                    loadResidents(it)
                    loadFlats(it)
                }

                _isLoading.value = false
                onComplete(true, null)

            } catch (e: Exception) {
                Log.e(TAG, "Error removing resident", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }

    private suspend fun createNotification(
        userId: String,
        title: String,
        message: String,
        type: String,
        targetId: String = ""
    ) {
        try {
            val notification = Notification(
                notificationId = firestore.collection("notifications").document().id,
                userId = userId,
                title = title,
                message = message,
                type = type,
                targetId = targetId,
                createdAt = Timestamp.now()
            )
            firestore.collection("notifications").document(notification.notificationId)
                .set(notification)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error creating notification", e)
        }
    }

    // ==================== NEW FUNCTIONS ADDED ====================

    /**
     * Get block ranges from existing flats
     */
    suspend fun getBlockRangesFromFlats(societyId: String): List<BlockRange> {
        return try {
            val snapshot = firestore.collection("flats")
                .whereEqualTo("societyId", societyId)
                .get()
                .await()

            val grouped = snapshot.documents.groupBy { it.getString("blockNumber") ?: "" }

            grouped.map { (block, flats) ->
                val numbers = flats.mapNotNull {
                    it.getString("flatNumber")?.toIntOrNull()
                }.sorted()

                BlockRange(
                    blockName = block,
                    startFlat = numbers.firstOrNull()?.toString() ?: "",
                    endFlat = numbers.lastOrNull()?.toString() ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting block ranges from flats", e)
            emptyList()
        }
    }

    /**
     * Update society with flat edit (SAFE - only adds new flats, never deletes occupied ones)
     */
    fun updateSocietyWithFlatEdit(
        societyId: String,
        societyName: String,
        address: String,
        city: String,
        pincode: String,
        blockRanges: List<BlockRange>,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // ✅ Calculate new totals
                val totalBlocks = blockRanges.count { it.blockName.isNotBlank() }

                val totalFlats = blockRanges.sumOf { range ->
                    val start = range.startFlat.toIntOrNull() ?: 0
                    val end = range.endFlat.toIntOrNull() ?: 0
                    if (start > 0 && end >= start && range.blockName.isNotBlank()) {
                        (end - start + 1)
                    } else 0
                }

                // Step 1: Update society basic info WITH totals
                val updates = mutableMapOf<String, Any>(
                    "societyName" to societyName,
                    "address" to address,
                    "city" to city,
                    "pincode" to pincode,
                    "totalBlocks" to totalBlocks,    // ✅ UPDATE
                    "totalFlats" to totalFlats       // ✅ UPDATE
                )

                // Also update blocks list if needed
                val blocksList = blockRanges.map { it.blockName }.filter { it.isNotBlank() }
                updates["blocks"] = blocksList

                firestore.collection("societies")
                    .document(societyId)
                    .update(updates)
                    .await()

                Log.d(TAG, "✅ Society totals updated: $totalBlocks blocks, $totalFlats flats")

                // Rest of your existing logic for adding new flats...
                val snapshot = firestore.collection("flats")
                    .whereEqualTo("societyId", societyId)
                    .get()
                    .await()

                val existingFlats = snapshot.documents
                val existingKeys = existingFlats.map {
                    Pair(
                        it.getString("blockNumber"),
                        it.getString("flatNumber")
                    )
                }.toSet()

                val batch = firestore.batch()
                var newFlatsCount = 0

                blockRanges.forEach { block ->
                    if (block.blockName.isNotBlank()) {
                        val start = block.startFlat.toIntOrNull() ?: return@forEach
                        val end = block.endFlat.toIntOrNull() ?: return@forEach

                        for (i in start..end) {
                            val flatNumber = i.toString().padStart(3, '0')
                            val key = Pair(block.blockName, flatNumber)

                            if (!existingKeys.contains(key)) {
                                val flatId = "${societyId}_${block.blockName}_${flatNumber}"
                                val flat = Flat(
                                    flatId = flatId,
                                    flatNumber = flatNumber,
                                    blockNumber = block.blockName,
                                    societyId = societyId,
                                    occupied = false,
                                    createdAt = Timestamp.now()
                                )
                                batch.set(firestore.collection("flats").document(flatId), flat)
                                newFlatsCount++
                            }
                        }
                    }
                }

                if (newFlatsCount > 0) {
                    batch.commit().await()
                    Log.d(TAG, "Added $newFlatsCount new flats")

                    // ✅ Update totalFlats again after adding new flats
                    val finalTotalFlats = totalFlats + newFlatsCount
                    firestore.collection("societies").document(societyId)
                        .update("totalFlats", finalTotalFlats)
                        .await()
                }

                loadFlats(societyId)
                onResult(true, "Society updated successfully with $newFlatsCount new flats")

            } catch (e: Exception) {
                Log.e(TAG, "Error updating society with flats", e)
                onResult(false, e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Create society with blocks and flats (for new society creation)
     */
    fun createSocietyWithBlocks(
        societyName: String,
        address: String,
        city: String,
        pincode: String,
        blockRanges: List<BlockRange>,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = auth.currentUser ?: throw Exception("User not logged in")

                // Check if user already has a society
                val existingSociety = firestore.collection("societies")
                    .whereEqualTo("createdBy", currentUser.uid)
                    .get()
                    .await()

                if (!existingSociety.isEmpty) {
                    _isLoading.value = false
                    onComplete(false, "You already have a society")
                    return@launch
                }

                val societyId = firestore.collection("societies").document().id
                val blocksList = blockRanges.map { it.blockName }

                // ✅ FIX: Calculate totalBlocks and totalFlats correctly
                val totalBlocks = blocksList.size

                val totalFlats = blockRanges.sumOf { range ->
                    val start = range.startFlat.toIntOrNull() ?: 0
                    val end = range.endFlat.toIntOrNull() ?: 0
                    if (start > 0 && end >= start) {
                        (end - start + 1)
                    } else 0
                }

                val society = Society(
                    societyId = societyId,
                    societyName = societyName,
                    address = address,
                    city = city,
                    pincode = pincode,
                    totalFlats = totalFlats,      // ✅ FIXED
                    totalBlocks = totalBlocks,    // ✅ FIXED
                    blocks = blocksList,
                    createdBy = currentUser.uid,
                    createdAt = Timestamp.now()
                )

                firestore.collection("societies").document(societyId).set(society).await()
                Log.d(TAG, "✅ Society created with $totalBlocks blocks and $totalFlats flats")

                firestore.collection("users").document(currentUser.uid)
                    .update(mapOf(
                        "societyId" to societyId,
                        "societyName" to societyName
                    ))
                    .await()

                // Generate flats
                val batch = firestore.batch()
                var flatCount = 0

                blockRanges.forEach { range ->
                    val block = range.blockName.trim().uppercase()
                    val start = range.startFlat.toInt()
                    val end = range.endFlat.toInt()

                    for (i in start..end) {
                        val flatNumber = i.toString().padStart(3, '0')
                        val flatId = "${societyId}_${block}_$flatNumber"

                        val flat = Flat(
                            flatId = flatId,
                            flatNumber = flatNumber,
                            blockNumber = block,
                            societyId = societyId,
                            occupied = false,
                            createdAt = Timestamp.now()
                        )

                        batch.set(firestore.collection("flats").document(flatId), flat)
                        flatCount++
                    }
                }

                batch.commit().await()
                Log.d(TAG, "✅ Created $flatCount flats")

                // ✅ Update totalFlats again to be absolutely sure
                if (flatCount != totalFlats) {
                    firestore.collection("societies").document(societyId)
                        .update("totalFlats", flatCount)
                        .await()
                    Log.w(TAG, "⚠️ Adjusted totalFlats from $totalFlats to $flatCount")
                }

                _society.value = society
                _isLoading.value = false
                onComplete(true, societyId)

            } catch (e: Exception) {
                Log.e(TAG, "Error creating society with blocks", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }
    /**
     * Get only vacant flats grouped by block
     */
    fun getVacantFlatsByBlock(): Map<String, List<Flat>> {
        val result = mutableMapOf<String, MutableList<Flat>>()

        _flats.value
            .filter { !it.occupied }
            .forEach { flat ->
                val block = flat.blockNumber

                if (!result.containsKey(block)) {
                    result[block] = mutableListOf()
                }
                result[block]?.add(flat)
            }

        return result
    }
    fun clearError() {
        _errorMessage.value = null
    }
}