//package com.example.rakshasetu.ui.Community

package com.example.rakshasetu.ui.Community

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.data.models.JoinRequest
import com.example.rakshasetu.ui.secretary.SecretaryTopBarColor
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.ui.secretary.SecretaryViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinRequestsScreen(
    navController: NavController,
    userRole: String? = "secretary",
    societyId: String
) {
    val viewModel: SecretaryViewModel = viewModel()
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val joinRequests by viewModel.joinRequests.collectAsState()

    val currentUserId = viewModel.getCurrentUserId() ?: ""

    var selectedRequest by remember { mutableStateOf<JoinRequest?>(null) }
    var showActionDialog by remember { mutableStateOf(false) }

    LaunchedEffect(societyId) {
        viewModel.loadJoinRequests(societyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Join Requests",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Primary
                )
            } else if (joinRequests.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.GroupAdd,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "No pending join requests",
                        fontSize = 16.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(joinRequests) { request ->
                        JoinRequestCard(
                            request = request,
                            onApprove = {
                                selectedRequest = request
                                showActionDialog = true
                            },
                            onReject = {
                                selectedRequest = request
                                showActionDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Action Dialog
    if (showActionDialog && selectedRequest != null) {
        val request = selectedRequest!!
        AlertDialog(
            onDismissRequest = { showActionDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (request.userRole == "resident") Icons.Default.Home
                        else Icons.Default.Security,
                        contentDescription = null,
                        tint = Primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${request.userRole?.replaceFirstChar { it.uppercase() }} Request")
                }
            },
            text = {
                Column {
                    Text(
                        text = "Name: ${request.userName}",
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Email: ${request.userEmail}",
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Phone: ${request.userPhone}",
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    if (request.userRole == "resident") {
                        Text(
                            text = "Flat: ${request.blockNumber}-${request.flatNumber}",
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                    } else {
                        Text(
                            text = "Shift: ${request.shift?.replaceFirstChar { it.uppercase() }}",
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.respondToRequest(
                            request = request,
                            approved = true,
                            respondedBy = currentUserId
                        ) { success, message ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Request approved",
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewModel.loadJoinRequests(societyId)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error: $message",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            showActionDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Success
                    )
                ) {
                    Text("Approve", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        viewModel.respondToRequest(
                            request = request,
                            approved = false,
                            respondedBy = currentUserId
                        ) { success, message ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Request rejected",
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewModel.loadJoinRequests(societyId)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error: $message",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            showActionDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Error
                    )
                ) {
                    Text("Reject", color = Color.White)
                }
            }
        )
    }
}

@Composable
fun JoinRequestCard(
    request: JoinRequest,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (request.userRole == "resident")
                                Primary.copy(alpha = 0.1f)
                            else
                                Secondary.copy(alpha = 0.1f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (request.userRole == "resident")
                            Icons.Default.Home
                        else
                            Icons.Default.Security,
                        contentDescription = null,
                        tint = if (request.userRole == "resident") Primary else Secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = request.userName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = request.userRole?.replaceFirstChar { it.uppercase() } ?: "",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }

                // Time
                request.requestedAt?.toDate()?.let { date ->
                    val timeAgo = getTimeAgo(date)
                    Text(
                        text = timeAgo,
                        fontSize = 12.sp,
                        color = TextHint
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Details
            Column {
                DetailRow("Email", request.userEmail, Icons.Default.Email)
                DetailRow("Phone", request.userPhone, Icons.Default.Phone)

                if (request.userRole == "resident") {
                    DetailRow(
                        "Flat",
                        "${request.blockNumber}-${request.flatNumber}",
                        Icons.Default.Home
                    )
                } else {
                    DetailRow("Shift", request.shift?.replaceFirstChar { it.uppercase() } ?: "", Icons.Default.Schedule)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Success
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Approve")
                }

                Button(
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Error
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reject")
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = TextHint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label:",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.width(60.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

fun getTimeAgo(date: Date): String {
    val now = Date()
    val diffInSeconds = (now.time - date.time) / 1000

    return when {
        diffInSeconds < 60 -> "Just now"
        diffInSeconds < 3600 -> "${diffInSeconds / 60} min ago"
        diffInSeconds < 86400 -> "${diffInSeconds / 3600} hours ago"
        else -> "${diffInSeconds / 86400} days ago"
    }
}