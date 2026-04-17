package com.example.rakshasetu.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rakshasetu.core.components.BottomNavigationBar
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.resident.SecretaryTopBarColor
//import com.example.rakshasetu.ui.secretary.SecretaryTopBarColor
//import com.example.rakshasetu.ui.secretary.SecretaryTopBarColor
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userRole: String? = null  // ADD THIS PARAMETER
) {
    val viewModel: AuthViewModel = viewModel()
    val userData by viewModel.userData.collectAsState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var isEditing by remember { mutableStateOf(false) }

    // Editable fields
    var fullName by remember { mutableStateOf(userData?.fullName ?: "") }
    var phone by remember { mutableStateOf(userData?.phone ?: "") }
    var flatNumber by remember { mutableStateOf(userData?.flatNumber ?: "") }
    var blockNumber by remember { mutableStateOf(userData?.blockNumber ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
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
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute,
                userRole = userRole
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
            // Profile Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Image
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userData?.fullName?.firstOrNull()?.toString() ?: "U",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Edit Profile Button
                    Button(
                        onClick = { isEditing = !isEditing },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEditing) Success else Primary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isEditing) "Save" else "Edit Profile")
                    }
                }
            }

            // Personal Information
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Personal Information",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        if (isEditing) {
                            // Editable fields
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = { Text("Full Name") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = TextHint
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("Phone Number") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = TextHint
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        } else {
                            ProfileInfoRow("Full Name", userData?.fullName ?: "N/A", Icons.Default.Person)
                            ProfileInfoRow("Email", userData?.email ?: "N/A", Icons.Default.Email)
                            ProfileInfoRow("Phone", userData?.phone ?: "N/A", Icons.Default.Phone)
                            ProfileInfoRow("Role", userData?.role?.replaceFirstChar { it.uppercase() } ?: "N/A", Icons.Default.Work)
                        }
                    }
                }
            }

            // Society Information
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Society Information",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        if (isEditing && userData?.role == "resident") {
                            OutlinedTextField(
                                value = blockNumber,
                                onValueChange = { blockNumber = it },
                                label = { Text("Block Number") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = TextHint
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = flatNumber,
                                onValueChange = { flatNumber = it },
                                label = { Text("Flat Number") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = TextHint
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        } else {
                            ProfileInfoRow("Society Name", userData?.societyName ?: "N/A", Icons.Default.House)
                            if (userData?.role == "resident") {
                                ProfileInfoRow("Block", userData?.blockNumber ?: "N/A", Icons.Default.Apartment)
                                ProfileInfoRow("Flat", userData?.flatNumber ?: "N/A", Icons.Default.House)
                            }
                            if (userData?.role == "watchman") {
                                ProfileInfoRow("Gate", "Main Gate", Icons.Default.Security)
                                ProfileInfoRow("Shift", "Morning", Icons.Default.Schedule)
                            }
                        }
                    }
                }
            }

            // Account Settings
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Account Settings",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        SettingsItem("Change Password", Icons.Default.Lock, Primary)
                        SettingsItem("Notification Settings", Icons.Default.Notifications, Secondary)
                        SettingsItem("Privacy Settings", Icons.Default.PrivacyTip, TealMain)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSecondary
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun SettingsItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            fontSize = 15.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextHint,
            modifier = Modifier.size(20.dp)
        )
    }
}