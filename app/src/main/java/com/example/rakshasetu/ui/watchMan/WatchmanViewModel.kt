package com.example.rakshasetu.viewModel.viewModel.watchman

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rakshasetu.data.models.*
import com.example.rakshasetu.viewModel.viewModel.Cloudinary.CloudinaryHelper
import com.example.rakshasetu.viewModel.viewModel.Cloudinary.UploadCallback
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

data class VisitorItem(
    val id: String,
    val name: String,
    val flat: String,
    val time: String,
    val status: String,
    val photoUrl: String,
    val purpose: String,
    val entryTime: Timestamp?
)

data class FlatOwner(
    val flatId: String = "",
    val flatNumber: String = "",
    val ownerName: String = "",
    val ownerId: String = "",
    val phone: String = "",
    val isOccupied: Boolean = false  // ✅ Fixed field name
)
//data class FlatOwner(
//    val flatId: String,
//    val flatNumber: String,
//    val ownerName: String,
//    val ownerId: String,
//    val phone: String,
//    val isOccupied: Boolean
//)

data class PreApprovedGuestItem(
    val id: String,
    val guestName: String,
    val flatNumber: String,
    val residentId: String,
    val schedule: String,
    val purpose: String,
    val isValid: Boolean
)

class WatchmanViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Cloudinary
    private val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dfkfuassi/image/upload"
    private val cloudinaryUploadPreset = "thread0"
    private val client = OkHttpClient()

    // State flows
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _uploadProgress = MutableStateFlow(0)
    val uploadProgress: StateFlow<Int> = _uploadProgress.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _userName = MutableStateFlow("Watchman")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _societyId = MutableStateFlow("")
    val societyId: StateFlow<String> = _societyId.asStateFlow()

    private val _societyName = MutableStateFlow("")
    val societyName: StateFlow<String> = _societyName.asStateFlow()

    private val _todayVisitorsCount = MutableStateFlow(0)
    val todayVisitorsCount: StateFlow<Int> = _todayVisitorsCount.asStateFlow()

    private val _pendingVisitorsCount = MutableStateFlow(0)
    val pendingVisitorsCount: StateFlow<Int> = _pendingVisitorsCount.asStateFlow()

    private val _recentVisitors = MutableStateFlow<List<VisitorItem>>(emptyList())
    val recentVisitors: StateFlow<List<VisitorItem>> = _recentVisitors.asStateFlow()

    private val _visitorHistory = MutableStateFlow<List<VisitorItem>>(emptyList())
    val visitorHistory: StateFlow<List<VisitorItem>> = _visitorHistory.asStateFlow()

    private val _flats = MutableStateFlow<List<FlatOwner>>(emptyList())
    val flats: StateFlow<List<FlatOwner>> = _flats.asStateFlow()

    private val _preApprovedGuests = MutableStateFlow<List<PreApprovedGuestItem>>(emptyList())
    val preApprovedGuests: StateFlow<List<PreApprovedGuestItem>> = _preApprovedGuests.asStateFlow()

    companion object {
        private const val TAG = "WatchmanViewModel"
    }
    /**
     * ✅ FIXED: Load residents for flat selection
     * The document ID is the Firebase Auth UID
     */
    /**
     * ✅ FIXED: Get resident by flat number - ALWAYS use document ID as Firebase UID
     */
    suspend fun getResidentByFlat(flatNumber: String): Resident? {
        return try {
            Log.d(TAG, "getResidentByFlat called for flat: $flatNumber")

            val querySnapshot = firestore.collection("users")
                .whereEqualTo("flatNumber", flatNumber)
                .whereEqualTo("role", "resident")
                .limit(1)
                .get()
                .await()

            val document = querySnapshot.documents.firstOrNull()

            if (document != null) {
                // ✅ CRITICAL: document.id is the Firebase Auth UID
                val resident = Resident(
                    documentId = document.id,  // ✅ This is the Firebase UID
                    fullName = document.getString("fullName") ?: "",
                    flatNumber = document.getString("flatNumber") ?: "",
                    phone = document.getString("phone") ?: "",
                    email = document.getString("email") ?: "",
                    role = "resident",
                    societyId = document.getString("societyId") ?: ""
                )

                Log.d(TAG, "=========================================")
                Log.d(TAG, "✅ Found resident for flat: $flatNumber")
                Log.d(TAG, "✅ Resident Name: ${resident.fullName}")
                Log.d(TAG, "✅ Resident UID (documentId): ${resident.documentId}")
                Log.d(TAG, "⚠️ This MUST match the logged-in resident's Firebase UID")
                Log.d(TAG, "=========================================")

                return resident
            } else {
                Log.e(TAG, "❌ No resident found for flat: $flatNumber")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting resident by flat", e)
            null
        }
    }
//    suspend fun getResidentByFlat(flatNumber: String): Resident? {
//        return try {
//            val querySnapshot = firestore.collection("users")
//                .whereEqualTo("flatNumber", flatNumber)
//                .whereEqualTo("role", "resident")
//                .limit(1)
//                .get()
//                .await()
//
//            val document = querySnapshot.documents.firstOrNull()
//            if (document != null) {
//                // ✅ CRITICAL: document.id is the Firebase Auth UID
//                Resident(
//                    documentId = document.id,  // ✅ This is the correct UID
//                    fullName = document.getString("fullName") ?: "",
//                    flatNumber = document.getString("flatNumber") ?: "",
//                    phone = document.getString("phone") ?: "",
//                    email = document.getString("email") ?: "",
//                    role = "resident"
//                )
//            } else {
//                null
//            }
//        } catch (e: Exception) {
//            Log.e(TAG, "Error getting resident by flat", e)
//            null
//        }
//    }
    /**
     * ✅ FIXED: Create visitor request with CORRECT residentId (documentId / Firebase UID)
     */
    fun createVisitorRequest(
        flatNumber: String,
        visitorName: String,
        visitorPhone: String,
        purpose: String,
        organization: String,
        numberOfVisitors: Int,
        vehicleNumber: String,
        notes: String,
        photoUrl: String,
        residentId: String,  // ✅ This MUST be the documentId (Firebase Auth UID)
        residentName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = auth.currentUser ?: throw Exception("Not logged in")

                // Get watchman details
                val watchmanQuery = firestore.collection("watchman")
                    .whereEqualTo("watchmanId", currentUser.uid)
                    .get()
                    .await()

                val watchmanDoc = watchmanQuery.documents.firstOrNull()
                val watchmanName = watchmanDoc?.getString("name") ?: "Watchman"
                val societyId = watchmanDoc?.getString("societyId") ?: ""

                // Generate unique visitor ID
                val visitorId = firestore.collection("visitors").document().id

                // ✅ CRITICAL: Log the residentId being stored
                Log.d(TAG, "=========================================")
                Log.d(TAG, "Creating visitor with:")
                Log.d(TAG, "  visitorName: $visitorName")
                Log.d(TAG, "  flatNumber: $flatNumber")
                Log.d(TAG, "  residentId (Firebase UID): $residentId")
                Log.d(TAG, "  residentName: $residentName")
                Log.d(TAG, "  societyId: $societyId")
                Log.d(TAG, "=========================================")

                val visitor = hashMapOf(
                    "visitorId" to visitorId,
                    "visitorName" to visitorName,
                    "visitorPhone" to visitorPhone,
                    "flatNumber" to flatNumber,
                    "purpose" to purpose,
                    "organization" to organization,
                    "numberOfVisitors" to numberOfVisitors,
                    "vehicleNumber" to vehicleNumber,
                    "notes" to notes,
                    "visitorPhotoUrl" to photoUrl,

                    // 🔥 CRITICAL FIX: This MUST be the Firebase Auth UID (documentId)
                    "residentId" to residentId,      // ✅ Must match logged-in resident's UID
                    "residentName" to residentName,
                    "societyId" to societyId,

                    "watchmanId" to currentUser.uid,
                    "watchmanName" to watchmanName,

                    "status" to "pending",
                    "entryTime" to Timestamp.now(),
                    "createdAt" to Timestamp.now(),
                    "updatedAt" to Timestamp.now()
                )

                firestore.collection("visitors")
                    .document(visitorId)
                    .set(visitor)
                    .await()

                Log.d(TAG, "✅ Visitor request created successfully with ID: $visitorId")
                Log.d(TAG, "✅ Stored with residentId: $residentId")

                _isLoading.value = false
                onSuccess()

            } catch (e: Exception) {
                Log.e(TAG, "Error creating visitor request", e)
                _isLoading.value = false
                onError(e.message ?: "Unknown error")
            }
        }
    }
    /**
     * ✅ FIXED: Create visitor request with CORRECT residentId (Auth UID)
     */
//    fun createVisitorRequest(
//        flatNumber: String,
//        visitorName: String,
//        visitorPhone: String,
//        purpose: String,
//        organization: String,
//        numberOfVisitors: Int,
//        vehicleNumber: String,
//        notes: String,
//        photoUrl: String,
//        residentId: String,  // ✅ This MUST be the Firebase Auth UID
//        residentName: String,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
//    ) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val currentUser = auth.currentUser ?: throw Exception("Not logged in")
//
//                // Get watchman details
//                val watchmanDoc = firestore.collection("watchman")
//                    .whereEqualTo("watchmanId", currentUser.uid)
//                    .get()
//                    .await()
//
//                val watchmanName = watchmanDoc.documents.firstOrNull()?.getString("name") ?: "Watchman"
//                val societyId = watchmanDoc.documents.firstOrNull()?.getString("societyId") ?: ""
//
//                // Generate unique visitor ID
//                val visitorId = firestore.collection("visitors").document().id
//
//                // ✅ CRITICAL: Log the residentId being stored
//                Log.d(TAG, "=========================================")
//                Log.d(TAG, "Creating visitor with:")
//                Log.d(TAG, "  visitorName: $visitorName")
//                Log.d(TAG, "  flatNumber: $flatNumber")
//                Log.d(TAG, "  residentId (Auth UID): $residentId")
//                Log.d(TAG, "  residentName: $residentName")
//                Log.d(TAG, "=========================================")
//
//                val visitor = hashMapOf(
//                    "visitorId" to visitorId,
//                    "visitorName" to visitorName,
//                    "visitorPhone" to visitorPhone,
//                    "flatNumber" to flatNumber,
//                    "purpose" to purpose,
//                    "organization" to organization,
//                    "numberOfVisitors" to numberOfVisitors,
//                    "vehicleNumber" to vehicleNumber,
//                    "notes" to notes,
//                    "visitorPhotoUrl" to photoUrl,
//
//                    // 🔥 CRITICAL FIX: Use the documentId (Firebase Auth UID)
//                    "residentId" to residentId,      // ✅ This MUST be the Auth UID
//                    "residentName" to residentName,
//                    "societyId" to societyId,
//
//                    "watchmanId" to currentUser.uid,
//                    "watchmanName" to watchmanName,
//
//                    "status" to "pending",
//                    "entryTime" to Timestamp.now(),
//                    "createdAt" to Timestamp.now(),
//                    "updatedAt" to Timestamp.now()
//                )
//
//                firestore.collection("visitors")
//                    .document(visitorId)
//                    .set(visitor)
//                    .await()
//
//                Log.d(TAG, "✅ Visitor request created successfully with ID: $visitorId")
//                Log.d(TAG, "✅ Stored with residentId: $residentId")
//
//                _isLoading.value = false
//                onSuccess()
//
//            } catch (e: Exception) {
//                Log.e(TAG, "Error creating visitor request", e)
//                _isLoading.value = false
//                onError(e.message ?: "Unknown error")
//            }
//        }
//    }
//    fun createVisitorRequest(
//        flatNumber: String,
//        visitorName: String,
//        visitorPhone: String,
//        purpose: String,
//        organization: String,
//        numberOfVisitors: Int,
//        vehicleNumber: String,  // ✅ Add this parameter
//        notes: String,          // ✅ Add this parameter
//        photoUrl: String,
//        residentId: String,     // ✅ Add this parameter
//        residentName: String,   // ✅ Add this parameter
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
////    fun createVisitorRequest(
////        flatNumber: String,
////        visitorName: String,
////        visitorPhone: String,
////        purpose: String,
////        organization: String,
////        numberOfVisitors: Int,
////        photoUrl: String,
////        onSuccess: () -> Unit,
////        onError: (String) -> Unit
//    ) {
////        viewModelScope.launch {
////            try {
////                val currentUser = auth.currentUser
////                if (currentUser == null) {
////                    onError("User not logged in")
////                    return@launch
////                }
////
////                // Get societyId from current state or user
////                var societyId = _societyId.value
////                if (societyId.isEmpty()) {
////                    val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
////                    societyId = userDoc.getString("societyId") ?: ""
////                    _societyId.value = societyId
////                }
////
////                // Step 1: Get flat → resident mapping
////                val flatSnapshot = firestore.collection("flats")
////                    .whereEqualTo("flatNumber", flatNumber)
////                    .whereEqualTo("societyId", societyId)
////                    .get()
////                    .await()
////
////                val flatDoc = flatSnapshot.documents.firstOrNull()
////
////                if (flatDoc == null) {
////                    onError("Flat not found")
////                    return@launch
////                }
////
////                val residentId = flatDoc.getString("residentId") ?: ""
////                val residentName = flatDoc.getString("residentName") ?: flatDoc.getString("ownerName") ?: ""
////
////                if (residentId.isEmpty()) {
////                    onError("No resident assigned to this flat")
////                    return@launch
////                }
////
////                // Step 2: Create visitor document
////                val visitorId = firestore.collection("visitors").document().id
////
////                val visitor = hashMapOf(
////                    "visitorId" to visitorId,
////                    "visitorName" to visitorName,
////                    "visitorPhone" to visitorPhone,
////                    "visitorPhotoUrl" to photoUrl,
////                    "flatNumber" to flatNumber,
////                    "residentId" to residentId,
////                    "residentName" to residentName,
////                    "watchmanId" to currentUser.uid,
////                    "watchmanName" to _userName.value,
////                    "purpose" to purpose,
////                    "organization" to organization,
////                    "numberOfVisitors" to numberOfVisitors,
////                    "entryTime" to System.currentTimeMillis(),
////                    "status" to "pending",
////                    "createdAt" to System.currentTimeMillis()
////                )
////
////                firestore.collection("visitors")
////                    .document(visitorId)
////                    .set(visitor)
////                    .await()
////
////                // Step 3: Create notification for resident
////                createNotification(
////                    userId = residentId,
////                    title = "New Visitor Request",
////                    message = "$visitorName is at the gate for flat $flatNumber",
////                    type = "visitor_request",
////                    targetId = visitorId
////                )
////
////                // Refresh data
////                loadTodayStats()
////                loadRecentVisitors()
////
////                onSuccess()
////
////            } catch (e: Exception) {
////                Log.e(TAG, "Error creating visitor request", e)
////                onError(e.message ?: "Error creating request")
////            }
////        }
////    }
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val currentUser = auth.currentUser ?: throw Exception("Not logged in")
//
//                // Get watchman details
//                val watchmanDoc = firestore.collection("watchman")
//                    .whereEqualTo("watchmanId", currentUser.uid)
//                    .get()
//                    .await()
//
//                val watchmanName = watchmanDoc.documents.firstOrNull()?.getString("name") ?: "Watchman"
//                val societyId = watchmanDoc.documents.firstOrNull()?.getString("societyId") ?: ""
//
//                // Generate unique visitor ID
//                val visitorId = firestore.collection("visitors").document().id
//
//                // ✅ CRITICAL FIX: Store the correct residentId (Auth UID)
//                Log.d(TAG, "Creating visitor with residentId (Auth UID): $residentId")
//                Log.d(TAG, "Resident name: $residentName")
//
//                val visitor = hashMapOf(
//                    "visitorId" to visitorId,
//                    "visitorName" to visitorName,
//                    "visitorPhone" to visitorPhone,
//                    "flatNumber" to flatNumber,
//                    "purpose" to purpose,
//                    "organization" to organization,
//                    "numberOfVisitors" to numberOfVisitors,
//                    "vehicleNumber" to vehicleNumber,
//                    "notes" to notes,
//                    "visitorPhotoUrl" to photoUrl,
//
//                    // 🔥 CRITICAL: This MUST be the Firebase Auth UID
//                    "residentId" to residentId,      // ✅ Must match logged-in resident's UID
//                    "residentName" to residentName,
//                    "societyId" to societyId,
//
//                    "watchmanId" to currentUser.uid,
//                    "watchmanName" to watchmanName,
//
//                    "status" to "pending",
//                    "entryTime" to Timestamp.now(),
//                    "createdAt" to Timestamp.now(),
//                    "updatedAt" to Timestamp.now()
//                )
//
//                firestore.collection("visitors")
//                    .document(visitorId)
//                    .set(visitor)
//                    .await()
//
//                Log.d(TAG, "Visitor request created successfully with ID: $visitorId")
//
//                _isLoading.value = false
//                onSuccess()
//
//            } catch (e: Exception) {
//                Log.e(TAG, "Error creating visitor request", e)
//                _isLoading.value = false
//                onError(e.message ?: "Unknown error")
//            }
//        }
//    }

// WatchmanViewModel.kt - Add this debug function

    /**
     * Debug function to check Firestore data
     * Call this from your screen to verify data
     */
    fun debugCheckFirestoreData() {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                Log.d("FLAT_DEBUG", "=== DEBUG START ===")
                Log.d("FLAT_DEBUG", "Current User UID: ${currentUser?.uid}")

                // Check user document
                if (currentUser != null) {
                    val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
                    Log.d("FLAT_DEBUG", "User societyId: ${userDoc.getString("societyId")}")
                    Log.d("FLAT_DEBUG", "User role: ${userDoc.getString("role")}")
                    Log.d("FLAT_DEBUG", "User name: ${userDoc.getString("fullName")}")
                }

                // Check all flats (no filter)
                val allFlats = firestore.collection("flats").get().await()
                Log.d("FLAT_DEBUG", "Total flats in DB: ${allFlats.size()}")
                allFlats.documents.forEach { doc ->
                    Log.d("FLAT_DEBUG", "Flat ${doc.getString("flatNumber")} - societyId: ${doc.getString("societyId")}, occupied: ${doc.getBoolean("occupied")}")
                }

                // Check flats with current societyId
                val societyId = _societyId.value
                if (societyId.isNotEmpty()) {
                    val filteredFlats = firestore.collection("flats")
                        .whereEqualTo("societyId", societyId)
                        .get()
                        .await()
                    Log.d("FLAT_DEBUG", "Flats with societyId '$societyId': ${filteredFlats.size()}")
                }

                Log.d("FLAT_DEBUG", "=== DEBUG END ===")
            } catch (e: Exception) {
                Log.e("FLAT_DEBUG", "Debug error", e)
            }
        }
    }
    /**
     * 🔥 CRITICAL FIX: Initialize watchman with society ID
     */
// Add this function to WatchmanViewModel.kt if not already present
    fun initializeWatchman(societyId: String) {
        if (_societyId.value == societyId) return

        _societyId.value = societyId
        loadUserData()
        loadFlats()
        loadTodayStats()
        loadRecentVisitors()
        loadVisitorHistory()
        loadPreApprovedGuests()
    }
    /**
     * 🔥 FIX: Real-time visitor updates listener
     */
    fun listenToVisitorUpdates() {
        firestore.collection("visitors")
            .whereEqualTo("societyId", _societyId.value)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error listening to visitor updates", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    loadRecentVisitors()
                    loadVisitorHistory()
                    loadTodayStats()
                    loadPendingVisitorsCount()
                }
            }
    }

    private fun loadPendingVisitorsCount() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("visitors")
                    .whereEqualTo("status", "pending")
                    .get()
                    .await()
                _pendingVisitorsCount.value = snapshot.size()
            } catch (e: Exception) {
                Log.e(TAG, "Error loading pending visitors count", e)
            }
        }
    }
    /**
     * Initialize view model with society ID
     */
// WatchmanViewModel.kt - Update initialize function

    fun initialize(societyId: String) {
        Log.d("FLAT_DEBUG", "initialize called with societyId: $societyId")
        _societyId.value = societyId
        loadUserData()
        loadFlats()  // This will now work with correct societyId
        loadTodayStats()
        loadRecentVisitors()
        loadVisitorHistory()
        loadPreApprovedGuests()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser ?: return@launch
                val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
                if (userDoc.exists()) {
                    _userName.value = userDoc.getString("fullName") ?: "Watchman"
                    _societyName.value = userDoc.getString("societyName") ?: ""
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading user data", e)
            }
        }
    }

    /**
     * Upload image to Cloudinary
     */
// Optional: Simulate progress for better UX
    fun uploadImageToCloudinaryWithProgress(
        context: Context,
        imageUri: Uri,
        onProgress: (Int) -> Unit,
        onComplete: (String?) -> Unit
    ) {
        _isLoading.value = true

        // Simulate progress while uploading
        viewModelScope.launch {
            var progress = 0
            while (progress < 90) {
                delay(50)
                progress += 5
                _uploadProgress.value = progress
                onProgress(progress)
            }
        }

        CloudinaryHelper.uploadImageFromUri(
            context = context,
            imageUri = imageUri,
            callback = object : UploadCallback {
                override fun onSuccess(url: String) {
                    _isLoading.value = false
                    _uploadProgress.value = 100
                    onProgress(100)
                    onComplete(url)
                }

                override fun onError(error: String) {
                    Log.e(TAG, "Cloudinary upload error: $error")
                    _isLoading.value = false
                    _errorMessage.value = error
                    onComplete(null)
                }
            }
        )
    }
    /**
     * Upload image to Cloudinary using URI (Working 100%)
     */
// Add this function to your existing WatchmanViewModel class

    /**
     * Upload image to Cloudinary using URI
     */
    fun uploadImageToCloudinary(
        context: Context,
        imageUri: Uri,
        onComplete: (String?) -> Unit
    ) {
        _isLoading.value = true
        _uploadProgress.value = 0

        // Simulate progress
        viewModelScope.launch {
            var progress = 0
            while (progress < 90) {
                delay(50)
                progress += 5
                _uploadProgress.value = progress
            }
        }

        CloudinaryHelper.uploadImageFromUri(
            context = context,
            imageUri = imageUri,
            callback = object : UploadCallback {
                override fun onSuccess(url: String) {
                    _isLoading.value = false
                    _uploadProgress.value = 100
                    onComplete(url)
                }

                override fun onError(error: String) {
                    Log.e(TAG, "Cloudinary upload error: $error")
                    _isLoading.value = false
                    _errorMessage.value = error
                    onComplete(null)
                }
            }
        )
    }

    /**
     * Load flats
     */
    fun loadFlats() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = auth.currentUser
                if (currentUser == null) {
                    Log.e(TAG, "No current user")
                    _isLoading.value = false
                    return@launch
                }

                // Get societyId from user document
                val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
                val societyId = userDoc.getString("societyId") ?: ""

                // ✅ DEBUG LOG - Check what societyId we're using
                Log.d("FLAT_DEBUG", "Watchman SocietyId: $societyId")
                Log.d("FLAT_DEBUG", "Current User ID: ${currentUser.uid}")

                if (societyId.isEmpty()) {
                    Log.e(TAG, "No society ID found for watchman")
                    _isLoading.value = false
                    return@launch
                }

                _societyId.value = societyId

                // Query flats by societyId
                val snapshot = firestore.collection("flats")
                    .whereEqualTo("societyId", societyId)
                    .get()
                    .await()

                // ✅ DEBUG LOG - Check how many flats found
                Log.d("FLAT_DEBUG", "Found ${snapshot.size()} flats for societyId: $societyId")

                val flatList = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null

                    // ✅ FIX: Handle both "occupied" and "isOccupied" field names
                    val isOccupied = when {
                        data.containsKey("occupied") -> data["occupied"] as? Boolean ?: false
                        data.containsKey("isOccupied") -> data["isOccupied"] as? Boolean ?: false
                        else -> false
                    }

                    // Get owner/resident name
                    val ownerName = when {
                        data.containsKey("residentName") -> data["residentName"] as? String ?: ""
                        data.containsKey("ownerName") -> data["ownerName"] as? String ?: ""
                        else -> "Vacant"
                    }

                    // Get phone number
                    val phone = when {
                        data.containsKey("residentPhone") -> data["residentPhone"] as? String ?: ""
                        data.containsKey("ownerPhone") -> data["ownerPhone"] as? String ?: ""
                        else -> ""
                    }

                    // Get resident ID
                    val ownerId = when {
                        data.containsKey("residentId") -> data["residentId"] as? String ?: ""
                        data.containsKey("ownerId") -> data["ownerId"] as? String ?: ""
                        else -> ""
                    }

                    FlatOwner(
                        flatId = doc.id,
                        flatNumber = data["flatNumber"] as? String ?: "",
                        ownerName = ownerName,
                        ownerId = ownerId,
                        phone = phone,
                        isOccupied = isOccupied
                    )
                }

                // Sort flats by flat number
                _flats.value = flatList.sortedBy { parseFlatNumber(it.flatNumber) }

                // ✅ DEBUG LOG - Check final list
                Log.d("FLAT_DEBUG", "Final flats list size: ${_flats.value.size}")
                _flats.value.forEach { flat ->
                    Log.d("FLAT_DEBUG", "Flat: ${flat.flatNumber} - Occupied: ${flat.isOccupied}")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error loading flats", e)
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    // Helper function to parse flat numbers for sorting
    private fun parseFlatNumber(flatNumber: String): String {
        // Extract numeric part for sorting (e.g., "A-101" -> "00101")
        val match = Regex("""(\d+)""").find(flatNumber)
        val numeric = match?.value?.toIntOrNull() ?: 0
        // Return padded numeric for proper sorting
        return numeric.toString().padStart(5, '0')
    }
    /**
     * Create visitor request
     */
//    fun createVisitorRequest(
//        visitorName: String,
//        visitorPhone: String,
//        purpose: String,
//        organization: String,
//        numberOfVisitors: Int,
//        vehicleNumber: String,
//        notes: String,
//        flatNumber: String,
//        photoUrl: String,
//        onComplete: (Boolean, String?) -> Unit
//    )
//    {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val currentUser = auth.currentUser ?: throw Exception("Not logged in")
//
//                // Get resident ID
//                val flatSnapshot = firestore.collection("flats")
//                    .whereEqualTo("societyId", _societyId.value)
//                    .whereEqualTo("flatNumber", flatNumber)
//                    .get()
//                    .await()
//
//                if (flatSnapshot.isEmpty) {
//                    _isLoading.value = false
//                    onComplete(false, "Flat not found")
//                    return@launch
//                }
//
//                val flatData = flatSnapshot.documents.first()
//                val residentId = flatData.getString("residentId") ?: ""
//                if (residentId.isEmpty()) {
//                    _isLoading.value = false
//                    onComplete(false, "No resident assigned")
//                    return@launch
//                }
//
//                // Create visitor
//                val visitorId = firestore.collection("visitors").document().id
//                val visitor = Visitor(
//                    visitorId = visitorId,
//                    visitorName = visitorName,
//                    visitorPhone = visitorPhone,
//                    visitorPhotoUrl = photoUrl,
//                    flatNumber = flatNumber,
//                    residentId = residentId,
//                    watchmanId = currentUser.uid,
//                    watchmanName = _userName.value,
//                    purpose = purpose,
//                    organization = organization,
//                    numberOfVisitors = numberOfVisitors,
//                    vehicleNumber = vehicleNumber,
//                    entryTime = Timestamp.now(),
//                    status = "pending",
//                    notes = notes,
//                    createdAt = Timestamp.now()
//                )
//
//                firestore.collection("visitors").document(visitorId).set(visitor).await()
//
//                // Notify resident
//                createNotification(
//                    userId = residentId,
//                    title = "New Visitor Request",
//                    message = "$visitorName is at the gate",
//                    type = "visitor_request",
//                    targetId = visitorId
//                )
//
//                // Refresh data
//                loadTodayStats()
//                loadRecentVisitors()
//
//                _isLoading.value = false
//                onComplete(true, visitorId)
//
//            } catch (e: Exception) {
//                Log.e(TAG, "Error creating visitor", e)
//                _isLoading.value = false
//                onComplete(false, e.message)
//            }
//        }
//    }

    /**
     * Load today's stats
     */
    fun loadTodayStats() {
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val startOfDay = Timestamp(calendar.time)

                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                val endOfDay = Timestamp(calendar.time)

                val todaySnapshot = firestore.collection("visitors")
                    .whereGreaterThanOrEqualTo("entryTime", startOfDay)
                    .whereLessThanOrEqualTo("entryTime", endOfDay)
                    .get()
                    .await()

                _todayVisitorsCount.value = todaySnapshot.size()

                val pendingSnapshot = firestore.collection("visitors")
                    .whereEqualTo("status", "pending")
                    .get()
                    .await()

                _pendingVisitorsCount.value = pendingSnapshot.size()

            } catch (e: Exception) {
                Log.e(TAG, "Error loading stats", e)
            }
        }
    }

    /**
     * Load recent visitors
     */
    fun loadRecentVisitors() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("visitors")
                    .orderBy("entryTime", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .await()

                _recentVisitors.value = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    VisitorItem(
                        id = doc.id,
                        name = data["visitorName"] as? String ?: "",
                        flat = data["flatNumber"] as? String ?: "",
                        time = formatTime(data["entryTime"] as? Timestamp),
                        status = data["status"] as? String ?: "pending",
                        photoUrl = data["visitorPhotoUrl"] as? String ?: "",
                        purpose = data["purpose"] as? String ?: "",
                        entryTime = data["entryTime"] as? Timestamp
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading recent visitors", e)
            }
        }
    }

    /**
     * Load visitor history
     */
    fun loadVisitorHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = firestore.collection("visitors")
                    .orderBy("entryTime", Query.Direction.DESCENDING)
                    .limit(100)
                    .get()
                    .await()

                _visitorHistory.value = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    VisitorItem(
                        id = doc.id,
                        name = data["visitorName"] as? String ?: "",
                        flat = data["flatNumber"] as? String ?: "",
                        time = formatTime(data["entryTime"] as? Timestamp),
                        status = data["status"] as? String ?: "pending",
                        photoUrl = data["visitorPhotoUrl"] as? String ?: "",
                        purpose = data["purpose"] as? String ?: "",
                        entryTime = data["entryTime"] as? Timestamp
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading history", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load pre-approved guests
     */
    fun loadPreApprovedGuests() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = firestore.collection("preApprovedGuests")
                    .whereEqualTo("isActive", true)
                    .get()
                    .await()

                _preApprovedGuests.value = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    PreApprovedGuestItem(
                        id = doc.id,
                        guestName = data["guestName"] as? String ?: "",
                        flatNumber = data["flatNumber"] as? String ?: "",
                        residentId = data["residentId"] as? String ?: "",
                        schedule = data["schedule"] as? String ?: "",
                        purpose = data["purpose"] as? String ?: "",
                        isValid = isGuestValid(data)
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading pre-approved guests", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Check if guest is currently valid based on schedule and dates
     */
    private fun isGuestValid(data: Map<String, Any>): Boolean {
        // Check if guest is active
        val isActive = data["isActive"] as? Boolean ?: true
        if (!isActive) return false

        // Check validity dates
        val validFrom = data["validFrom"] as? Timestamp
        val validUntil = data["validUntil"] as? Timestamp
        val now = Timestamp.now()

        if (validFrom != null && validUntil != null) {
            return now >= validFrom && now <= validUntil
        }

        return true
    }

    /**
     * Send emergency alert
     */
    fun sendEmergencyAlert(message: String, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = auth.currentUser ?: throw Exception("Not logged in")

                val emergencyId = firestore.collection("emergency").document().id
                val emergency = Emergency(
                    emergencyId = emergencyId,
                    senderId = currentUser.uid,
                    senderName = _userName.value,
                    message = message,
                    societyId = _societyId.value,
                    status = "active",
                    createdAt = Timestamp.now()
                )

                firestore.collection("emergency").document(emergencyId).set(emergency).await()

                // Notify all society members
                val usersSnapshot = firestore.collection("users")
                    .whereEqualTo("societyId", _societyId.value)
                    .get()
                    .await()

                usersSnapshot.documents.forEach { userDoc ->
                    createNotification(
                        userId = userDoc.id,
                        title = "🚨 EMERGENCY ALERT",
                        message = message.take(100),
                        type = "emergency",
                        targetId = emergencyId
                    )
                }

                _isLoading.value = false
                onComplete(true, emergencyId)

            } catch (e: Exception) {
                Log.e(TAG, "Error sending emergency", e)
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
            firestore.collection("notifications").document(notification.notificationId).set(notification).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error creating notification", e)
        }
    }

    private fun formatTime(timestamp: Timestamp?): String {
        if (timestamp == null) return ""
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(timestamp.toDate())
    }

    fun clearError() {
        _errorMessage.value = null
    }
}