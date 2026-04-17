package com.example.rakshasetu.viewModel.viewModel.auth


import com.example.rakshasetu.data.models.Notification
import com.example.rakshasetu.data.models.Visitor
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class VisitorRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun createVisitor(visitor: Visitor): Result<String> {
        return try {
            val docRef = firestore.collection("visitors").document()
            val visitorId = docRef.id
            firestore.collection("visitors").document(visitorId).set(visitor.copy(visitorId = visitorId)).await()
            Result.success(visitorId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVisitor(visitorId: String): Visitor? {
        return try {
            val doc = firestore.collection("visitors").document(visitorId).get().await()
            doc.toObject(Visitor::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateVisitorStatus(visitorId: String, status: String): Result<Unit> {
        return try {
            firestore.collection("visitors").document(visitorId)
                .update(mapOf("status" to status, "exitTime" to Timestamp.now()))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPendingVisitorRequests(residentId: String): List<Visitor> {
        return try {
            val snapshot = firestore.collection("visitors")
                .whereEqualTo("residentId", residentId)
                .whereEqualTo("status", "pending")
                .orderBy("entryTime", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Visitor::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getVisitorHistory(residentId: String, limit: Int = 50): List<Visitor> {
        return try {
            val snapshot = firestore.collection("visitors")
                .whereEqualTo("residentId", residentId)
                .orderBy("entryTime", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Visitor::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getTodayVisitors(societyId: String): List<Visitor> {
        return try {
            val startOfDay = Timestamp.now()
            // Simplified - in production, calculate start of day properly
            val snapshot = firestore.collection("visitors")
                .whereGreaterThanOrEqualTo("entryTime", startOfDay)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Visitor::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getVisitorsByFlat(flatNumber: String, limit: Int = 20): List<Visitor> {
        return try {
            val snapshot = firestore.collection("visitors")
                .whereEqualTo("flatNumber", flatNumber)
                .orderBy("entryTime", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Visitor::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}