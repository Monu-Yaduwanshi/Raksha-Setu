package com.example.rakshasetu.ui.watchMan
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.rakshasetu.ui.theme.Background
import com.example.rakshasetu.ui.theme.Primary
import com.example.rakshasetu.ui.theme.Success
import com.example.rakshasetu.ui.theme.TextSecondary
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapturePhotoScreen(
    navController: NavController,
    userRole: String? = "watchman"
) {
    val context = LocalContext.current
    var isCapturing by remember { mutableStateOf(false) }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        isCapturing = false
        bitmap?.let {
            // Save to cache and get URI
            val imageUri = saveImageToCache(context, it)

            // Return photo URI to previous screen
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("photoUrl", imageUri.toString())

            Toast.makeText(context, "Photo captured successfully", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        } ?: run {
            Toast.makeText(context, "Failed to capture photo", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            isCapturing = true
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(
                context,
                "Camera permission is required to capture visitor photos",
                Toast.LENGTH_LONG
            ).show()
            navController.popBackStack()
        }
    }

    // Check permission on launch
    LaunchedEffect(Unit) {
        if (hasCameraPermission) {
            isCapturing = true
            cameraLauncher.launch(null)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Capture Photo",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SecretaryTopBarColor
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                isCapturing -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Opening camera...",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }
                !hasCameraPermission -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Camera permission denied",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("Grant Permission")
                        }
                    }
                }
                else -> {
                    Text(
                        text = "Ready to capture",
                        fontSize = 14.sp,
                        color = Success
                    )
                }
            }
        }
    }
}

// Helper function to save bitmap to cache
fun saveImageToCache(context: android.content.Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "visitor_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
    }
    return Uri.fromFile(file)
}