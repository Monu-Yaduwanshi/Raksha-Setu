package com.example.rakshasetu.ui.watchMan
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.core.utils.safeClickable
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.watchman.WatchmanViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddVisitorScreen(
//    navController: NavController,
//    flatNumber: String,  // Get flatNumber directly from navigation
//    userRole: String? = "watchman"
//) {
//    val viewModel: WatchmanViewModel = viewModel()
//    val context = LocalContext.current
//    val isLoading by viewModel.isLoading.collectAsState()
//
//    // ✅ FIX 1: Use rememberSaveable instead of remember
//    var visitorName by rememberSaveable { mutableStateOf("") }
//    var visitorPhone by rememberSaveable { mutableStateOf("") }
//    var purpose by rememberSaveable { mutableStateOf("Delivery") }
//    var organization by rememberSaveable { mutableStateOf("") }
//    var numberOfVisitors by rememberSaveable { mutableStateOf("1") }
//    var photoUrl by rememberSaveable { mutableStateOf("") }
//
//    val purposes = listOf("Delivery", "Guest", "Cab", "House Help", "Maintenance", "Other")
//
//    // Receive photo from CapturePhotoScreen
//    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
//    val returnedPhoto = savedStateHandle?.get<String>("photoUrl")
//
//    LaunchedEffect(returnedPhoto) {
//        if (!returnedPhoto.isNullOrEmpty()) {
//            photoUrl = returnedPhoto
//            savedStateHandle.remove<String>("photoUrl")
//            Toast.makeText(context, "Photo captured successfully", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Add Visitor",
//                        color = Color.White,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            Icons.Default.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color.White
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = SecretaryTopBarColor
//                )
//            )
//        }
//    ) { paddingValues ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Background)
//                .padding(paddingValues)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // Selected Flat Card
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Primary.copy(alpha = 0.1f)
//                    ),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.Home,
//                            contentDescription = null,
//                            tint = Primary
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = "Selected Flat: $flatNumber",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Primary
//                        )
//                        Spacer(modifier = Modifier.weight(1f))
//                        Button(
//                            onClick = {
//                                navController.popBackStack()
//                            },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Text("Change")
//                        }
//                    }
//                }
//            }
//
//            // Photo Capture Section
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Surface
//                    ),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = "Visitor Photo",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary,
//                            modifier = Modifier.align(Alignment.Start)
//                        )
//
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        Button(
//                            onClick = {
//                                navController.navigate("capture_photo")
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Icon(Icons.Default.CameraAlt, contentDescription = null)
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(if (photoUrl.isNotEmpty()) "Retake Photo" else "Take Photo")
//                        }
//
//                        if (photoUrl.isNotEmpty()) {
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                modifier = Modifier
//                                    .background(Success.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
//                                    .padding(8.dp)
//                            ) {
//                                Icon(
//                                    Icons.Default.CheckCircle,
//                                    contentDescription = null,
//                                    tint = Success,
//                                    modifier = Modifier.size(16.dp)
//                                )
//                                Spacer(modifier = Modifier.width(4.dp))
//                                Text(
//                                    text = "Photo captured successfully",
//                                    fontSize = 12.sp,
//                                    color = Success
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Visitor Details Form
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Surface
//                    ),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Visitor Details",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary,
//                            modifier = Modifier.padding(bottom = 12.dp)
//                        )
//
//                        // Visitor Name
//                        OutlinedTextField(
//                            value = visitorName,
//                            onValueChange = { visitorName = it },
//                            label = { Text("Visitor Name *") },
//                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            singleLine = true
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        // Phone Number - ✅ FIX 3: Added keyboard type
//                        OutlinedTextField(
//                            value = visitorPhone,
//                            onValueChange = { visitorPhone = it },
//                            label = { Text("Phone Number") },
//                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            singleLine = true,
//                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
//                                keyboardType = KeyboardType.Phone
//                            )
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        // Purpose Selection
//                        Text(
//                            text = "Purpose *",
//                            fontSize = 14.sp,
//                            color = TextSecondary,
//                            modifier = Modifier.padding(bottom = 4.dp)
//                        )
//
//                        // ✅ Fixed: Using Column instead of nested LazyColumn
//// In AddVisitorScreen.kt - Update the purpose selection section
//
//// Purpose Selection - Using safeClickable (no ripple)
//                        Column(
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            purposes.forEach { p ->
//                                Row(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(vertical = 4.dp)
//                                        .safeClickable { purpose = p },  // ✅ Using safeClickable
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    RadioButton(
//                                        selected = purpose == p,
//                                        onClick = { purpose = p },
//                                        colors = RadioButtonDefaults.colors(
//                                            selectedColor = Primary
//                                        )
//                                    )
//                                    Text(
//                                        text = p,
//                                        modifier = Modifier.padding(start = 8.dp)
//                                    )
//                                }
//                            }
//                        }
//
//                        if (purpose == "Delivery" || purpose == "Cab") {
//                            Spacer(modifier = Modifier.height(8.dp))
//
//                            OutlinedTextField(
//                                value = organization,
//                                onValueChange = { organization = it },
//                                label = { Text("Organization/Company") },
//                                leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
//                                modifier = Modifier.fillMaxWidth(),
//                                colors = OutlinedTextFieldDefaults.colors(
//                                    focusedBorderColor = Primary,
//                                    unfocusedBorderColor = TextHint
//                                ),
//                                shape = RoundedCornerShape(8.dp),
//                                singleLine = true
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        // Number of Visitors - ✅ FIX 2: Filter only digits
//                        OutlinedTextField(
//                            value = numberOfVisitors,
//                            onValueChange = {
//                                numberOfVisitors = it.filter { char -> char.isDigit() }
//                            },
//                            label = { Text("Number of Visitors") },
//                            leadingIcon = { Icon(Icons.Default.Group, contentDescription = null) },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            singleLine = true,
//                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
//                                keyboardType = KeyboardType.Number
//                            )
//                        )
//                    }
//                }
//            }
//
//            // Submit Button
//            item {
//                Button(
//                    onClick = {
//                        // Validation
//                        if (visitorName.isBlank()) {
//                            Toast.makeText(context, "Please enter visitor name", Toast.LENGTH_SHORT).show()
//                            return@Button
//                        }
//                        if (photoUrl.isBlank()) {
//                            Toast.makeText(context, "Please capture visitor photo", Toast.LENGTH_SHORT).show()
//                            return@Button
//                        }
//
//                        val count = numberOfVisitors.toIntOrNull()
//                        if (count == null || count <= 0) {
//                            Toast.makeText(context, "Please enter valid number of visitors", Toast.LENGTH_SHORT).show()
//                            return@Button
//                        }
//
//                        // Use the simplified function
//                        viewModel.createVisitorRequest(
//                            flatNumber = flatNumber,
//                            visitorName = visitorName.trim(),
//                            visitorPhone = visitorPhone.trim(),
//                            purpose = purpose,
//                            organization = organization.trim(),
//                            numberOfVisitors = count,
//                            photoUrl = photoUrl,
//                            onSuccess = {
//                                Toast.makeText(
//                                    context,
//                                    "Visitor request sent to resident successfully",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                                navController.popBackStack()
//                            },
//                            onError = { error ->
//                                Toast.makeText(
//                                    context,
//                                    "Failed: $error",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        )
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(55.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Success,
//                        disabledContainerColor = Success.copy(alpha = 0.5f)
//                    ),
//                    shape = RoundedCornerShape(14.dp),
//                    enabled = !isLoading && visitorName.isNotBlank() && photoUrl.isNotBlank()
//                ) {
//                    if (isLoading) {
//                        CircularProgressIndicator(
//                            color = Color.White,
//                            strokeWidth = 2.dp,
//                            modifier = Modifier.size(24.dp)
//                        )
//                    } else {
//                        Text(
//                            text = "Send Request to Resident",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.White
//                        )
//                    }
//                }
//            }
//
//            // Bottom padding
//            item {
//                Spacer(modifier = Modifier.height(32.dp))
//            }
//        }
//    }
//}


import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVisitorScreen(
    navController: NavController,
    flatNumber: String,
    userRole: String? = "watchman"
) {
    val viewModel: WatchmanViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isLoading by viewModel.isLoading.collectAsState()
    val uploadProgress by viewModel.uploadProgress.collectAsState()

    // State for resident data
    var residentId by remember { mutableStateOf("") }
    var residentName by remember { mutableStateOf("") }
    var societyId by remember { mutableStateOf("") }
    var isLoadingResident by remember { mutableStateOf(true) }

    // Form states
    var visitorName by rememberSaveable { mutableStateOf("") }
    var visitorPhone by rememberSaveable { mutableStateOf("") }
    var purpose by rememberSaveable { mutableStateOf("Delivery") }
    var organization by rememberSaveable { mutableStateOf("") }
    var numberOfVisitors by rememberSaveable { mutableStateOf("1") }
    var vehicleNumber by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var photoUri by rememberSaveable { mutableStateOf<String?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val purposes = listOf("Delivery", "Guest", "Cab", "House Help", "Maintenance", "Other")

    // Create interaction source for clickable
    val interactionSource = remember { MutableInteractionSource() }
// In AddVisitorScreen.kt, replace the LaunchedEffect with this:

// In AddVisitorScreen.kt, update the LaunchedEffect that fetches resident
    // ✅ FIXED: Fetch resident using the ViewModel function
    LaunchedEffect(flatNumber) {
        isLoadingResident = true

        try {
            val resident = viewModel.getResidentByFlat(flatNumber)

            if (resident != null) {
                // ✅ CRITICAL: Use documentId (Firebase Auth UID)
                residentId = resident.documentId
                residentName = resident.fullName
                societyId = resident.societyId

                Log.d("AddVisitorScreen", "=========================================")
                Log.d("AddVisitorScreen", "✅ Found resident for flat: $flatNumber")
                Log.d("AddVisitorScreen", "✅ residentId (Firebase UID): $residentId")
                Log.d("AddVisitorScreen", "✅ Resident Name: $residentName")
                Log.d("AddVisitorScreen", "=========================================")
            } else {
                Log.e("AddVisitorScreen", "❌ No resident found for flat: $flatNumber")
                Toast.makeText(context, "No resident found for flat $flatNumber", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("AddVisitorScreen", "Error fetching resident", e)
            Toast.makeText(context, "Error finding resident: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            isLoadingResident = false
        }
    }
//    LaunchedEffect(flatNumber) {
//        isLoadingResident = true
//        val firestore = FirebaseFirestore.getInstance()
//
//        try {
//            val querySnapshot = firestore.collection("users")
//                .whereEqualTo("flatNumber", flatNumber)
//                .whereEqualTo("role", "resident")
//                .get()
//                .await()
//
//            val document = querySnapshot.documents.firstOrNull()
//
//            if (document != null) {
//                // ✅ CRITICAL: document.id is the Firebase Auth UID
//                residentId = document.id  // ✅ This is the correct UID
//                residentName = document.getString("fullName") ?: "Resident"
//                societyId = document.getString("societyId") ?: ""
//
//                Log.d("AddVisitorScreen", "=========================================")
//                Log.d("AddVisitorScreen", "✅ Found resident for flat: $flatNumber")
//                Log.d("AddVisitorScreen", "✅ residentId (Auth UID): $residentId")
//                Log.d("AddVisitorScreen", "✅ Resident Name: $residentName")
//                Log.d("AddVisitorScreen", "⚠️ This MUST match: AkO8PAyL27Z96X5QfJKiXTW8CDQ2")
//                Log.d("AddVisitorScreen", "=========================================")
//            } else {
//                Log.e("AddVisitorScreen", "❌ No resident found for flat: $flatNumber")
//                Toast.makeText(context, "No resident found for flat $flatNumber", Toast.LENGTH_LONG).show()
//            }
//        } catch (e: Exception) {
//            Log.e("AddVisitorScreen", "Error fetching resident", e)
//            Toast.makeText(context, "Error finding resident: ${e.message}", Toast.LENGTH_LONG).show()
//        } finally {
//            isLoadingResident = false
//        }
//    }
    // Fetch residentId from flat number
//    LaunchedEffect(flatNumber) {
//        isLoadingResident = true
//        val firestore = FirebaseFirestore.getInstance()
//
//        try {
//            val querySnapshot = firestore.collection("users")
//                .whereEqualTo("flatNumber", flatNumber)
//                .whereEqualTo("role", "resident")
//                .get()
//                .await()
//
//            val document = querySnapshot.documents.firstOrNull()
//
//            if (document != null) {
//                residentId = document.id
//                residentName = document.getString("fullName") ?: "Resident"
//                societyId = document.getString("societyId") ?: ""
//                Log.d("AddVisitorScreen", "Found resident: $residentName with ID: $residentId")
//            } else {
//                Log.e("AddVisitorScreen", "No resident found for flat: $flatNumber")
//                Toast.makeText(context, "No resident found for flat $flatNumber", Toast.LENGTH_LONG).show()
//            }
//        } catch (e: Exception) {
//            Log.e("AddVisitorScreen", "Error fetching resident", e)
//            Toast.makeText(context, "Error finding resident: ${e.message}", Toast.LENGTH_LONG).show()
//        } finally {
//            isLoadingResident = false
//        }
//    }

    // Receive photo from CapturePhotoScreen
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val returnedPhotoUri = savedStateHandle?.get<String>("photoUrl")

    LaunchedEffect(returnedPhotoUri) {
        if (!returnedPhotoUri.isNullOrEmpty()) {
            photoUri = returnedPhotoUri
            savedStateHandle.remove<String>("photoUrl")
            Toast.makeText(context, "Photo captured successfully", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Visitor",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Selected Flat Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Primary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            tint = Primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Selected Flat: $flatNumber",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                            if (isLoadingResident) {
                                Text(
                                    text = "Loading resident...",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            } else if (residentName.isNotBlank()) {
                                Text(
                                    text = "Resident: $residentName",
                                    fontSize = 12.sp,
                                    color = Success
                                )
                            } else {
                                Text(
                                    text = "No resident found!",
                                    fontSize = 12.sp,
                                    color = Error
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Change")
                        }
                    }
                }
            }

            // Photo Capture Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Visitor Photo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                navController.navigate("capture_photo")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (photoUri != null) "Retake Photo" else "Take Photo")
                        }

                        if (photoUri != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(Success.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Success,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Photo captured successfully",
                                    fontSize = 12.sp,
                                    color = Success
                                )
                            }
                        }
                    }
                }
            }

            // Upload Progress
            if (isUploading) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                progress = uploadProgress / 100f,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Uploading photo: $uploadProgress%",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Visitor Details Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Visitor Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = visitorName,
                            onValueChange = { visitorName = it },
                            label = { Text("Visitor Name *") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextHint
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = visitorPhone,
                            onValueChange = { visitorPhone = it },
                            label = { Text("Phone Number") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextHint
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Phone
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Purpose *",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            purposes.forEach { p ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        // ✅ FIXED: Proper clickable with indication and interactionSource
                                        .clickable(
                                            indication = LocalIndication.current,
                                            interactionSource = interactionSource
                                        ) {
                                            purpose = p
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = purpose == p,
                                        onClick = {
                                            purpose = p
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Primary
                                        )
                                    )
                                    Text(
                                        text = p,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }

                        if (purpose == "Delivery" || purpose == "Cab") {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = organization,
                                onValueChange = { organization = it },
                                label = { Text("Organization/Company") },
                                leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = TextHint
                                ),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = numberOfVisitors,
                            onValueChange = {
                                numberOfVisitors = it.filter { char -> char.isDigit() }
                            },
                            label = { Text("Number of Visitors") },
                            leadingIcon = { Icon(Icons.Default.Group, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextHint
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = vehicleNumber,
                            onValueChange = { vehicleNumber = it },
                            label = { Text("Vehicle Number (if any)") },
                            leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextHint
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Notes (optional)") },
                            leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextHint
                            ),
                            shape = RoundedCornerShape(8.dp),
                            minLines = 2
                        )
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = {
                        // Validation
                        if (visitorName.isBlank()) {
                            Toast.makeText(context, "Please enter visitor name", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (photoUri == null) {
                            Toast.makeText(context, "Please capture visitor photo", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (residentId.isBlank()) {
                            Toast.makeText(context, "No resident found for this flat", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val count = numberOfVisitors.toIntOrNull()
                        if (count == null || count <= 0) {
                            Toast.makeText(context, "Please enter valid number of visitors", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isUploading = true
                        val imageUri = Uri.parse(photoUri)

                        viewModel.uploadImageToCloudinary(context, imageUri) { uploadedUrl ->
                            isUploading = false

                            if (uploadedUrl != null) {
                                viewModel.createVisitorRequest(
                                    flatNumber = flatNumber,
                                    visitorName = visitorName.trim(),
                                    visitorPhone = visitorPhone.trim(),
                                    purpose = purpose,
                                    organization = organization.trim(),
                                    numberOfVisitors = count,
                                    vehicleNumber = vehicleNumber.trim(),
                                    notes = notes.trim(),
                                    photoUrl = uploadedUrl,
                                    residentId = residentId,
                                    residentName = residentName,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Visitor request sent to resident successfully",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.popBackStack()
                                    },
                                    onError = { error ->
                                        Toast.makeText(
                                            context,
                                            "Failed: $error",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to upload photo. Please try again.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Success,
                        disabledContainerColor = Success.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    enabled = !isLoading && !isUploading && visitorName.isNotBlank() && photoUri != null && residentId.isNotBlank()
                ) {
                    when {
                        isLoading || isUploading -> {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isUploading) "Uploading photo..." else "Sending...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        else -> {
                            Text(
                                text = "Send Request to Resident",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}