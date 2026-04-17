package com.example.rakshasetu.ui.secretary

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendAnnouncementScreen(
    navController: NavController,
    userRole: String? = "secretary",
    societyId: String,
    createdByName: String
) {
    val viewModel: SecretaryViewModel = viewModel()
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()

    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf("normal") }
    var selectedAudience by remember { mutableStateOf("all") }

    // Validation states
    var isTitleValid by remember { mutableStateOf(true) }
    var isMessageValid by remember { mutableStateOf(true) }

    val priorities = listOf("normal", "high", "urgent")
    val audiences = listOf("all", "residents", "watchmen")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Send Announcement",
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
                            text = "Announcement Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Title
                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                                isTitleValid = true
                            },
                            label = { Text("Announcement Title *") },
                            leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
                            isError = !isTitleValid,
                            supportingText = {
                                if (!isTitleValid) {
                                    Text("Title is required", color = Error, fontSize = 12.sp)
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

                        Spacer(modifier = Modifier.height(12.dp))

                        // Message
                        OutlinedTextField(
                            value = message,
                            onValueChange = {
                                message = it
                                isMessageValid = true
                            },
                            label = { Text("Announcement Message *") },
                            leadingIcon = { Icon(Icons.Default.Message, contentDescription = null) },
                            isError = !isMessageValid,
                            supportingText = {
                                if (!isMessageValid) {
                                    Text("Message is required", color = Error, fontSize = 12.sp)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
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
                            text = "Priority",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            priorities.forEach { priority ->
                                FilterChip(
                                    selected = selectedPriority == priority,
                                    onClick = { selectedPriority = priority },
                                    label = {
                                        Text(
                                            when (priority) {
                                                "urgent" -> "Urgent"
                                                "high" -> "High"
                                                else -> "Normal"
                                            }
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = when (priority) {
                                            "urgent" -> Error
                                            "high" -> Warning
                                            else -> Primary
                                        },
                                        selectedLabelColor = Color.White,
                                        labelColor = TextSecondary
                                    )
                                )
                            }
                        }
                    }
                }
            }

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
                            text = "Target Audience",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            audiences.forEach { audience ->
                                FilterChip(
                                    selected = selectedAudience == audience,
                                    onClick = { selectedAudience = audience },
                                    label = {
                                        Text(
                                            when (audience) {
                                                "all" -> "All Members"
                                                "residents" -> "Residents Only"
                                                else -> "Watchmen Only"
                                            }
                                        )
                                    },
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

            item {
                Button(
                    onClick = {
                        // Validate inputs
                        isTitleValid = title.isNotBlank()
                        isMessageValid = message.isNotBlank()

                        if (isTitleValid && isMessageValid) {
                            viewModel.sendAnnouncement(
                                title = title.trim(),
                                message = message.trim(),
                                priority = selectedPriority,
                                audience = selectedAudience,
                                societyId = societyId,
                                createdByName = createdByName
                            ) { success, result ->
                                if (success) {
                                    Toast.makeText(
                                        context,
                                        "Announcement sent successfully!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed: $result",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill all required fields",
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
                            text = "Send Announcement",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}