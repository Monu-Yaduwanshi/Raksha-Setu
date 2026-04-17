//package com.example.rakshasetu.viewModel.viewModel.auth
package com.example.rakshasetu.viewModel.viewModel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
//import java.sql.Timestamp
import com.google.firebase.Timestamp
// Data class for User
data class User(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "", // "secretary", "watchman", "resident"
    val societyId: String = "",
    val societyName: String = "",
    val flatNumber: String = "",
    val blockNumber: String = "",
    val profileImageUrl: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLogin: Long = 0,
    val fcmToken: String = "",
    val createdBy: String = "" // Secretary who created this account
) {
    // Convert to Map for Firestore
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "userId" to userId,
            "fullName" to fullName,
            "email" to email,
            "phone" to phone,
            "role" to role,
            "societyId" to societyId,
            "societyName" to societyName,
            "flatNumber" to flatNumber,
            "blockNumber" to blockNumber,
            "profileImageUrl" to profileImageUrl,
            "isActive" to isActive,
            "createdAt" to createdAt,
            "lastLogin" to lastLogin,
            "fcmToken" to fcmToken,
            "createdBy" to createdBy
        )
        return map
    }

    companion object {
        // Create from Firestore document
        fun fromFirestore(document: DocumentSnapshot): User {
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
    }
}

// Data class for Society

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
//    val createdAt: Long = System.currentTimeMillis()
//)

class AuthViewModel : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    // Firebase instances
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // State flows for UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        Log.d(TAG, "AuthViewModel initialized")
        if (auth.currentUser != null) {
            Log.d(TAG, "User already logged in: ${auth.currentUser?.uid}")
            viewModelScope.launch {
                ensureUserDocumentExists(auth.currentUser!!.uid)
                fetchUserData(auth.currentUser!!.uid)
            }
        }
    }

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Get current user data
     */
    fun getCurrentUserData(): User? {
        return _userData.value
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * ==================== SIGN IN ====================
     * Sign in with email, password and verify role matches selected role
     */
    fun signIn(
        email: String,
        password: String,
        selectedRole: String,
        navController: NavController,
        showToast: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Attempting sign in for email: $email with selected role: $selectedRole")

            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    Log.d(TAG, "Sign in successful for user: ${firebaseUser.uid}")

                    val userExists = ensureUserDocumentExists(firebaseUser.uid)

                    if (userExists) {
                        fetchUserData(firebaseUser.uid)
                        delay(500)

                        val actualRole = _userData.value?.role

                        if (actualRole == selectedRole) {
                            updateFCMToken(firebaseUser.uid)
                            _currentUser.value = firebaseUser
                            _isUserLoggedIn.value = true
                            showToast("Login successful")
                            navigateBasedOnRole(navController)
                        } else {
                            Log.e(TAG, "Role mismatch: Selected=$selectedRole, Actual=$actualRole")
                            auth.signOut()
                            showToast("Invalid role selected. You are registered as $actualRole")
                        }
                    } else {
                        showToast("Account not properly configured. Contact secretary.")
                    }
                } else {
                    showToast("Login failed: Unknown error")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Sign in error", e)
                showToast("Login failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Simplified sign in without role selection (for existing users)
     */
    fun signIn(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    ensureUserDocumentExists(firebaseUser.uid)
                    fetchUserData(firebaseUser.uid)
                    updateFCMToken(firebaseUser.uid)
                    _currentUser.value = firebaseUser
                    _isUserLoggedIn.value = true
                    _isLoading.value = false
                    onResult(true, null)
                } else {
                    _isLoading.value = false
                    onResult(false, "Login failed")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Sign in error", e)
                _isLoading.value = false
                onResult(false, e.message)
            }
        }
    }

    /**
     * ==================== SECRETARY SIGN UP ====================
     */
    fun secretarySignUp(
        fullName: String,
        email: String,
        password: String,
        phone: String,
        societyName: String,
        societyAddress: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Attempting secretary sign up for email: $email")

            try {
                // 🔐 STEP 1: Create Auth User
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: throw Exception("User creation failed")

                Log.d(TAG, "Auth user created: $userId")

                // 📌 STEP 2: Generate Society ID
                val societyId = firestore.collection("societies").document().id

                // 🏢 STEP 3: Create Society (MAIN COLLECTION)
                val societyData = hashMapOf(
                    "societyId" to societyId,
                    "societyName" to societyName,
                    "address" to societyAddress,
                    "createdBy" to userId,
                    "createdAt" to Timestamp.now(), // ✅ FIXED
                    "totalFlats" to 0,
                    "totalBlocks" to 0,
                    "blocks" to emptyList<String>()
                )

                firestore.collection("societies")
                    .document(societyId)
                    .set(societyData)
                    .await()

                Log.d(TAG, "Society created: $societyId")

                // 🧾 STEP 4: SAVE REGISTRATION COPY (NEW COLLECTION ✅)
                val registrationData = hashMapOf(
                    "userId" to userId,
                    "fullName" to fullName,
                    "email" to email,
                    "phone" to phone,
                    "societyName" to societyName,
                    "societyAddress" to societyAddress,
                    "registeredAt" to Timestamp.now()
                )

                firestore.collection("registrations")
                    .document(userId)
                    .set(registrationData)
                    .await()

                Log.d(TAG, "Registration data saved separately")

                // 🔔 STEP 5: Get FCM Token
                val fcmToken = try {
                    FirebaseMessaging.getInstance().token.await()
                } catch (e: Exception) {
                    ""
                }

                // 👤 STEP 6: Create User
                val user = User(
                    userId = userId,
                    fullName = fullName,
                    email = email,
                    phone = phone,
                    role = "secretary",
                    societyId = societyId,
                    societyName = societyName,
                    createdAt = System.currentTimeMillis(),
                    fcmToken = fcmToken,
                    isActive = true
                )

                firestore.collection("users")
                    .document(userId)
                    .set(user.toMap())
                    .await()

                Log.d(TAG, "User document created")

                // ✅ STEP 7: Update State
                _currentUser.value = authResult.user
                _userData.value = user
                _userRole.value = "secretary"
                _isUserLoggedIn.value = true

                onComplete(true, null)

            } catch (e: Exception) {
                Log.e(TAG, "Secretary sign up error", e)
                onComplete(false, e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }
//    fun secretarySignUp(
//        fullName: String,
//        email: String,
//        password: String,
//        phone: String,
//        societyName: String,
//        societyAddress: String,
//        onComplete: (Boolean, String?) -> Unit
//    ) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            Log.d(TAG, "Attempting secretary sign up for email: $email")
//
//            try {
//                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
//                val userId = authResult.user?.uid ?: throw Exception("User creation failed")
//                Log.d(TAG, "Auth user created: $userId")
//
//                val societyId = firestore.collection("societies").document().id
//                val societyData = mutableMapOf<String, Any>(
//                    "societyId" to societyId,
//                    "societyName" to societyName,
//                    "address" to societyAddress,
//                    "createdBy" to userId,
//                    "createdAt" to System.currentTimeMillis(),
//                    "totalFlats" to 0,
//                    "totalBlocks" to 0,
//                    "blocks" to emptyList<String>()
//                )
//
//                firestore.collection("societies").document(societyId).set(societyData).await()
//                Log.d(TAG, "Society created: $societyId")
//
//                val fcmToken = try {
//                    FirebaseMessaging.getInstance().token.await()
//                } catch (e: Exception) {
//                    Log.e(TAG, "Error getting FCM token", e)
//                    ""
//                }
//
//                val user = User(
//                    userId = userId,
//                    fullName = fullName,
//                    email = email,
//                    phone = phone,
//                    role = "secretary",
//                    societyId = societyId,
//                    societyName = societyName,
//                    createdAt = System.currentTimeMillis(),
//                    fcmToken = fcmToken,
//                    isActive = true
//                )
//
//                firestore.collection("users").document(userId).set(user.toMap()).await()
//                Log.d(TAG, "User document created for secretary")
//
//                _currentUser.value = authResult.user
//                _userData.value = user
//                _userRole.value = "secretary"
//                _isUserLoggedIn.value = true
//
//                onComplete(true, null)
//
//            } catch (e: Exception) {
//                Log.e(TAG, "Secretary sign up error", e)
//                onComplete(false, e.message)
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    /**
     * General sign up for any role (for residents/watchmen created by secretary)
     */
    fun signUp(
        fullName: String,
        email: String,
        password: String,
        phone: String,
        role: String,
        societyId: String = "",
        societyName: String = "",
        flatNumber: String = "",
        blockNumber: String = "",
        createdBy: String = "",
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: throw Exception("User creation failed")

                val fcmToken = try {
                    FirebaseMessaging.getInstance().token.await()
                } catch (e: Exception) {
                    ""
                }

                val user = User(
                    userId = userId,
                    fullName = fullName,
                    email = email,
                    phone = phone,
                    role = role,
                    societyId = societyId,
                    societyName = societyName,
                    flatNumber = flatNumber,
                    blockNumber = blockNumber,
                    createdAt = System.currentTimeMillis(),
                    createdBy = createdBy,
                    fcmToken = fcmToken,
                    isActive = true
                )

                firestore.collection("users").document(userId).set(user.toMap()).await()

                if (role == "resident" && flatNumber.isNotBlank() && blockNumber.isNotBlank()) {
                    val flatData = mutableMapOf<String, Any>(
                        "flatNumber" to flatNumber,
                        "blockNumber" to blockNumber,
                        "societyId" to societyId,
                        "residentId" to userId,
                        "residentName" to fullName,
                        "residentPhone" to phone,
                        "isOccupied" to true,
                        "createdAt" to System.currentTimeMillis()
                    )
                    firestore.collection("flats").document("${societyId}_${blockNumber}_${flatNumber}")
                        .set(flatData)
                        .await()
                }

                if (role == "watchman") {
                    val watchmanData = mutableMapOf<String, Any>(
                        "watchmanId" to userId,
                        "name" to fullName,
                        "phone" to phone,
                        "societyId" to societyId,
                        "shift" to "morning",
                        "isActive" to true,
                        "joinedAt" to System.currentTimeMillis()
                    )
                    firestore.collection("watchmen").document(userId).set(watchmanData).await()
                }

                _isLoading.value = false
                onComplete(true, userId)

            } catch (e: Exception) {
                Log.e(TAG, "Sign up error", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }

    /**
     * ==================== CREATE RESIDENT ====================
     */
    fun createResident(
        fullName: String,
        email: String,
        password: String,
        phone: String,
        flatNumber: String,
        blockNumber: String,
        societyId: String,
        societyName: String,
        createdBy: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Creating resident: $email")

            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: throw Exception("User creation failed")
                Log.d(TAG, "Auth user created for resident: $userId")

                val fcmToken = try {
                    FirebaseMessaging.getInstance().token.await()
                } catch (e: Exception) {
                    ""
                }

                val user = User(
                    userId = userId,
                    fullName = fullName,
                    email = email,
                    phone = phone,
                    role = "resident",
                    societyId = societyId,
                    societyName = societyName,
                    flatNumber = flatNumber,
                    blockNumber = blockNumber,
                    createdAt = System.currentTimeMillis(),
                    createdBy = createdBy,
                    fcmToken = fcmToken,
                    isActive = true
                )

                firestore.collection("users").document(userId).set(user.toMap()).await()
                Log.d(TAG, "Resident user document created")

                val flatData = mutableMapOf<String, Any>(
                    "flatNumber" to flatNumber,
                    "blockNumber" to blockNumber,
                    "societyId" to societyId,
                    "residentId" to userId,
                    "residentName" to fullName,
                    "residentPhone" to phone,
                    "occupied" to true,
                    //"Occupied" to true,
                    "createdAt" to System.currentTimeMillis()
                )

                firestore.collection("flats").document("${societyId}_${blockNumber}_${flatNumber}")
                    .set(flatData)
                    .await()
                Log.d(TAG, "Flat document updated")

                onComplete(true, userId)

            } catch (e: Exception) {
                Log.e(TAG, "Create resident error", e)
                onComplete(false, e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * ==================== CREATE WATCHMAN ====================
     */
    fun createWatchman(
        fullName: String,
        email: String,
        password: String,
        phone: String,
        societyId: String,
        societyName: String,
        shift: String,
        createdBy: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Creating watchman: $email")

            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: throw Exception("User creation failed")
                Log.d(TAG, "Auth user created for watchman: $userId")

                val fcmToken = try {
                    FirebaseMessaging.getInstance().token.await()
                } catch (e: Exception) {
                    ""
                }

                val user = User(
                    userId = userId,
                    fullName = fullName,
                    email = email,
                    phone = phone,
                    role = "watchman",
                    societyId = societyId,
                    societyName = societyName,
                    createdAt = System.currentTimeMillis(),
                    createdBy = createdBy,
                    fcmToken = fcmToken,
                    isActive = true
                )

                firestore.collection("users").document(userId).set(user.toMap()).await()
                Log.d(TAG, "Watchman user document created")

                val watchmanData = mutableMapOf<String, Any>(
                    "watchmanId" to userId,
                    "name" to fullName,
                    "phone" to phone,
                    "societyId" to societyId,
                    "shift" to shift,
                    "isActive" to true,
                    "joinedAt" to System.currentTimeMillis()
                )

                firestore.collection("watchmen").document(userId).set(watchmanData).await()
                Log.d(TAG, "Watchman added to watchmen collection")

                onComplete(true, userId)

            } catch (e: Exception) {
                Log.e(TAG, "Create watchman error", e)
                onComplete(false, e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * ==================== UPDATE USER PROFILE ====================
     */
    fun updateUserProfile(
        userId: String,
        updates: Map<String, Any>,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestore.collection("users").document(userId)
                    .update(updates)
                    .await()

                fetchUserData(userId)
                _isLoading.value = false
                onComplete(true, null)
            } catch (e: Exception) {
                Log.e(TAG, "Update profile error", e)
                _isLoading.value = false
                onComplete(false, e.message)
            }
        }
    }

    /**
     * ==================== GET SOCIETY BY ID ====================
     */
    fun getSocietyById(
        societyId: String,
        onResult: (Society?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("societies").document(societyId).get().await()
                if (snapshot.exists()) {
                    val data = snapshot.data ?: return@launch
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
                       // createdAt = data["createdAt"] as? Long ?: System.currentTimeMillis()
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
     * ==================== SEARCH SOCIETIES ====================
     */
    fun searchSocieties(
        query: String,
        onResult: (List<Society>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("societies")
                    .whereGreaterThanOrEqualTo("societyName", query)
                    .whereLessThanOrEqualTo("societyName", query + "\uf8ff")
                    .get()
                    .await()

                val societies = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    Society(
                        societyId = doc.id,
                        societyName = data["societyName"] as? String ?: "",
                        address = data["address"] as? String ?: "",
                        city = data["city"] as? String ?: "",
                        pincode = data["pincode"] as? String ?: "",
                        totalFlats = (data["totalFlats"] as? Long)?.toInt() ?: 0,
                        totalBlocks = (data["totalBlocks"] as? Long)?.toInt() ?: 0,
                        blocks = data["blocks"] as? List<String> ?: emptyList(),
                        createdBy = data["createdBy"] as? String ?: "",
                      //  createdAt = data["createdAt"] as? Long ?: System.currentTimeMillis()
                        createdAt = data["createdAt"] as? Timestamp
                    )
                }
                onResult(societies)
            } catch (e: Exception) {
                Log.e(TAG, "Error searching societies", e)
                onResult(emptyList())
            }
        }
    }

    /**
     * ==================== ENSURE USER DOCUMENT EXISTS ====================
     */
    private suspend fun ensureUserDocumentExists(userId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val docRef = firestore.collection("users").document(userId)
                val snapshot = docRef.get().await()

                if (!snapshot.exists()) {
                    Log.w(TAG, "User document not found for $userId, creating minimal document")

                    val minimalUser = mutableMapOf<String, Any>(
                        "userId" to userId,
                        "email" to (auth.currentUser?.email ?: ""),
                        "fullName" to (auth.currentUser?.displayName ?: "User"),
                        "role" to "resident",
                        "isActive" to true,
                        "createdAt" to System.currentTimeMillis(),
                        "lastLogin" to System.currentTimeMillis()
                    )

                    docRef.set(minimalUser).await()
                    Log.d(TAG, "Created minimal user document for $userId")
                }
                true
            } catch (e: Exception) {
                Log.e(TAG, "Error ensuring user document exists", e)
                false
            }
        }
    }

    /**
     * ==================== UPDATE FCM TOKEN ====================
     */
    private suspend fun updateFCMToken(userId: String) {
        withContext(Dispatchers.IO) {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                Log.d(TAG, "FCM token obtained: $token")

                val userDoc = firestore.collection("users").document(userId)
                val snapshot = userDoc.get().await()

                if (snapshot.exists()) {
                    userDoc.update(
                        mapOf(
                            "fcmToken" to token,
                            "lastLogin" to System.currentTimeMillis()
                        )
                    ).await()
                    Log.d(TAG, "FCM token updated for $userId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating FCM token", e)
            }
        }
    }

    /**
     * ==================== FETCH USER DATA ====================
     */
    private fun fetchUserData(userId: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Fetching user data for $userId")
                val document = firestore.collection("users").document(userId).get().await()

                if (document.exists()) {
                    val user = User.fromFirestore(document)
                    _userData.value = user
                    _userRole.value = user.role
                    Log.d(TAG, "User data fetched. Role: ${user.role}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching user data", e)
            }
        }
    }
    fun refreshUserData() {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                fetchUserData(currentUser.uid)
            }
        }
    }
    /**
     * ==================== SIGN OUT ====================
     */
    fun signOut(onComplete: () -> Unit = {}) {
        auth.signOut()
        _isUserLoggedIn.value = false
        _userRole.value = null
        _currentUser.value = null
        _userData.value = null
        Log.d(TAG, "User signed out")
        onComplete()
    }

    /**
     * ==================== NAVIGATE BASED ON ROLE ====================
     */
    fun navigateBasedOnRole(navController: NavController) {
        when (_userRole.value) {
            "secretary" -> {
                Log.d(TAG, "Navigating to Secretary Home")
                navController.navigate(Screen.SecretaryHome.route) {
                    popUpTo(Screen.SignIn.route) { inclusive = true }
                }
            }
            "watchman" -> {
                Log.d(TAG, "Navigating to Watchman Home")
                navController.navigate(Screen.WatchmanHome.route) {
                    popUpTo(Screen.SignIn.route) { inclusive = true }
                }
            }
            "resident" -> {
                Log.d(TAG, "Navigating to Resident Home")
                navController.navigate(Screen.ResidentHome.route) {
                    popUpTo(Screen.SignIn.route) { inclusive = true }
                }
            }
            else -> {
                Log.w(TAG, "Unknown role, navigating to Sign In")
                navController.navigate(Screen.SignIn.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    /**
     * ==================== GET SOCIETY RESIDENTS ====================
     */
    fun getSocietyResidents(
        societyId: String,
        onResult: (List<User>?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users")
                    .whereEqualTo("societyId", societyId)
                    .whereEqualTo("role", "resident")
                    .get()
                    .await()

                val residents = snapshot.documents.mapNotNull { User.fromFirestore(it) }
                Log.d(TAG, "Found ${residents.size} residents")
                onResult(residents)
            } catch (e: Exception) {
                Log.e(TAG, "Error getting residents", e)
                onResult(null)
            }
        }
    }

    /**
     * ==================== GET SOCIETY WATCHMEN ====================
     */
    fun getSocietyWatchmen(
        societyId: String,
        onResult: (List<User>?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users")
                    .whereEqualTo("societyId", societyId)
                    .whereEqualTo("role", "watchman")
                    .get()
                    .await()

                val watchmen = snapshot.documents.mapNotNull { User.fromFirestore(it) }
                Log.d(TAG, "Found ${watchmen.size} watchmen")
                onResult(watchmen)
            } catch (e: Exception) {
                Log.e(TAG, "Error getting watchmen", e)
                onResult(null)
            }
        }
    }

    // Helper function for delay
    private suspend fun delay(timeMillis: Long) {
        kotlinx.coroutines.delay(timeMillis)
    }
}