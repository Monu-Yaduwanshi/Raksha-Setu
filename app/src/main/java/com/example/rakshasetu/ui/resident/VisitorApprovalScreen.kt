package com.example.rakshasetu.ui.resident

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.data.models.Visitor
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.ui.watchMan.StatusChip
//import com.example.rakshasetu.viewModel.viewModel.resident.ResidentViewModel
//import com.example.rakshasetu.viewModel.viewModel.resident.VisitorRequestItem
import kotlinx.coroutines.launch






            import android.util.Log

                    import androidx.compose.ui.layout.ContentScale

                    import coil.compose.AsyncImage
                    import com.google.firebase.Timestamp

                    import java.text.SimpleDateFormat
                    import java.util.*

                    @OptIn(ExperimentalMaterial3Api::class)
                    @Composable
                    fun VisitorApprovalScreen(
                        navController: NavController,
                        viewModel: ResidentViewModel = viewModel(),
                        userRole: String? = "resident"
                    ) {
                        val context = LocalContext.current
                        val scope = rememberCoroutineScope()
                        val isLoading by viewModel.isLoading.collectAsState()

                        val requestId = navController.currentBackStackEntry?.arguments?.getString("requestId") ?: ""
                        var visitor by remember { mutableStateOf<Visitor?>(null) }
                        var isLoadingRequest by remember { mutableStateOf(true) }

                        // Load the actual request from Firebase using Visitor type
                        LaunchedEffect(requestId) {
                            isLoadingRequest = true
                            visitor = viewModel.getVisitorById(requestId)

                            // Debug log for image URL
                            visitor?.let {
                                Log.d("VisitorApprovalScreen", "Visitor: ${it.name}")
                                Log.d("VisitorApprovalScreen", "Photo URL: ${it.visitorPhotoUrl}")
                            }

                            isLoadingRequest = false
                        }

                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text("Visitor Approval", color = Color.White) },
                                    navigationIcon = {
                                        IconButton(onClick = { navController.popBackStack() }) {
                                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                                        }
                                    },
                                    colors = TopAppBarDefaults.topAppBarColors(containerColor = SecretaryTopBarColor)
                                )
                            }
                        ) { paddingValues ->
                            when {
                                isLoadingRequest -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Background)
                                            .padding(paddingValues),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = Primary)
                                    }
                                }
                                visitor == null -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Background)
                                            .padding(paddingValues),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(Icons.Default.Error, contentDescription = null, tint = Error, modifier = Modifier.size(64.dp))
                                            Text("Request not found", fontSize = 16.sp, color = TextSecondary, modifier = Modifier.padding(top = 8.dp))
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Button(onClick = { navController.popBackStack() }) {
                                                Text("Go Back")
                                            }
                                        }
                                    }
                                }
                                else -> {
                                    val currentVisitor = visitor!!
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Background)
                                            .padding(paddingValues)
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        // Visitor Photo Card
                                        item {
                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = CardDefaults.cardColors(containerColor = Surface),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Column(
                                                    modifier = Modifier.padding(16.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    // ✅ FIXED: Load actual visitor photo using AsyncImage
                                                    if (currentVisitor.visitorPhotoUrl.isNotBlank()) {
                                                        AsyncImage(
                                                            model = currentVisitor.visitorPhotoUrl,
                                                            contentDescription = "Visitor Photo",
                                                            modifier = Modifier
                                                                .size(120.dp)
                                                                .clip(CircleShape),
                                                            contentScale = ContentScale.Crop
                                                        )
                                                    } else {
                                                        // Fallback icon when no photo URL
                                                        Box(
                                                            modifier = Modifier
                                                                .size(120.dp)
                                                                .clip(CircleShape)
                                                                .background(Primary.copy(alpha = 0.1f)),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Icon(
                                                                Icons.Default.Person,
                                                                contentDescription = null,
                                                                tint = Primary,
                                                                modifier = Modifier.size(60.dp)
                                                            )
                                                        }
                                                    }

                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        text = currentVisitor.name,
                                                        fontSize = 20.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = TextPrimary
                                                    )
                                                    StatusChip(status = currentVisitor.status)
                                                }
                                            }
                                        }

                                        // Visitor Details Card
                                        item {
                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = CardDefaults.cardColors(containerColor = Surface),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Column(modifier = Modifier.padding(16.dp)) {
                                                    Text(
                                                        text = "Visitor Details",
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = TextPrimary,
                                                        modifier = Modifier.padding(bottom = 12.dp)
                                                    )

                                                    DetailRow("Phone", currentVisitor.phone, Icons.Default.Phone)
                                                    DetailRow("Purpose", currentVisitor.purpose, Icons.Default.Info)
                                                    if (currentVisitor.organization.isNotBlank()) {
                                                        DetailRow("Organization", currentVisitor.organization, Icons.Default.Business)
                                                    }
                                                    DetailRow("Visitors", currentVisitor.numberOfVisitors.toString(), Icons.Default.Group)
                                                    if (currentVisitor.vehicleNumber.isNotBlank()) {
                                                        DetailRow("Vehicle", currentVisitor.vehicleNumber, Icons.Default.DirectionsCar)
                                                    }
                                                    DetailRow("Arrival", viewModel.formatTime(currentVisitor.entryTime), Icons.Default.Schedule)
                                                    DetailRow("Watchman", currentVisitor.watchmanName, Icons.Default.Security)
                                                }
                                            }
                                        }

                                        // Notes Card
                                        if (currentVisitor.notes.isNotBlank()) {
                                            item {
                                                Card(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    colors = CardDefaults.cardColors(containerColor = Surface),
                                                    shape = RoundedCornerShape(12.dp)
                                                ) {
                                                    Column(modifier = Modifier.padding(16.dp)) {
                                                        Text(
                                                            text = "Notes",
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = TextPrimary,
                                                            modifier = Modifier.padding(bottom = 8.dp)
                                                        )
                                                        Text(
                                                            text = currentVisitor.notes,
                                                            fontSize = 14.sp,
                                                            color = TextSecondary
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        // Action Buttons
                                        item {
                                            Column(
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                // Approve Button
                                                Button(
                                                    onClick = {
                                                        currentVisitor.id.let {
                                                            viewModel.approveVisitor(it)
                                                            Toast.makeText(context, "Visitor Approved", Toast.LENGTH_SHORT).show()
                                                            navController.popBackStack()
                                                        }
                                                    },
                                                    modifier = Modifier.fillMaxWidth().height(50.dp),
                                                    colors = ButtonDefaults.buttonColors(containerColor = Success),
                                                    shape = RoundedCornerShape(8.dp),
                                                    enabled = !isLoading
                                                ) {
                                                    if (isLoading) {
                                                        CircularProgressIndicator(
                                                            color = Color.White,
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    } else {
                                                        Icon(Icons.Default.Check, contentDescription = null)
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text("Allow Entry")
                                                    }
                                                }

                                                // Reject Button
                                                Button(
                                                    onClick = {
                                                        currentVisitor.id.let {
                                                            viewModel.rejectVisitor(it)
                                                            Toast.makeText(context, "Visitor Rejected", Toast.LENGTH_SHORT).show()
                                                            navController.popBackStack()
                                                        }
                                                    },
                                                    modifier = Modifier.fillMaxWidth().height(50.dp),
                                                    colors = ButtonDefaults.buttonColors(containerColor = Error),
                                                    shape = RoundedCornerShape(8.dp),
                                                    enabled = !isLoading
                                                ) {
                                                    Icon(Icons.Default.Close, contentDescription = null)
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text("Deny Entry")
                                                }

                                                // Leave at Gate Button
                                                Button(
                                                    onClick = {
                                                        currentVisitor.id.let {
                                                            viewModel.leaveAtGate(it)
                                                            Toast.makeText(context, "Leave at Gate", Toast.LENGTH_SHORT).show()
                                                            navController.popBackStack()
                                                        }
                                                    },
                                                    modifier = Modifier.fillMaxWidth().height(50.dp),
                                                    colors = ButtonDefaults.buttonColors(containerColor = Warning),
                                                    shape = RoundedCornerShape(8.dp),
                                                    enabled = !isLoading
                                                ) {
                                                    Icon(Icons.Default.HourglassEmpty, contentDescription = null)
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text("Leave at Gate")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

            @Composable
            fun DetailRow(
                label: String,
                value: String,
                icon: androidx.compose.ui.graphics.vector.ImageVector
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "$label:",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        modifier = Modifier.width(80.dp)
                    )
                    Text(
                        text = value,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

//            @Composable
//            fun StatusChip(status: String) {
//                val (color, backgroundColor) = when (status.lowercase()) {
//                    "approved" -> Success to Success.copy(alpha = 0.1f)
//                    "rejected" -> Error to Error.copy(alpha = 0.1f)
//                    "pending" -> Warning to Warning.copy(alpha = 0.1f)
//                    "leave_at_gate" -> TealMain to TealMain.copy(alpha = 0.1f)
//                    else -> TextSecondary to TextSecondary.copy(alpha = 0.1f)
//                }
//
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(20.dp))
//                        .background(backgroundColor)
//                        .padding(horizontal = 12.dp, vertical = 6.dp)
//                ) {
//                    Text(
//                        text = when (status.lowercase()) {
//                            "approved" -> "Approved"
//                            "rejected" -> "Rejected"
//                            "pending" -> "Pending"
//                            "leave_at_gate" -> "Leave at Gate"
//                            else -> status
//                        },
//                        fontSize = 11.sp,
//                        fontWeight = FontWeight.Medium,
//                        color = color
//                    )
//                }
//            }

            // Helper function for formatting date and time
//            fun formatDateTime(timestamp: Timestamp?): String {
//                if (timestamp == null) return "N/A"
//                val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
//                return dateFormat.format(timestamp.toDate())
//            }

            // Helper function for formatting time only
            fun formatTimeOnly(timestamp: Timestamp?): String {
                if (timestamp == null) return "N/A"
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                return timeFormat.format(timestamp.toDate())
            }