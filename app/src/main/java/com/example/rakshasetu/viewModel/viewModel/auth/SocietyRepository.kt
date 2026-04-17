package com.example.rakshasetu.viewModel.viewModel.auth


import com.example.rakshasetu.data.models.Flat
import com.example.rakshasetu.data.models.JoinRequest
import com.example.rakshasetu.data.models.Society
import com.example.rakshasetu.data.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class SocietyRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun createSociety(society: Society): Result<String> {
        return try {
            val docRef = firestore.collection("societies").document()
            val societyId = docRef.id
            firestore.collection("societies").document(societyId).set(society.copy(societyId = societyId)).await()
            Result.success(societyId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSociety(societyId: String): Society? {
        return try {
            val doc = firestore.collection("societies").document(societyId).get().await()
            doc.toObject(Society::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getSocietyByUser(userId: String): Society? {
        return try {
            val userDoc = firestore.collection("users").document(userId).get().await()
            val societyId = userDoc.getString("societyId") ?: return null
            getSociety(societyId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addFlats(flats: List<Flat>): Result<Unit> {
        return try {
            val batch = firestore.batch()
            flats.forEach { flat ->
                val docRef = firestore.collection("flats").document(flat.flatId)
                batch.set(docRef, flat)
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFlats(societyId: String): List<Flat> {
        return try {
            val snapshot = firestore.collection("flats")
                .whereEqualTo("societyId", societyId)
                .orderBy("blockNumber")
                .orderBy("flatNumber")
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Flat::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAvailableFlats(societyId: String, block: String): List<Flat> {
        return try {
            val snapshot = firestore.collection("flats")
                .whereEqualTo("societyId", societyId)
                .whereEqualTo("blockNumber", block)
                .whereEqualTo("isOccupied", false)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Flat::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getSocietyResidents(societyId: String): List<User> {
        return try {
            val snapshot = firestore.collection("users")
                .whereEqualTo("societyId", societyId)
                .whereEqualTo("role", "resident")
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getSocietyWatchmen(societyId: String): List<User> {
        return try {
            val snapshot = firestore.collection("users")
                .whereEqualTo("societyId", societyId)
                .whereEqualTo("role", "watchman")
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createJoinRequest(request: JoinRequest): Result<String> {
        return try {
            val docRef = firestore.collection("joinRequests").document()
            val requestId = docRef.id
            firestore.collection("joinRequests").document(requestId).set(request.copy(requestId = requestId)).await()
            Result.success(requestId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJoinRequests(societyId: String, status: String = "pending"): List<JoinRequest> {
        return try {
            val snapshot = firestore.collection("joinRequests")
                .whereEqualTo("societyId", societyId)
                .whereEqualTo("status", status)
                .orderBy("requestedAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(JoinRequest::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateJoinRequest(requestId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("joinRequests").document(requestId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchSocieties(query: String): List<Society> {
        return try {
            val snapshot = firestore.collection("societies")
                .whereGreaterThanOrEqualTo("societyName", query)
                .whereLessThanOrEqualTo("societyName", query + "\uf8ff")
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Society::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}