package com.example.rakshasetu.ui.watchMan

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.rakshasetu.core.utils.safeClickable
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.watchman.WatchmanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendEmergencyMessageScreen(
    navController: NavController,
    userRole: String? = "watchman"
) {
    val viewModel: WatchmanViewModel = viewModel()
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()

    var message by remember { mutableStateOf("🚨 EMERGENCY ALERT 🚨\nPlease be alert! There is an emergency situation in the society.") }
    var showPreview by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Emergency Alert",
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
                    containerColor = Error
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
                        containerColor = Error.copy(alpha = 0.1f)
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
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = Error,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "This will send an alert to ALL society members",
                            fontSize = 14.sp,
                            color = Error,
                            fontWeight = FontWeight.Medium
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
                            text = "Emergency Message",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            label = { Text("Message") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Error,
                                unfocusedBorderColor = TextHint
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "You can edit this message before sending",
                            fontSize = 12.sp,
                            color = TextHint
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
                            text = "Quick Templates",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Simple row layout for templates
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TemplateChip(
                                    text = "Fire Emergency",
                                    onClick = {
                                        message = "🚨 FIRE EMERGENCY 🚨\nFire reported in the society. Please evacuate immediately and follow safety protocols."
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                TemplateChip(
                                    text = "Medical Emergency",
                                    onClick = {
                                        message = "🚨 MEDICAL EMERGENCY 🚨\nMedical emergency in the society. Please clear the pathways for ambulance."
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TemplateChip(
                                    text = "Security Alert",
                                    onClick = {
                                        message = "🚨 SECURITY ALERT 🚨\nSuspicious activity reported. Please stay alert and lock your doors."
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                TemplateChip(
                                    text = "Power Outage",
                                    onClick = {
                                        message = "⚠️ POWER OUTAGE ⚠️\nThere is a power outage in the society. Please use emergency lights and avoid elevators."
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        showPreview = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Error,
                        disabledContainerColor = Error.copy(alpha = 0.5f)
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
                            text = "Preview & Send Alert",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    // Preview Dialog
    if (showPreview) {
        AlertDialog(
            onDismissRequest = { showPreview = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Error)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Emergency Alert Preview")
                }
            },
            text = {
                Column {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Error.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = message,
                            modifier = Modifier.padding(16.dp),
                            color = Error,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This alert will be sent to:",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "• All residents\n• All watchmen\n• Security team",
                        fontSize = 14.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.sendEmergencyAlert(message) { success, result ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Emergency alert sent to all members",
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
                            showPreview = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Error
                    )
                ) {
                    Text("Send Alert", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPreview = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TemplateChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Surface,
        border = BorderStroke(1.dp, Error),
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .safeClickable { onClick() }
                //.clickable { onClick() }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                color = TextPrimary,
                maxLines = 1
            )
        }
    }
}
