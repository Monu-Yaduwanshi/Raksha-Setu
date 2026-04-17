package com.example.rakshasetu.ui.Community

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel
import com.example.rakshasetu.viewModel.viewModel.auth.CommunityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinCommunityScreen(
    navController: NavController,
    userRole: String? = null
) {
    val context = LocalContext.current
    val viewModel: CommunityViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val userData by authViewModel.userData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Get societyId from navigation arguments - FIXED
    val societyId = navController.currentBackStackEntry
        ?.arguments?.getString("societyId") ?: ""

    // Form fields based on role
    var flatNumber by remember { mutableStateOf("") }
    var blockNumber by remember { mutableStateOf("") }
    var shift by remember { mutableStateOf("morning") }

    // Validation states
    var isFlatValid by remember { mutableStateOf(true) }
    var isBlockValid by remember { mutableStateOf(true) }

    val userRoleValue = userData?.role ?: "resident"
    val shifts = listOf("Morning", "Evening", "Night")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Join Society",
                        color = Surface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Surface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Surface
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Join Request",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "You are requesting to join as:",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = userRoleValue.replaceFirstChar { it.uppercase() },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Role-specific fields
            if (userRoleValue == "resident") {
                // Flat Number
                OutlinedTextField(
                    value = flatNumber,
                    onValueChange = {
                        flatNumber = it
                        isFlatValid = true
                    },
                    label = { Text("Flat Number", color = TextSecondary) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Flat",
                            tint = Primary
                        )
                    },
                    isError = !isFlatValid,
                    supportingText = {
                        if (!isFlatValid) {
                            Text(
                                text = "Flat number is required",
                                color = Error,
                                fontSize = 12.sp
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Surface,
                        unfocusedContainerColor = Surface,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = TextHint,
                        cursorColor = Primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Block Number
                OutlinedTextField(
                    value = blockNumber,
                    onValueChange = {
                        blockNumber = it
                        isBlockValid = true
                    },
                    label = { Text("Block Number", color = TextSecondary) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Apartment,
                            contentDescription = "Block",
                            tint = Primary
                        )
                    },
                    isError = !isBlockValid,
                    supportingText = {
                        if (!isBlockValid) {
                            Text(
                                text = "Block number is required",
                                color = Error,
                                fontSize = 12.sp
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Surface,
                        unfocusedContainerColor = Surface,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = TextHint,
                        cursorColor = Primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            } else if (userRoleValue == "watchman") {
                // Shift Selection
                Text(
                    text = "Select Shift",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    shifts.forEach { shiftOption ->
                        FilterChip(
                            selected = shift == shiftOption.lowercase(),
                            onClick = { shift = shiftOption.lowercase() },
                            label = { Text(shiftOption) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Primary,
                                selectedLabelColor = Surface,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    if (userRoleValue == "resident") {
                        isFlatValid = flatNumber.isNotBlank()
                        isBlockValid = blockNumber.isNotBlank()

                        if (!isFlatValid || !isBlockValid) {
                            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                    }

                    viewModel.requestToJoin(
                        userId = userData?.userId ?: "",
                        userName = userData?.fullName ?: "",
                        userEmail = userData?.email ?: "",
                        userPhone = userData?.phone ?: "",
                        userRole = userRoleValue,
                        societyId = societyId,
                        societyName = "", // Will be fetched
                        flatNumber = flatNumber,
                        blockNumber = blockNumber,
                        shift = shift
                    ) { success, message ->
                        if (success) {
                            Toast.makeText(
                                context,
                                "Join request sent successfully! Waiting for secretary approval.",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(
                                context,
                                "Failed: $message",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonPrimary
                ),
                shape = RoundedCornerShape(14.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Surface,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Send Join Request",
                        color = Surface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}