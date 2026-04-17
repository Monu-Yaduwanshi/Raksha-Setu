package com.example.rakshasetu.ui.secretary

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CreateWatchmanScreen(
//    navController: NavController,
//    userRole: String? = "secretary",
//    societyId: String,
//    societyName: String
//) {
//    val viewModel: AuthViewModel = viewModel()
//    val context = LocalContext.current
//    val isLoading by viewModel.isLoading.collectAsState()
//    val currentUserId = viewModel.getCurrentUserId() ?: ""
//
//    var fullName by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var phone by remember { mutableStateOf("") }
//    var selectedShift by remember { mutableStateOf("morning") }
//
//    var passwordVisible by remember { mutableStateOf(false) }
//    var confirmPasswordVisible by remember { mutableStateOf(false) }
//
//    // Validation states
//    var isNameValid by remember { mutableStateOf(true) }
//    var isEmailValid by remember { mutableStateOf(true) }
//    var isPasswordValid by remember { mutableStateOf(true) }
//    var isConfirmPasswordValid by remember { mutableStateOf(true) }
//    var isPhoneValid by remember { mutableStateOf(true) }
//
//    val shifts = listOf("Morning", "Evening", "Night")
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Add Watchman",
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
//                            text = "Shift Assignment",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        Text(
//                            text = "Select Shift",
//                            fontSize = 14.sp,
//                            color = TextSecondary,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//
//                        LazyRow(
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            items(shifts) { shift ->
//                                FilterChip(
//                                    selected = selectedShift == shift.lowercase(),
//                                    onClick = { selectedShift = shift.lowercase() },
//                                    label = { Text(shift) },
//                                    colors = FilterChipDefaults.filterChipColors(
//                                        selectedContainerColor = Secondary,
//                                        selectedLabelColor = Color.White,
//                                        labelColor = TextSecondary
//                                    )
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
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
//
//                        if (isNameValid && isEmailValid && isPhoneValid &&
//                            isPasswordValid && isConfirmPasswordValid) {
//
//                            viewModel.createWatchman(
//                                fullName = fullName.trim(),
//                                email = email.trim(),
//                                password = password.trim(),
//                                phone = phone.trim(),
//                                societyId = societyId,
//                                societyName = societyName,
//                                shift = selectedShift,
//                                createdBy = currentUserId
//                            ) { success, message ->
//                                if (success) {
//                                    Toast.makeText(
//                                        context,
//                                        "Watchman added successfully!",
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
//                            Toast.makeText(
//                                context,
//                                "Please fill all required fields correctly",
//                                Toast.LENGTH_SHORT
//                            ).show()
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
//                            text = "Add Watchman",
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
//fun CreateWatchmanScreen(
//    navController: NavController,
//    userRole: String? = "secretary",
//    societyId: String,
//    societyName: String
//) {
//    val viewModel: AuthViewModel = viewModel()
//    val context = LocalContext.current
//    val isLoading by viewModel.isLoading.collectAsState()
//    val currentUserId = viewModel.getCurrentUserId() ?: ""
//
//    // 🔥 NEW STATES (ADDED ONLY)
//    var showDialog by remember { mutableStateOf(false) }
//    var createdUserId by remember { mutableStateOf("") }
//
//    var fullName by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var phone by remember { mutableStateOf("") }
//    var selectedShift by remember { mutableStateOf("morning") }
//
//    var passwordVisible by remember { mutableStateOf(false) }
//    var confirmPasswordVisible by remember { mutableStateOf(false) }
//
//    // Validation states
//    var isNameValid by remember { mutableStateOf(true) }
//    var isEmailValid by remember { mutableStateOf(true) }
//    var isPasswordValid by remember { mutableStateOf(true) }
//    var isConfirmPasswordValid by remember { mutableStateOf(true) }
//    var isPhoneValid by remember { mutableStateOf(true) }
//
//    val shifts = listOf("Morning", "Evening", "Night")
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Add Watchman",
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
//                        OutlinedTextField(
//                            value = fullName,
//                            onValueChange = {
//                                fullName = it
//                                isNameValid = true
//                            },
//                            label = { Text("Full Name *") },
//                            leadingIcon = { Icon(Icons.Default.Person, null) },
//                            isError = !isNameValid,
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
//                        OutlinedTextField(
//                            value = email,
//                            onValueChange = {
//                                email = it
//                                isEmailValid = true
//                            },
//                            label = { Text("Email Address *") },
//                            leadingIcon = { Icon(Icons.Default.Email, null) },
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
//                        OutlinedTextField(
//                            value = phone,
//                            onValueChange = {
//                                phone = it
//                                isPhoneValid = true
//                            },
//                            label = { Text("Phone Number *") },
//                            leadingIcon = { Icon(Icons.Default.Phone, null) },
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
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(containerColor = Surface),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Shift Assignment",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        Text(
//                            text = "Select Shift",
//                            fontSize = 14.sp,
//                            color = TextSecondary,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//
//                        LazyRow(
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            items(shifts) { shift ->
//                                FilterChip(
//                                    selected = selectedShift == shift.lowercase(),
//                                    onClick = { selectedShift = shift.lowercase() },
//                                    label = { Text(shift) },
//                                    colors = FilterChipDefaults.filterChipColors(
//                                        selectedContainerColor = Secondary,
//                                        selectedLabelColor = Color.White,
//                                        labelColor = TextSecondary
//                                    )
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(containerColor = Surface),
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
//                        OutlinedTextField(
//                            value = password,
//                            onValueChange = {
//                                password = it
//                                isPasswordValid = true
//                            },
//                            label = { Text("Password *") },
//                            leadingIcon = { Icon(Icons.Default.Lock, null) },
//                            trailingIcon = {
//                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                                    Icon(
//                                        if (passwordVisible) Icons.Default.Visibility
//                                        else Icons.Default.VisibilityOff,
//                                        null,
//                                        tint = Primary
//                                    )
//                                }
//                            },
//                            visualTransformation = if (passwordVisible)
//                                androidx.compose.ui.text.input.VisualTransformation.None
//                            else
//                                androidx.compose.ui.text.input.PasswordVisualTransformation(),
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
//                        OutlinedTextField(
//                            value = confirmPassword,
//                            onValueChange = {
//                                confirmPassword = it
//                                isConfirmPasswordValid = true
//                            },
//                            label = { Text("Confirm Password *") },
//                            leadingIcon = { Icon(Icons.Default.Lock, null) },
//                            trailingIcon = {
//                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
//                                    Icon(
//                                        if (confirmPasswordVisible) Icons.Default.Visibility
//                                        else Icons.Default.VisibilityOff,
//                                        null,
//                                        tint = Primary
//                                    )
//                                }
//                            },
//                            visualTransformation = if (confirmPasswordVisible)
//                                androidx.compose.ui.text.input.VisualTransformation.None
//                            else
//                                androidx.compose.ui.text.input.PasswordVisualTransformation(),
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
//            item {
//                Button(
//                    onClick = {
//
//                        isNameValid = fullName.isNotBlank()
//                        isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//                        isPhoneValid = phone.length >= 10
//                        isPasswordValid = password.length >= 6
//                        isConfirmPasswordValid = password == confirmPassword
//
//                        if (isNameValid && isEmailValid && isPhoneValid &&
//                            isPasswordValid && isConfirmPasswordValid) {
//
//                            viewModel.createWatchman(
//                                fullName = fullName.trim(),
//                                email = email.trim(),
//                                password = password.trim(),
//                                phone = phone.trim(),
//                                societyId = societyId,
//                                societyName = societyName,
//                                shift = selectedShift,
//                                createdBy = currentUserId
//                            ) { success, message ->
//
//                                if (success) {
//
//                                    // 🔥 USER ID FROM CALLBACK
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
//                                                "role" to "watchman",
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
//
//                        } else {
//                            Toast.makeText(
//                                context,
//                                "Please fill all required fields correctly",
//                                Toast.LENGTH_SHORT
//                            ).show()
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
//                            text = "Add Watchman",
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
//    // 🔥 SUCCESS DIALOG (NEW - DOES NOT CHANGE UI ABOVE)
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
//                Text("Watchman Created Successfully")
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
fun CreateWatchmanScreen(
    navController: NavController,
    userRole: String? = "secretary",
    societyId: String,
    societyName: String
) {
    val authViewModel: AuthViewModel = viewModel()
    val secretaryViewModel: SecretaryViewModel = viewModel()
    val context = LocalContext.current
    val isLoading by authViewModel.isLoading.collectAsState()
    val currentUserId = authViewModel.getCurrentUserId() ?: ""

    // Get dynamic data from ViewModel
    val society by secretaryViewModel.society.collectAsState()

    // Load society data
    LaunchedEffect(societyId) {
        if (societyId.isNotBlank()) {
            secretaryViewModel.loadSociety(societyId)
        }
    }

    // State variables
    var showDialog by remember { mutableStateOf(false) }
    var createdUserId by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedShift by remember { mutableStateOf("morning") }

    // ✅ Neutralized - set to empty strings (no flat assignment)
    var selectedBlock by remember { mutableStateOf("") }
    var selectedFlat by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Validation states
    var isNameValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isConfirmPasswordValid by remember { mutableStateOf(true) }
    var isPhoneValid by remember { mutableStateOf(true) }

    val shifts = listOf("Morning", "Evening", "Night")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Watchman",
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

            // Shift Assignment Card
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
                            text = "Shift Assignment",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Text(
                            text = "Select Shift",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(shifts) { shift ->
                                FilterChip(
                                    selected = selectedShift == shift.lowercase(),
                                    onClick = { selectedShift = shift.lowercase() },
                                    label = { Text(shift) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Secondary,
                                        selectedLabelColor = Color.White,
                                        labelColor = TextSecondary
                                    )
                                )
                            }
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
                                PasswordVisualTransformation(),
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
                                PasswordVisualTransformation(),
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

                        if (isNameValid && isEmailValid && isPhoneValid &&
                            isPasswordValid && isConfirmPasswordValid) {

                            authViewModel.createWatchman(
                                fullName = fullName.trim(),
                                email = email.trim(),
                                password = password.trim(),
                                phone = phone.trim(),
                                societyId = societyId,
                                societyName = societyName,  // ✅ Society name stored correctly
                                shift = selectedShift,
                                createdBy = currentUserId
                            ) { success, message ->
                                if (success) {
                                    createdUserId = message ?: ""

                                    // ✅ Save credentials WITHOUT flat assignment
                                    FirebaseFirestore.getInstance()
                                        .collection("user_credentials")
                                        .document(createdUserId)
                                        .set(
                                            mapOf(
                                                "userId" to createdUserId,
                                                "email" to email.trim(),
                                                "password" to password.trim(),
                                                "role" to "watchman",
                                                "societyId" to societyId,
                                                "societyName" to societyName,  // ✅ Society name stored
                                                "blockNumber" to "",  // ❌ REMOVED - empty string
                                                "flatNumber" to "",    // ❌ REMOVED - empty string
                                                "shift" to selectedShift
                                            )
                                        )
                                        .addOnSuccessListener {
                                            showDialog = true
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                context,
                                                "Watchman created but credential save failed: ${e.message}",
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
                            Toast.makeText(
                                context,
                                "Please fill all required fields correctly",
                                Toast.LENGTH_SHORT
                            ).show()
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
                            text = "Add Watchman",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    // ✅ Success Dialog - WITHOUT flat assignment
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
                    Text("OK", color = Primary)
                }
            },
            title = {
                Text(
                    text = "Watchman Created Successfully",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Success
                )
            },
            text = {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "User ID: $createdUserId",
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Email: $email",
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Password: $password",
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Shift: ${selectedShift.capitalize()}",
                        fontSize = 14.sp,
                        color = Secondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Society: $societyName",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }
        )
    }
}