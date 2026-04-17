package com.example.rakshasetu.viewModel.viewModel.auth


import com.example.rakshasetu.data.models.Announcement
import com.example.rakshasetu.data.models.Emergency
import com.example.rakshasetu.data.models.Notification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class NotificationRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun createNotification(notification: Notification): Result<String> {
        return try {
            val docRef = firestore.collection("notifications").document()
            val notificationId = docRef.id
            firestore.collection("notifications").document(notificationId).set(notification.copy(notificationId = notificationId)).await()
            Result.success(notificationId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserNotifications(userId: String, limit: Int = 50): List<Notification> {
        return try {
            val snapshot = firestore.collection("notifications")
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Notification::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun markNotificationAsRead(notificationId: String): Result<Unit> {
        return try {
            firestore.collection("notifications").document(notificationId)
                .update("isRead", true)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAllNotificationsAsRead(userId: String): Result<Unit> {
        return try {
            val snapshot = firestore.collection("notifications")
                .whereEqualTo("userId", userId)
                .whereEqualTo("isRead", false)
                .get()
                .await()

            val batch = firestore.batch()
            snapshot.documents.forEach { doc ->
                batch.update(doc.reference, "isRead", true)
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createAnnouncement(announcement: Announcement): Result<String> {
        return try {
            val docRef = firestore.collection("announcements").document()
            val announcementId = docRef.id
            firestore.collection("announcements").document(announcementId).set(announcement.copy(announcementId = announcementId)).await()
            Result.success(announcementId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSocietyAnnouncements(societyId: String): List<Announcement> {
        return try {
            val snapshot = firestore.collection("announcements")
                .whereEqualTo("societyId", societyId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Announcement::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createEmergencyAlert(emergency: Emergency): Result<String> {
        return try {
            val docRef = firestore.collection("emergency").document()
            val emergencyId = docRef.id
            firestore.collection("emergency").document(emergencyId).set(emergency.copy(emergencyId = emergencyId)).await()
            Result.success(emergencyId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getActiveEmergencies(societyId: String): List<Emergency> {
        return try {
            val snapshot = firestore.collection("emergency")
                .whereEqualTo("societyId", societyId)
                .whereEqualTo("status", "active")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Emergency::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}