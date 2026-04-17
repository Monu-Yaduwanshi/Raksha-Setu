package com.example.rakshasetu.viewModel.viewModel.auth


import android.util.Log
import com.example.rakshasetu.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { Result.success(it) } ?: Result.failure(Exception("Login failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(
        fullName: String,
        email: String,
        password: String,
        phone: String,
        role: String,
        societyId: String = "",
        societyName: String = "",
        flatNumber: String = "",
        blockNumber: String = "",
        createdBy: String = ""
    ): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("User creation failed")

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
                createdBy = createdBy
            )

            firestore.collection("users").document(userId).set(user).await()
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    suspend fun getUserData(userId: String): User? {
        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            doc.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserData(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users").document(userId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}