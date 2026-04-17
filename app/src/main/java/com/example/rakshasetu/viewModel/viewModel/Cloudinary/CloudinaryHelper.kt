package com.example.rakshasetu.viewModel.viewModel.Cloudinary

import android.content.Context
import android.net.Uri
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

interface UploadCallback {
    fun onSuccess(url: String)
    fun onError(error: String)
}

class CloudinaryHelper {
    companion object {
        private const val TAG = "Cloudinary"
        private const val CLOUDINARY_URL = "https://api.cloudinary.com/v1_1/dfkfuassgrti/image/upload"
        private const val UPLOAD_PRESET = "thread012ds"  // ✅ Make sure this is your correct preset

        /**
         * Upload image from URI (Working 100%)
         */
        fun uploadImageFromUri(
            context: Context,
            imageUri: Uri,
            callback: UploadCallback
        ) {
            Thread {
                var response: okhttp3.Response? = null
                try {
                    Log.d(TAG, "Starting upload from URI: $imageUri")

                    // Open input stream from URI
                    val inputStream = context.contentResolver.openInputStream(imageUri)
                    if (inputStream == null) {
                        Log.e(TAG, "Failed to open input stream for URI: $imageUri")
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            callback.onError("Failed to open image file")
                        }
                        return@Thread
                    }

                    // Create temporary file
                    val file = File.createTempFile(
                        "visitor_${System.currentTimeMillis()}",
                        ".jpg",
                        context.cacheDir
                    )

                    // Copy stream to file
                    FileOutputStream(file).use { output ->
                        inputStream.copyTo(output)
                    }
                    inputStream.close()

                    Log.d(TAG, "File created: ${file.absolutePath}, size: ${file.length()} bytes")

                    // Create multipart request
                    val requestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(
                            "file",
                            file.name,
                            file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        )
                        .addFormDataPart("upload_preset", UPLOAD_PRESET)
                        .build()

                    val request = Request.Builder()
                        .url(CLOUDINARY_URL)
                        .post(requestBody)
                        .build()

                    // Execute request
                    response = OkHttpClient().newCall(request).execute()
                    val body = response.body?.string()

                    Log.d(TAG, "Cloudinary Response Code: ${response.code}")
                    Log.d(TAG, "Cloudinary Response Body: $body")

                    if (response.isSuccessful && body != null) {
                        val json = JSONObject(body)
                        val url = json.getString("secure_url")
                        Log.d(TAG, "Upload successful! URL: $url")

                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            callback.onSuccess(url)
                        }
                    } else {
                        Log.e(TAG, "Upload failed with code: ${response.code}")
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            callback.onError("Upload failed: ${response.code}")
                        }
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "Cloudinary upload error", e)
                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                        callback.onError(e.message ?: "Upload error")
                    }
                } finally {
                    // ✅ FIX: Close response to prevent leak
                    response?.close()
                }
            }.start()
        }

        /**
         * Alternative: Upload from File
         */
        fun uploadImageFromFile(
            context: Context,
            file: File,
            callback: UploadCallback
        ) {
            Thread {
                var response: okhttp3.Response? = null
                try {
                    Log.d(TAG, "Uploading file: ${file.absolutePath}")

                    val requestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(
                            "file",
                            file.name,
                            file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        )
                        .addFormDataPart("upload_preset", UPLOAD_PRESET)
                        .build()

                    val request = Request.Builder()
                        .url(CLOUDINARY_URL)
                        .post(requestBody)
                        .build()

                    response = OkHttpClient().newCall(request).execute()
                    val body = response.body?.string()

                    if (response.isSuccessful && body != null) {
                        val json = JSONObject(body)
                        val url = json.getString("secure_url")

                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            callback.onSuccess(url)
                        }
                    } else {
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            callback.onError("Upload failed")
                        }
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "Upload error", e)
                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                        callback.onError(e.message ?: "Error")
                    }
                } finally {
                    response?.close()
                }
            }.start()
        }
    }
}
//    fun uploadImage(bitmap: Bitmap, callback: UploadCallback) {
//        Thread {
//            try {
//                // Convert bitmap to byte array
//                val stream = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
//                val imageData = stream.toByteArray()
//
//                // Create multipart request
//                val requestBody = MultipartBody.Builder()
//                    .setType(MultipartBody.Companion.FORM)
//                    .addFormDataPart(
//                        "file",
//                        "visitor_${System.currentTimeMillis()}.jpg",
//                        imageData.toRequestBody("image/jpeg".toMediaTypeOrNull())
//                    )
//                    .addFormDataPart("upload_preset", UPLOAD_PRESET)
//                    .build()
//
//                val request = Request.Builder()
//                    .url(CLOUDINARY_URL)
//                    .post(requestBody)
//                    .build()
//
//                client.newCall(request).execute().use { response ->
//                    if (response.isSuccessful) {
//                        val responseBody = response.body?.string()
//                        val json = JSONObject(responseBody ?: "")
//                        val imageUrl = json.getString("secure_url")
//                        callback.onSuccess(imageUrl)
//                    } else {
//                        callback.onError("Upload failed: ${response.code}")
//                    }
//                }
//            } catch (e: IOException) {
//                Log.e(TAG, "Upload error", e)
//                callback.onError(e.message ?: "Unknown error")
//            }
//        }.start()
//    }
//}
