package com.example.rakshasetu.ui.secretary


import android.R.attr.name
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel
import com.example.rakshasetu.viewModel.viewModel.auth.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//val SecretaryTopBarColor = Color(0xFF0CB381)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CreateResidentScreen(
//    navController: NavController,
//    userRole: String? = "secretary",
//    societyId: String,
//    societyName: String
//) {
//    val authViewModel: AuthViewModel = viewModel()
//    val secretaryViewModel: SecretaryViewModel = viewModel()
//    val context = LocalContext.current
//    val isLoading by authViewModel.isLoading.collectAsState()
//    val currentUserId = authViewModel.getCurrentUserId() ?: ""
//
//    // Get dynamic data from ViewModel
//    val society by secretaryViewModel.society.collectAsState()
//    val flats by secretaryViewModel.flats.collectAsState()
//
//    // Form fields
//    var fullName by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var phone by remember { mutableStateOf("") }
//    var selectedBlock by remember { mutableStateOf("") }
//    var selectedFlat by remember { mutableStateOf("") }
//
//    // UI states
//    var passwordVisible by remember { mutableStateOf(false) }
//    var confirmPasswordVisible by remember { mutableStateOf(false) }
//
//    // Validation states
//    var isNameValid by remember { mutableStateOf(true) }
//    var isEmailValid by remember { mutableStateOf(true) }
//    var isPasswordValid by remember { mutableStateOf(true) }
//    var isConfirmPasswordValid by remember { mutableStateOf(true) }
//    var isPhoneValid by remember { mutableStateOf(true) }
//    var isBlockSelected by remember { mutableStateOf(true) }
//    var isFlatSelected by remember { mutableStateOf(true) }
//
//    // Load society data to get blocks and flats
//    LaunchedEffect(societyId) {
//        if (societyId.isNotBlank()) {
//            secretaryViewModel.loadSociety(societyId)
//            secretaryViewModel.loadFlats(societyId)
//        }
//    }
//
//    // Get blocks from society
//    val blocks = society?.blocks ?: emptyList()
//
//    // Set default selected block if available
//    LaunchedEffect(blocks) {
//        if (blocks.isNotEmpty() && selectedBlock.isEmpty()) {
//            selectedBlock = blocks.first()
//        }
//    }
//
//    // Get available flats (not occupied) for selected block
//    val availableFlats = flats.filter {
//        it.blockNumber == selectedBlock && !it.isOccupied
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Add Resident",
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
//            // Personal Information Card
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
//                            text = "Personal Information",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        // Full Name
//                        OutlinedTextField(
//                            value = fullName,
//                            onValueChange = {
//                                fullName = it
//                                isNameValid = true
//                            },
//                            label = { Text("Full Name *") },
//                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
//                            isError = !isNameValid,
//                            supportingText = {
//                                if (!isNameValid) {
//                                    Text("Name is required", color = Error, fontSize = 12.sp)
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Surface,
//                                unfocusedContainerColor = Surface,
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint,
//                                cursorColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            enabled = !isLoading
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        // Email
//                        OutlinedTextField(
//                            value = email,
//                            onValueChange = {
//                                email = it
//                                isEmailValid = true
//                            },
//                            label = { Text("Email Address *") },
//                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
//                            isError = !isEmailValid,
//                            supportingText = {
//                                if (!isEmailValid) {
//                                    Text("Valid email is required", color = Error, fontSize = 12.sp)
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Surface,
//                                unfocusedContainerColor = Surface,
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint,
//                                cursorColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            enabled = !isLoading
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        // Phone
//                        OutlinedTextField(
//                            value = phone,
//                            onValueChange = {
//                                phone = it
//                                isPhoneValid = true
//                            },
//                            label = { Text("Phone Number *") },
//                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
//                            isError = !isPhoneValid,
//                            supportingText = {
//                                if (!isPhoneValid) {
//                                    Text("Valid phone number required", color = Error, fontSize = 12.sp)
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Surface,
//                                unfocusedContainerColor = Surface,
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint,
//                                cursorColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            enabled = !isLoading
//                        )
//                    }
//                }
//            }
//
//            // Flat Assignment Card
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
//                            text = "Flat Assignment",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        // Block Selection
//                        Text(
//                            text = "Select Block",
//                            fontSize = 14.sp,
//                            color = TextSecondary,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//
//                        if (blocks.isEmpty()) {
//                            Card(
//                                modifier = Modifier.fillMaxWidth(),
//                                colors = CardDefaults.cardColors(
//                                    containerColor = Warning.copy(alpha = 0.1f)
//                                ),
//                                shape = RoundedCornerShape(8.dp)
//                            ) {
//                                Text(
//                                    text = "No blocks found. Please add flats first.",
//                                    modifier = Modifier.padding(12.dp),
//                                    color = Warning,
//                                    fontSize = 14.sp
//                                )
//                            }
//                        } else {
//                            LazyRow(
//                                horizontalArrangement = Arrangement.spacedBy(8.dp)
//                            ) {
//                                items(blocks) { block ->
//                                    FilterChip(
//                                        selected = selectedBlock == block,
//                                        onClick = {
//                                            selectedBlock = block
//                                            selectedFlat = ""
//                                            isBlockSelected = true
//                                        },
//                                        label = { Text("Block $block") },
//                                        colors = FilterChipDefaults.filterChipColors(
//                                            selectedContainerColor = Primary,
//                                            selectedLabelColor = Color.White,
//                                            labelColor = TextSecondary
//                                        )
//                                    )
//                                }
//                            }
//                        }
//
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        // Flat Selection
//                        Text(
//                            text = "Select Flat",
//                            fontSize = 14.sp,
//                            color = TextSecondary,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//
//                        if (availableFlats.isEmpty() && blocks.isNotEmpty()) {
//                            Card(
//                                modifier = Modifier.fillMaxWidth(),
//                                colors = CardDefaults.cardColors(
//                                    containerColor = Warning.copy(alpha = 0.1f)
//                                ),
//                                shape = RoundedCornerShape(8.dp)
//                            ) {
//                                Text(
//                                    text = "No vacant flats in Block $selectedBlock. Please add flats first.",
//                                    modifier = Modifier.padding(12.dp),
//                                    color = Warning,
//                                    fontSize = 14.sp
//                                )
//                            }
//                        } else if (availableFlats.isNotEmpty()) {
//                            LazyColumn(
//                                modifier = Modifier.heightIn(max = 200.dp),
//                                verticalArrangement = Arrangement.spacedBy(4.dp)
//                            ) {
//                                items(availableFlats.size) { index ->
//                                    val flat = availableFlats[index]
//                                    val flatNumber = flat.flatNumber
//                                    FilterChip(
//                                        selected = selectedFlat == flatNumber,
//                                        onClick = {
//                                            selectedFlat = flatNumber
//                                            isFlatSelected = true
//                                        },
//                                        label = { Text("Flat $flatNumber") },
//                                        modifier = Modifier.fillMaxWidth(),
//                                        colors = FilterChipDefaults.filterChipColors(
//                                            selectedContainerColor = Primary,
//                                            selectedLabelColor = Color.White,
//                                            labelColor = TextSecondary
//                                        )
//                                    )
//                                }
//                            }
//                        }
//
//                        if (!isFlatSelected && selectedFlat.isEmpty() && blocks.isNotEmpty()) {
//                            Text(
//                                text = "Please select a flat",
//                                color = Error,
//                                fontSize = 12.sp,
//                                modifier = Modifier.padding(top = 4.dp)
//                            )
//                        }
//                    }
//                }
//            }
//
//            // Password Card
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
//                            text = "Password",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        // Password
//                        OutlinedTextField(
//                            value = password,
//                            onValueChange = {
//                                password = it
//                                isPasswordValid = true
//                            },
//                            label = { Text("Password *") },
//                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
//                            trailingIcon = {
//                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                                    Icon(
//                                        if (passwordVisible) Icons.Default.Visibility
//                                        else Icons.Default.VisibilityOff,
//                                        contentDescription = null,
//                                        tint = Primary
//                                    )
//                                }
//                            },
//                            visualTransformation = if (passwordVisible)
//                                androidx.compose.ui.text.input.VisualTransformation.None
//                            else
//                                androidx.compose.ui.text.input.PasswordVisualTransformation(),
//                            isError = !isPasswordValid,
//                            supportingText = {
//                                if (!isPasswordValid) {
//                                    Text("Password must be at least 6 characters", color = Error, fontSize = 12.sp)
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Surface,
//                                unfocusedContainerColor = Surface,
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint,
//                                cursorColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            enabled = !isLoading
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        // Confirm Password
//                        OutlinedTextField(
//                            value = confirmPassword,
//                            onValueChange = {
//                                confirmPassword = it
//                                isConfirmPasswordValid = true
//                            },
//                            label = { Text("Confirm Password *") },
//                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
//                            trailingIcon = {
//                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
//                                    Icon(
//                                        if (confirmPasswordVisible) Icons.Default.Visibility
//                                        else Icons.Default.VisibilityOff,
//                                        contentDescription = null,
//                                        tint = Primary
//                                    )
//                                }
//                            },
//                            visualTransformation = if (confirmPasswordVisible)
//                                androidx.compose.ui.text.input.VisualTransformation.None
//                            else
//                                androidx.compose.ui.text.input.PasswordVisualTransformation(),
//                            isError = !isConfirmPasswordValid,
//                            supportingText = {
//                                if (!isConfirmPasswordValid) {
//                                    Text("Passwords do not match", color = Error, fontSize = 12.sp)
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Surface,
//                                unfocusedContainerColor = Surface,
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint,
//                                cursorColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            enabled = !isLoading
//                        )
//                    }
//                }
//            }
//
//            // Submit Button
//            item {
//                Button(
//                    onClick = {
//                        // Validate inputs
//                        isNameValid = fullName.isNotBlank()
//                        isEmailValid = email.isNotBlank() &&
//                                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//                        isPhoneValid = phone.isNotBlank() && phone.length >= 10
//                        isPasswordValid = password.length >= 6
//                        isConfirmPasswordValid = password == confirmPassword && password.isNotBlank()
//                        isBlockSelected = selectedBlock.isNotBlank()
//                        isFlatSelected = selectedFlat.isNotBlank()
//
//                        if (isNameValid && isEmailValid && isPhoneValid &&
//                            isPasswordValid && isConfirmPasswordValid &&
//                            isBlockSelected && isFlatSelected) {
//
//                            authViewModel.createResident(
//                                fullName = fullName.trim(),
//                                email = email.trim(),
//                                password = password.trim(),
//                                phone = phone.trim(),
//                                flatNumber = selectedFlat,
//                                blockNumber = selectedBlock,
//                                societyId = societyId,
//                                societyName = societyName,
//                                createdBy = currentUserId
//                            ) { success, message ->
//                                if (success) {
//                                    Toast.makeText(
//                                        context,
//                                        "Resident added successfully!",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                    navController.popBackStack()
//                                } else {
//                                    Toast.makeText(
//                                        context,
//                                        "Failed: $message",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                            }
//                        } else {
//                            when {
//                                !isNameValid -> Toast.makeText(context, "Please enter your full name", Toast.LENGTH_SHORT).show()
//                                !isEmailValid -> Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
//                                !isPhoneValid -> Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
//                                !isPasswordValid -> Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
//                                !isConfirmPasswordValid -> Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
//                                !isBlockSelected -> Toast.makeText(context, "Please select a block", Toast.LENGTH_SHORT).show()
//                                !isFlatSelected -> Toast.makeText(context, "Please select a flat", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(55.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Success,
//                        disabledContainerColor = Success.copy(alpha = 0.5f)
//                    ),
//                    shape = RoundedCornerShape(14.dp),
//                    enabled = !isLoading
//                ) {
//                    if (isLoading) {
//                        CircularProgressIndicator(
//                            color = Color.White,
//                            strokeWidth = 2.dp,
//                            modifier = Modifier.size(24.dp)
//                        )
//                    } else {
//                        Text(
//                            text = "Add Resident",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.White
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CreateResidentScreen(
//    navController: NavController,
//    userRole: String? = "secretary",
//    societyId: String,
//    societyName: String
//) {
//    val authViewModel: AuthViewModel = viewModel()
//    val secretaryViewModel: SecretaryViewModel = viewModel()
//    val context = LocalContext.current
//    val isLoading by authViewModel.isLoading.collectAsState()
//    val currentUserId = authViewModel.getCurrentUserId() ?: ""
//
//    // 🔥 NEW STATES (ADDED ONLY)
//    var showDialog by remember { mutableStateOf(false) }
//    var createdUserId by remember { mutableStateOf("") }
//
//    // Get dynamic data from ViewModel
//    val society by secretaryViewModel.society.collectAsState()
//    val flats by secretaryViewModel.flats.collectAsState()
//
//    // Form fields
//    var fullName by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var phone by remember { mutableStateOf("") }
//    var selectedBlock by remember { mutableStateOf("") }
//    var selectedFlat by remember { mutableStateOf("") }
//
//    // UI states
//    var passwordVisible by remember { mutableStateOf(false) }
//    var confirmPasswordVisible by remember { mutableStateOf(false) }
//
//    // Validation states
//    var isNameValid by remember { mutableStateOf(true) }
//    var isEmailValid by remember { mutableStateOf(true) }
//    var isPasswordValid by remember { mutableStateOf(true) }
//    var isConfirmPasswordValid by remember { mutableStateOf(true) }
//    var isPhoneValid by remember { mutableStateOf(true) }
//    var isBlockSelected by remember { mutableStateOf(true) }
//    var isFlatSelected by remember { mutableStateOf(true) }
//
//    // Load society data to get blocks and flats
//    LaunchedEffect(societyId) {
//        if (societyId.isNotBlank()) {
//            secretaryViewModel.loadSociety(societyId)
//            secretaryViewModel.loadFlats(societyId)
//        }
//    }
//
//    // Get blocks from society
//    val blocks = society?.blocks ?: emptyList()
//
//    // Set default selected block if available
//    LaunchedEffect(blocks) {
//        if (blocks.isNotEmpty() && selectedBlock.isEmpty()) {
//            selectedBlock = blocks.first()
//        }
//    }
//
//    // Get available flats (not occupied) for selected block
////    val availableFlats = flats.filter {
////        it.blockNumber == selectedBlock && !it.isOccupied
////    }
//    // Filter flats by selected block - FIXED with case-insensitive comparison
//    val availableFlats = flats.filter {
//        it.blockNumber.trim().equals(selectedBlock.trim(), ignoreCase = true) && !it.isOccupied
//    }
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Add Resident",
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
//
//            // Personal Information Card
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
//                            text = "Personal Information",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        // Full Name
//                        OutlinedTextField(
//                            value = fullName,
//                            onValueChange = {
//                                fullName = it
//                                isNameValid = true
//                            },
//                            label = { Text("Full Name *") },
//                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
//                            isError = !isNameValid,
//                            supportingText = {
//                                if (!isNameValid) {
//                                    Text("Name is required", color = Error, fontSize = 12.sp)
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Surface,
//                                unfocusedContainerColor = Surface,
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint,
//                                cursorColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            enabled = !isLoading
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        // Email
//                        OutlinedTextField(
//                            value = email,
//                            onValueChange = {
//                                email = it
//                                isEmailValid = true
//                            },
//                            label = { Text("Email Address *") },
//                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
//                            isError = !isEmailValid,
//                            supportingText = {
//                                if (!isEmailValid) {
//                                    Text("Valid email is required", color = Error, fontSize = 12.sp)
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Surface,
//                                unfocusedContainerColor = Surface,
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint,
//                                cursorColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            enabled = !isLoading
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        // Phone
//                        OutlinedTextField(
//                            value = phone,
//                            onValueChange = {
//                                phone = it
//                                isPhoneValid = true
//                            },
//                            label = { Text("Phone Number *") },
//                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
//                            isError = !isPhoneValid,
//                            supportingText = {
//                                if (!isPhoneValid) {
//                                    Text("Valid phone number required", color = Error, fontSize = 12.sp)
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Surface,
//                                unfocusedContainerColor = Surface,
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint,
//                                cursorColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            enabled = !isLoading
//                        )
//                    }
//                }
//            }
//
//            // Flat Assignment Card
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
//                            text = "Flat Assignment",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        // Block Selection
//                        Text(
//                            text = "Select Block",
//                            fontSize = 14.sp,
//                            color = TextSecondary,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//
//                        if (blocks.isEmpty()) {
//                            Card(
//                                modifier = Modifier.fillMaxWidth(),
//                                colors = CardDefaults.cardColors(
//                                    containerColor = Warning.copy(alpha = 0.1f)
//                                ),
//                                shape = RoundedCornerShape(8.dp)
//                            ) {
//                                Text(
//                                    text = "No blocks found. Please add flats first.",
//                                    modifier = Modifier.padding(12.dp),
//                                    color = Warning,
//                                    fontSize = 14.sp
//                                )
//                            }
//                        } else {
//                            LazyRow(
//                                horizontalArrangement = Arrangement.spacedBy(8.dp)
//                            ) {
//                                items(blocks) { block ->
//                                    FilterChip(
//                                        selected = selectedBlock == block,
//                                        onClick = {
//                                            selectedBlock = block
//                                            selectedFlat = ""
//                                            isBlockSelected = true
//                                        },
//                                        label = { Text("Block $block") },
//                                        colors = FilterChipDefaults.filterChipColors(
//                                            selectedContainerColor = Primary,
//                                            selectedLabelColor = Color.White,
//                                            labelColor = TextSecondary
//                                        )
//                                    )
//                                }
//                            }
//                        }
//
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        // Flat Selection
//                        Text(
//                            text = "Select Flat",
//                            fontSize = 14.sp,
//                            color = TextSecondary,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//
//                        if (availableFlats.isEmpty() && blocks.isNotEmpty()) {
//                            Card(
//                                modifier = Modifier.fillMaxWidth(),
//                                colors = CardDefaults.cardColors(
//                                    containerColor = Warning.copy(alpha = 0.1f)
//                                ),
//                                shape = RoundedCornerShape(8.dp)
//                            ) {
//                                Text(
//                                    text = "No vacant flats in Block $selectedBlock. Please add flats first.",
//                                    modifier = Modifier.padding(12.dp),
//                                    color = Warning,
//                                    fontSize = 14.sp
//                                )
//                            }
//                        } else if (availableFlats.isNotEmpty()) {
//                            LazyColumn(
//                                modifier = Modifier.heightIn(max = 200.dp),
//                                verticalArrangement = Arrangement.spacedBy(4.dp)
//                            ) {
//                                items(availableFlats.size) { index ->
//                                    val flat = availableFlats[index]
//                                    val flatNumber = flat.flatNumber
//                                    FilterChip(
//                                        selected = selectedFlat == flatNumber,
//                                        onClick = {
//                                            selectedFlat = flatNumber
//                                            isFlatSelected = true
//                                        },
//                                        label = { Text("Flat $flatNumber") },
//                                        modifier = Modifier.fillMaxWidth(),
//                                        colors = FilterChipDefaults.filterChipColors(
//                                            selectedContainerColor = Primary,
//                                            selectedLabelColor = Color.White,
//                                            labelColor = TextSecondary
//                                        )
//                                    )
//                                }
//                            }
//                        }
//
//                        if (!isFlatSelected && selectedFlat.isEmpty() && blocks.isNotEmpty()) {
//                            Text(
//                                text = "Please select a flat",
//                                color = Error,
//                                fontSize = 12.sp,
//                                modifier = Modifier.padding(top = 4.dp)
//                            )
//                        }
//                    }
//                }
//            }
//
//            // Password Card
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
//                            text = "Password",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        // Password
//                        OutlinedTextField(
//                            value = password,
//                            onValueChange = {
//                                password = it
//                                isPasswordValid = true
//                            },
//                            label = { Text("Password *") },
//                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
//                            trailingIcon = {
//                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                                    Icon(
//                                        if (passwordVisible) Icons.Default.Visibility
//                                        else Icons.Default.VisibilityOff,
//                                        contentDescription = null,
//                                        tint = Primary
//                                    )
//                                }
//                            },
//                            visualTransformation = if (passwordVisible)
//                                androidx.compose.ui.text.input.VisualTransformation.None
//                            else
//                                androidx.compose.ui.text.input.PasswordVisualTransformation(),
//                            isError = !isPasswordValid,
//                            supportingText = {
//                                if (!isPasswordValid) {
//                                    Text("Password must be at least 6 characters", color = Error, fontSize = 12.sp)
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Surface,
//                                unfocusedContainerColor = Surface,
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint,
//                                cursorColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            enabled = !isLoading
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        // Confirm Password
//                        OutlinedTextField(
//                            value = confirmPassword,
//                            onValueChange = {
//                                confirmPassword = it
//                                isConfirmPasswordValid = true
//                            },
//                            label = { Text("Confirm Password *") },
//                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
//                            trailingIcon = {
//                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
//                                    Icon(
//                                        if (confirmPasswordVisible) Icons.Default.Visibility
//                                        else Icons.Default.VisibilityOff,
//                                        contentDescription = null,
//                                        tint = Primary
//                                    )
//                                }
//                            },
//                            visualTransformation = if (confirmPasswordVisible)
//                                androidx.compose.ui.text.input.VisualTransformation.None
//                            else
//                                androidx.compose.ui.text.input.PasswordVisualTransformation(),
//                            isError = !isConfirmPasswordValid,
//                            supportingText = {
//                                if (!isConfirmPasswordValid) {
//                                    Text("Passwords do not match", color = Error, fontSize = 12.sp)
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Surface,
//                                unfocusedContainerColor = Surface,
//                                focusedBorderColor = Primary,
//                                unfocusedBorderColor = TextHint,
//                                cursorColor = Primary
//                            ),
//                            shape = RoundedCornerShape(8.dp),
//                            enabled = !isLoading
//                        )
//                    }
//                }
//            }
//
//            // Submit Button
//            item {
//                Button(
//                    onClick = {
//                        // Validate inputs
//                        isNameValid = fullName.isNotBlank()
//                        isEmailValid = email.isNotBlank() &&
//                                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//                        isPhoneValid = phone.isNotBlank() && phone.length >= 10
//                        isPasswordValid = password.length >= 6
//                        isConfirmPasswordValid = password == confirmPassword && password.isNotBlank()
//                        isBlockSelected = selectedBlock.isNotBlank()
//                        isFlatSelected = selectedFlat.isNotBlank()
//
//                        if (isNameValid && isEmailValid && isPhoneValid &&
//                            isPasswordValid && isConfirmPasswordValid &&
//                            isBlockSelected && isFlatSelected) {
//
//                            authViewModel.createResident(
//                                fullName = fullName.trim(),
//                                email = email.trim(),
//                                password = password.trim(),
//                                phone = phone.trim(),
//                                flatNumber = selectedFlat,
//                                blockNumber = selectedBlock,
//                                societyId = societyId,
//                                societyName = societyName,
//                                createdBy = currentUserId
//                            ) { success, message ->
//
//                                if (success) {
//
//                                    // 🔥 IMPORTANT: message should return userId
//                                    createdUserId = message ?: ""
//
//                                    // 🔥 SAVE CREDENTIALS
//                                    FirebaseFirestore.getInstance()
//                                        .collection("user_credentials")
//                                        .document(createdUserId)
//                                        .set(
//                                            mapOf(
//                                                "userId" to createdUserId,
//                                                "email" to email.trim(),
//                                                "password" to password.trim(),
//                                                "role" to "resident",
//                                                "societyId" to societyId
//                                            )
//                                        )
//
//                                    // 🔥 SHOW DIALOG
//                                    showDialog = true
//
//                                } else {
//                                    Toast.makeText(
//                                        context,
//                                        "Failed: $message",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                            }
//                        } else {
//                            when {
//                                !isNameValid -> Toast.makeText(context, "Please enter your full name", Toast.LENGTH_SHORT).show()
//                                !isEmailValid -> Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
//                                !isPhoneValid -> Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
//                                !isPasswordValid -> Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
//                                !isConfirmPasswordValid -> Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
//                                !isBlockSelected -> Toast.makeText(context, "Please select a block", Toast.LENGTH_SHORT).show()
//                                !isFlatSelected -> Toast.makeText(context, "Please select a flat", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(55.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Success,
//                        disabledContainerColor = Success.copy(alpha = 0.5f)
//                    ),
//                    shape = RoundedCornerShape(14.dp),
//                    enabled = !isLoading
//                ) {
//                    if (isLoading) {
//                        CircularProgressIndicator(
//                            color = Color.White,
//                            strokeWidth = 2.dp,
//                            modifier = Modifier.size(24.dp)
//                        )
//                    } else {
//                        Text(
//                            text = "Add Resident",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.White
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    // 🔥 SUCCESS DIALOG (NEW - NO UI CHANGE ABOVE)
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = {},
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        showDialog = false
//                        navController.popBackStack()
//                    }
//                ) {
//                    Text("OK")
//                }
//            },
//            title = {
//                Text("Resident Created Successfully")
//            },
//            text = {
//                Column {
//                    Text("User ID: $createdUserId")
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text("Email: $email")
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text("Password: $password")
//                }
//            }
//        )
//    }
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateResidentScreen(
    navController: NavController,
    userRole: String? = "secretary",
    societyId: String,
    societyName: String
) {
    val authViewModel: AuthViewModel = viewModel()
    val secretaryViewModel: SecretaryViewModel = viewModel()
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val isLoading by authViewModel.isLoading.collectAsState()
    val currentUserId = authViewModel.getCurrentUserId() ?: ""

    // State variables
    var showDialog by remember { mutableStateOf(false) }
    var createdUserId by remember { mutableStateOf("") }

    // Get dynamic data from ViewModel
    val society by secretaryViewModel.society.collectAsState()
    val flats by secretaryViewModel.flats.collectAsState()
//    LaunchedEffect(societyId) {
//        if (societyId.isNotBlank()) {
//            secretaryViewModel.loadFlats(societyId)
//        }
//    }
    // Form fields
    var name by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedBlock by remember { mutableStateOf("") }
    var selectedFlat by remember { mutableStateOf("") }

    // UI states
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var expandedBlock by remember { mutableStateOf(false) }
    var expandedFlat by remember { mutableStateOf(false) }

    // Validation states
    var isNameValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isConfirmPasswordValid by remember { mutableStateOf(true) }
    var isPhoneValid by remember { mutableStateOf(true) }
    var isBlockSelected by remember { mutableStateOf(true) }
    var isFlatSelected by remember { mutableStateOf(true) }

    // Load society data to get blocks and flats
    LaunchedEffect(societyId) {
        if (societyId.isNotBlank()) {
            secretaryViewModel.loadSociety(societyId)
            secretaryViewModel.loadFlats(societyId)
        }
    }

    // ✅ Get only vacant flats grouped by block
    val vacantFlatsMap = flats
        .filter { !it.occupied }
        .groupBy { it.blockNumber }
//    val vacantFlatsMap = remember(flats) {
//        flats
//            .filter { !it.occupied }
//            //.filter { !it.isOccupied }   // ✅ only vacant
//            .groupBy { it.blockNumber }  // ✅ group by block
//    }

    // ✅ Only blocks having vacant flats
    val availableBlocks = vacantFlatsMap.keys.toList()

    // Set default selected block if available
    LaunchedEffect(availableBlocks) {
        if (availableBlocks.isNotEmpty() && selectedBlock.isEmpty()) {
            selectedBlock = availableBlocks.first()
        }
    }

    // ✅ Get available flats for selected block
    val availableFlats = vacantFlatsMap[selectedBlock] ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Resident",
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

            // Personal Information Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Text(
                            text = "Personal Information",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Full Name
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = {
                                fullName = it
                                isNameValid = true
                            },
                            label = { Text("Full Name *") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            isError = !isNameValid,
                            supportingText = {
                                if (!isNameValid) {
                                    Text("Name is required", color = Error, fontSize = 12.sp)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Surface,
                                unfocusedContainerColor = Surface,
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextHint,
                                cursorColor = Primary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !isLoading
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                isEmailValid = true
                            },
                            label = { Text("Email Address *") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            isError = !isEmailValid,
                            supportingText = {
                                if (!isEmailValid) {
                                    Text("Valid email is required", color = Error, fontSize = 12.sp)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Surface,
                                unfocusedContainerColor = Surface,
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextHint,
                                cursorColor = Primary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !isLoading
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Phone
                        OutlinedTextField(
                            value = phone,
                            onValueChange = {
                                phone = it
                                isPhoneValid = true
                            },
                            label = { Text("Phone Number *") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                            isError = !isPhoneValid,
                            supportingText = {
                                if (!isPhoneValid) {
                                    Text("Valid phone number required", color = Error, fontSize = 12.sp)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Surface,
                                unfocusedContainerColor = Surface,
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextHint,
                                cursorColor = Primary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !isLoading
                        )
                    }
                }
            }

            // Flat Assignment Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Text(
                            text = "Flat Assignment",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Block Selection using Dropdown
                        Text(
                            text = "Select Block",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (availableBlocks.isEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Warning.copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "No vacant blocks available. Please add flats first.",
                                    modifier = Modifier.padding(12.dp),
                                    color = Warning,
                                    fontSize = 14.sp
                                )
                            }
                        } else {
                            ExposedDropdownMenuBox(
                                expanded = expandedBlock,
                                onExpandedChange = { expandedBlock = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedBlock,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Select Block") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBlock) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Surface,
                                        unfocusedContainerColor = Surface,
                                        focusedBorderColor = Primary,
                                        unfocusedBorderColor = TextHint,
                                        cursorColor = Primary
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                ExposedDropdownMenu(
                                    expanded = expandedBlock,
                                    onDismissRequest = { expandedBlock = false }
                                ) {
                                    availableBlocks.forEach { block ->
                                        DropdownMenuItem(
                                            text = { Text("Block $block") },
                                            onClick = {
                                                selectedBlock = block
                                                selectedFlat = ""
                                                expandedBlock = false
                                                isBlockSelected = true
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Flat Selection using Dropdown
                        Text(
                            text = "Select Flat",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (selectedBlock.isNotEmpty() && availableFlats.isEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Warning.copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "No vacant flats in Block $selectedBlock",
                                    modifier = Modifier.padding(12.dp),
                                    color = Warning,
                                    fontSize = 14.sp
                                )
                            }
                        } else if (selectedBlock.isNotEmpty() && availableFlats.isNotEmpty()) {
                            ExposedDropdownMenuBox(
                                expanded = expandedFlat,
                                onExpandedChange = { expandedFlat = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedFlat,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Select Flat") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFlat) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Surface,
                                        unfocusedContainerColor = Surface,
                                        focusedBorderColor = Primary,
                                        unfocusedBorderColor = TextHint,
                                        cursorColor = Primary
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                ExposedDropdownMenu(
                                    expanded = expandedFlat,
                                    onDismissRequest = { expandedFlat = false }
                                ) {
                                    availableFlats.forEach { flat ->
                                        DropdownMenuItem(
                                            text = { Text("Flat ${flat.flatNumber}") },
                                            onClick = {
                                                selectedFlat = flat.flatNumber
                                                expandedFlat = false
                                                isFlatSelected = true
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        if (!isFlatSelected && selectedFlat.isEmpty() && availableBlocks.isNotEmpty()) {
                            Text(
                                text = "Please select a flat",
                                color = Error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Password Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Text(
                            text = "Password",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Password
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                isPasswordValid = true
                            },
                            label = { Text("Password *") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Default.Visibility
                                        else Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = Primary
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible)
                                androidx.compose.ui.text.input.VisualTransformation.None
                            else
                                androidx.compose.ui.text.input.PasswordVisualTransformation(),
                            isError = !isPasswordValid,
                            supportingText = {
                                if (!isPasswordValid) {
                                    Text("Password must be at least 6 characters", color = Error, fontSize = 12.sp)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Surface,
                                unfocusedContainerColor = Surface,
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextHint,
                                cursorColor = Primary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !isLoading
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Confirm Password
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                isConfirmPasswordValid = true
                            },
                            label = { Text("Confirm Password *") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        if (confirmPasswordVisible) Icons.Default.Visibility
                                        else Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = Primary
                                    )
                                }
                            },
                            visualTransformation = if (confirmPasswordVisible)
                                androidx.compose.ui.text.input.VisualTransformation.None
                            else
                                androidx.compose.ui.text.input.PasswordVisualTransformation(),
                            isError = !isConfirmPasswordValid,
                            supportingText = {
                                if (!isConfirmPasswordValid) {
                                    Text("Passwords do not match", color = Error, fontSize = 12.sp)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Surface,
                                unfocusedContainerColor = Surface,
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextHint,
                                cursorColor = Primary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !isLoading
                        )
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = {
                        // Validate inputs
                        isNameValid = fullName.isNotBlank()
                        isEmailValid = email.isNotBlank() &&
                                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        isPhoneValid = phone.isNotBlank() && phone.length >= 10
                        isPasswordValid = password.length >= 6
                        isConfirmPasswordValid = password == confirmPassword && password.isNotBlank()
                        isBlockSelected = selectedBlock.isNotBlank()
                        isFlatSelected = selectedFlat.isNotBlank()

                        if (isNameValid && isEmailValid && isPhoneValid &&
                            isPasswordValid && isConfirmPasswordValid &&
                            isBlockSelected && isFlatSelected) {

                            authViewModel.createResident(
                                fullName = fullName.trim(),
                                email = email.trim(),
                                password = password.trim(),
                                phone = phone.trim(),
                                flatNumber = selectedFlat,
                                blockNumber = selectedBlock,
                                societyId = societyId,
                                societyName = societyName,
                                createdBy = currentUserId
                            ) { success, message ->

                                if (success) {
                                    createdUserId = message ?: ""

                                    // ✅ Update flat as occupied
                                    val userRef = firestore.collection("users").document()
                                    val userId = userRef.id

                                    val resident = User(
                                        userId = userId,
                                        //fullName = name,
                                        fullName = fullName,
                                        phone = phone,
                                        role = "resident",
                                        societyId = societyId,
                                        societyName = societyName,
                                        blockNumber = selectedBlock,
                                        flatNumber = selectedFlat,
                                        isActive = true,
                                        createdAt = System.currentTimeMillis(),
                                        createdBy = FirebaseAuth.getInstance().currentUser?.uid
                                            ?: ""
                                    )

// Save resident
                                    userRef.set(resident)

// Update flat
                                    //val flatId = "${societyId}__${selectedFlat}"
// ✅ CORRECTED VERSION - Add residentName
                                    val flatId = "${societyId}_${selectedBlock}_${selectedFlat}"
                                    firestore.collection("flats").document(flatId)
                                        .update(
                                            mapOf(
                                                "occupied" to true,
                                                "residentId" to userId,
                                                "residentName" to fullName,  // ✅ ADD THIS LINE
                                                "residentPhone" to phone
                                            )
                                        )
                                        .addOnSuccessListener {
                                            Log.d("CreateResident", "Flat $flatId marked as occupied with resident: $fullName")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("CreateResident", "Error updating flat: ${e.message}")
                                        }
                                        .addOnSuccessListener {
                                            Log.d("CreateResident", "Flat $flatId marked as occupied")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("CreateResident", "Error updating flat: ${e.message}")
                                        }

                                    // Save credentials to Firestore
                                    firestore.collection("user_credentials")
                                        .document(createdUserId)
                                        .set(
                                            mapOf(
                                                "userId" to createdUserId,
                                                "email" to email.trim(),
                                                "password" to password.trim(),
                                                "role" to "resident",
                                                "societyId" to societyId,
                                                "blockNumber" to selectedBlock,
                                                "flatNumber" to selectedFlat
                                            )
                                        )
                                        .addOnSuccessListener {
                                            showDialog = true
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                context,
                                                "Resident created but credential save failed: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            showDialog = true
                                        }

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed: $message",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } else {
                            when {
                                !isNameValid -> Toast.makeText(context, "Please enter your full name", Toast.LENGTH_SHORT).show()
                                !isEmailValid -> Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                                !isPhoneValid -> Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                                !isPasswordValid -> Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                                !isConfirmPasswordValid -> Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                !isBlockSelected -> Toast.makeText(context, "Please select a block", Toast.LENGTH_SHORT).show()
                                !isFlatSelected -> Toast.makeText(context, "Please select a flat", Toast.LENGTH_SHORT).show()
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
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Add Resident",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    // Success Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("OK")
                }
            },
            title = {
                Text("Resident Created Successfully")
            },
            text = {
                Column {
                    Text("User ID: $createdUserId")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Email: $email")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Password: $password")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Assigned Flat: $selectedBlock-$selectedFlat")
                }
            }
        )
    }
}