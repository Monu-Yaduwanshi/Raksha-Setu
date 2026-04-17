package com.example.rakshasetu.ui.resident


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.ui.watchMan.StatusChip
//import com.example.rakshasetu.viewModel.viewModel.resident.ResidentViewModel
//import com.example.rakshasetu.viewModel.viewModel.resident.VisitorRequestItem


import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.rakshasetu.data.models.Visitor
import com.google.firebase.Timestamp

import com.google.firebase.auth.FirebaseAuth

import java.text.SimpleDateFormat
import java.util.*



import androidx.compose.ui.layout.ContentScale

import coil.compose.AsyncImage
import com.example.rakshasetu.ui.watchMan.SecretaryTopBarColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitorRequestsScreen(
    navController: NavController,
    userRole: String? = "resident",
    viewModel: ResidentViewModel = viewModel()
) {
    val context = LocalContext.current
    val pendingVisitors by viewModel.pendingVisitors.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val currentUser = FirebaseAuth.getInstance().currentUser

    // Load pending visitors for this resident
    LaunchedEffect(Unit) {
        val residentId = currentUser?.uid
        Log.d("VisitorRequestsScreen", "Loading requests for residentId: $residentId")
        if (residentId != null) {
            viewModel.loadPendingVisitors(residentId)
        } else {
            Log.e("VisitorRequestsScreen", "No logged in user found!")
            Toast.makeText(context, "Please login again", Toast.LENGTH_SHORT).show()
        }
    }

    // Show error toast if needed
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Log.e("VisitorRequestsScreen", "Error: $it")
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    // Debug logging
    LaunchedEffect(pendingVisitors) {
        Log.d("VisitorRequestsScreen", "Pending visitors count: ${pendingVisitors.size}")
        pendingVisitors.forEach { visitor ->
            Log.d("VisitorRequestsScreen", "  - ${visitor.name} (${visitor.purpose}) from ${visitor.flatNumber}")
            Log.d("VisitorRequestsScreen", "    Photo URL: ${visitor.visitorPhotoUrl.take(50)}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Visitor Requests",
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
            when {
                isLoading && pendingVisitors.isEmpty() -> {
                    // Loading state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading requests...",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }
                pendingVisitors.isEmpty() -> {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Success,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Pending Requests",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "All visitor requests have been processed",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Go Back")
                        }
                    }
                }
                else -> {
                    // List of pending requests
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(pendingVisitors) { visitor ->
                            VisitorRequestCard(
                                visitor = visitor,
                                onApprove = {
                                    Log.d("VisitorRequestsScreen", "Approving visitor: ${visitor.id}")
                                    viewModel.approveVisitor(visitor.id)
                                    Toast.makeText(
                                        context,
                                        "${visitor.name} approved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onReject = {
                                    Log.d("VisitorRequestsScreen", "Rejecting visitor: ${visitor.id}")
                                    viewModel.rejectVisitor(visitor.id)
                                    Toast.makeText(
                                        context,
                                        "${visitor.name} rejected",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onLeaveAtGate = {
                                    Log.d("VisitorRequestsScreen", "Leave at gate for visitor: ${visitor.id}")
                                    viewModel.leaveAtGate(visitor.id)
                                    Toast.makeText(
                                        context,
                                        "${visitor.name} - leave at gate",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VisitorRequestCard(
    visitor: Visitor,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onLeaveAtGate: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row with Photo and Name
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ✅ FIXED: Show actual visitor photo using AsyncImage
                if (visitor.visitorPhotoUrl.isNotEmpty()) {
                    AsyncImage(
                        model = visitor.visitorPhotoUrl,
                        contentDescription = "Visitor Photo",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Fallback icon when no photo URL
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = visitor.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Flat: ${visitor.flatNumber}",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "Watchman: ${visitor.watchmanName}",
                        fontSize = 12.sp,
                        color = TextHint
                    )
                }

                // Status Chip
                StatusChip(status = visitor.status)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Divider
            Divider(
                color = TextHint.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Visitor Details Grid
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Purpose Row
                DetailRow(
                    label = "Purpose",
                    value = visitor.purpose,
                    icon = when (visitor.purpose.lowercase()) {
                        "delivery" -> Icons.Default.LocalShipping
                        "guest" -> Icons.Default.Person
                        "cab" -> Icons.Default.TaxiAlert
                        else -> Icons.Default.Info
                    }
                )

                // Organization Row (if present)
                if (visitor.organization.isNotBlank()) {
                    DetailRow(
                        label = "Organization",
                        value = visitor.organization,
                        icon = Icons.Default.Business
                    )
                }

                // Number of Visitors Row
                if (visitor.numberOfVisitors > 1) {
                    DetailRow(
                        label = "Visitors",
                        value = "${visitor.numberOfVisitors} people",
                        icon = Icons.Default.Group
                    )
                }

                // Vehicle Number Row (if present)
                if (visitor.vehicleNumber.isNotBlank()) {
                    DetailRow(
                        label = "Vehicle",
                        value = visitor.vehicleNumber,
                        icon = Icons.Default.DirectionsCar
                    )
                }

                // Phone Row (if present)
                if (visitor.phone.isNotBlank()) {
                    DetailRow(
                        label = "Phone",
                        value = visitor.phone,
                        icon = Icons.Default.Phone
                    )
                }

                // Arrival Time Row
                DetailRow(
                    label = "Arrived",
                    value = formatDateTime(visitor.entryTime),
                    icon = Icons.Default.Schedule
                )

                // Notes Row (if present)
                if (visitor.notes.isNotBlank()) {
                    DetailRow(
                        label = "Notes",
                        value = visitor.notes,
                        icon = Icons.Default.Notes
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Approve Button
                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Success),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Allow", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                // Reject Button
                Button(
                    onClick = onReject,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Error),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Deny", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                // Leave at Gate Button
                Button(
                    onClick = onLeaveAtGate,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Warning),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.HourglassEmpty,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gate", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

//@Composable
//fun DetailRow(
//    label: String,
//    value: String,
//    icon: androidx.compose.ui.graphics.vector.ImageVector
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.Top
//    ) {
//        Icon(
//            icon,
//            contentDescription = null,
//            tint = Primary,
//            modifier = Modifier.size(18.dp)
//        )
//        Spacer(modifier = Modifier.width(12.dp))
//        Text(
//            text = "$label:",
//            fontSize = 13.sp,
//            color = TextSecondary,
//            modifier = Modifier.width(85.dp)
//        )
//        Text(
//            text = value,
//            fontSize = 13.sp,
//            color = TextPrimary,
//            fontWeight = FontWeight.Medium,
//            modifier = Modifier.weight(1f)
//        )
//    }
//}

@Composable
fun StatusChip(status: String) {
    val (color, backgroundColor) = when (status.lowercase()) {
        "approved" -> Success to Success.copy(alpha = 0.1f)
        "rejected" -> Error to Error.copy(alpha = 0.1f)
        "pending" -> Warning to Warning.copy(alpha = 0.1f)
        "leave_at_gate" -> TealMain to TealMain.copy(alpha = 0.1f)
        else -> TextSecondary to TextSecondary.copy(alpha = 0.1f)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = when (status.lowercase()) {
                "approved" -> "Approved"
                "rejected" -> "Rejected"
                "pending" -> "Pending"
                "leave_at_gate" -> "Leave at Gate"
                else -> status
            },
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

// Helper function for formatting date and time
fun formatDateTime(timestamp: Timestamp?): String {
    if (timestamp == null) return "N/A"
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return dateFormat.format(timestamp.toDate())
}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun VisitorRequestsScreen(
//    navController: NavController,
//    userRole: String? = "resident",
//    viewModel: ResidentViewModel = viewModel()
//) {
//    val context = LocalContext.current
//    val pendingVisitors by viewModel.pendingVisitors.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
//
//    val currentUser = FirebaseAuth.getInstance().currentUser
//
//    // ✅ Load pending visitors for this resident
//    LaunchedEffect(Unit) {
//        val residentId = currentUser?.uid
//        Log.d("VisitorRequestsScreen", "Loading requests for residentId: $residentId")
//        if (residentId != null) {
//            viewModel.loadPendingVisitors(residentId)
//        } else {
//            Log.e("VisitorRequestsScreen", "No logged in user found!")
//            Toast.makeText(context, "Please login again", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Show error toast if needed
//    LaunchedEffect(errorMessage) {
//        errorMessage?.let {
//            Log.e("VisitorRequestsScreen", "Error: $it")
//            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//            viewModel.clearError()
//        }
//    }
//
//    // Debug logging
//    LaunchedEffect(pendingVisitors) {
//        Log.d("VisitorRequestsScreen", "Pending visitors count: ${pendingVisitors.size}")
//        pendingVisitors.forEach { visitor ->
//            Log.d("VisitorRequestsScreen", "  - ${visitor.name} (${visitor.purpose}) from ${visitor.flatNumber}")
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Visitor Requests",
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
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Background)
//                .padding(paddingValues)
//        ) {
//            when {
//                isLoading && pendingVisitors.isEmpty() -> {
//                    // Loading state
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        CircularProgressIndicator(
//                            color = Primary,
//                            modifier = Modifier.size(48.dp)
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Text(
//                            text = "Loading requests...",
//                            fontSize = 14.sp,
//                            color = TextSecondary
//                        )
//                    }
//                }
//                pendingVisitors.isEmpty() -> {
//                    // Empty state
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Icon(
//                            Icons.Default.CheckCircle,
//                            contentDescription = null,
//                            tint = Success,
//                            modifier = Modifier.size(80.dp)
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Text(
//                            text = "No Pending Requests",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary
//                        )
//                        Text(
//                            text = "All visitor requests have been processed",
//                            fontSize = 14.sp,
//                            color = TextSecondary,
//                            modifier = Modifier.padding(top = 4.dp)
//                        )
//                        Spacer(modifier = Modifier.height(24.dp))
//                        Button(
//                            onClick = { navController.popBackStack() },
//                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Text("Go Back")
//                        }
//                    }
//                }
//                else -> {
//                    // List of pending requests
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        items(pendingVisitors) { visitor ->
//                            VisitorRequestCard(
//                                visitor = visitor,
//                                onApprove = {
//                                    Log.d("VisitorRequestsScreen", "Approving visitor: ${visitor.id}")
//                                    viewModel.approveVisitor(visitor.id)
//                                    Toast.makeText(
//                                        context,
//                                        "${visitor.name} approved",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                },
//                                onReject = {
//                                    Log.d("VisitorRequestsScreen", "Rejecting visitor: ${visitor.id}")
//                                    viewModel.rejectVisitor(visitor.id)
//                                    Toast.makeText(
//                                        context,
//                                        "${visitor.name} rejected",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                },
//                                onLeaveAtGate = {
//                                    Log.d("VisitorRequestsScreen", "Leave at gate for visitor: ${visitor.id}")
//                                    viewModel.leaveAtGate(visitor.id)
//                                    Toast.makeText(
//                                        context,
//                                        "${visitor.name} - leave at gate",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                            )
//                        }
//
//                        item {
//                            Spacer(modifier = Modifier.height(16.dp))
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun VisitorRequestCard(
//    visitor: Visitor,
//    onApprove: () -> Unit,
//    onReject: () -> Unit,
//    onLeaveAtGate: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth(),
//        colors = CardDefaults.cardColors(containerColor = Surface),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            // Header Row with Photo and Name
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Visitor Photo
//                Box(
//                    modifier = Modifier
//                        .size(60.dp)
//                        .clip(CircleShape)
//                        .background(Primary.copy(alpha = 0.1f)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    if (visitor.imageUrl.isNotBlank()) {
//                        // In production, use Coil or Glide:
//                        // AsyncImage(model = visitor.imageUrl, contentDescription = null)
//                        Icon(
//                            Icons.Default.Person,
//                            contentDescription = null,
//                            tint = Primary,
//                            modifier = Modifier.size(35.dp)
//                        )
//                    } else {
//                        Icon(
//                            Icons.Default.Person,
//                            contentDescription = null,
//                            tint = Primary,
//                            modifier = Modifier.size(35.dp)
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        text = visitor.name,
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = TextPrimary
//                    )
//                    Text(
//                        text = "Flat: ${visitor.flatNumber}",
//                        fontSize = 14.sp,
//                        color = TextSecondary
//                    )
//                    Text(
//                        text = "Watchman: ${visitor.watchmanName}",
//                        fontSize = 12.sp,
//                        color = TextHint
//                    )
//                }
//
//                // Status Chip
//                StatusChip(status = visitor.status)
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Divider
//            Divider(
//                color = TextHint.copy(alpha = 0.2f),
//                thickness = 1.dp
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // Visitor Details Grid
//            Column(
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                // Purpose Row
//                DetailRow(
//                    label = "Purpose",
//                    value = visitor.purpose,
//                    icon = when (visitor.purpose.lowercase()) {
//                        "delivery" -> Icons.Default.LocalShipping
//                        "guest" -> Icons.Default.Person
//                        "cab" -> Icons.Default.TaxiAlert
//                        else -> Icons.Default.Info
//                    }
//                )
//
//                // Organization Row (if present)
//                if (visitor.organization.isNotBlank()) {
//                    DetailRow(
//                        label = "Organization",
//                        value = visitor.organization,
//                        icon = Icons.Default.Business
//                    )
//                }
//
//                // Number of Visitors Row
//                if (visitor.numberOfVisitors > 1) {
//                    DetailRow(
//                        label = "Visitors",
//                        value = "${visitor.numberOfVisitors} people",
//                        icon = Icons.Default.Group
//                    )
//                }
//
//                // Vehicle Number Row (if present)
//                if (visitor.vehicleNumber.isNotBlank()) {
//                    DetailRow(
//                        label = "Vehicle",
//                        value = visitor.vehicleNumber,
//                        icon = Icons.Default.DirectionsCar
//                    )
//                }
//
//                // Phone Row (if present)
//                if (visitor.phone.isNotBlank()) {
//                    DetailRow(
//                        label = "Phone",
//                        value = visitor.phone,
//                        icon = Icons.Default.Phone
//                    )
//                }
//
//                // Arrival Time Row
//                DetailRow(
//                    label = "Arrived",
//                    value = formatDateTime(visitor.entryTime),
//                    icon = Icons.Default.Schedule
//                )
//
//                // Notes Row (if present)
//                if (visitor.notes.isNotBlank()) {
//                    DetailRow(
//                        label = "Notes",
//                        value = visitor.notes,
//                        icon = Icons.Default.Notes
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // Action Buttons
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                // Approve Button
//                Button(
//                    onClick = onApprove,
//                    modifier = Modifier.weight(1f).height(48.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Success),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Icon(
//                        Icons.Default.Check,
//                        contentDescription = null,
//                        modifier = Modifier.size(18.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Allow", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                }
//
//                // Reject Button
//                Button(
//                    onClick = onReject,
//                    modifier = Modifier.weight(1f).height(48.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Error),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Icon(
//                        Icons.Default.Close,
//                        contentDescription = null,
//                        modifier = Modifier.size(18.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Deny", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                }
//
//                // Leave at Gate Button
//                Button(
//                    onClick = onLeaveAtGate,
//                    modifier = Modifier.weight(1f).height(48.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Warning),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Icon(
//                        Icons.Default.HourglassEmpty,
//                        contentDescription = null,
//                        modifier = Modifier.size(18.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Gate", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                }
//            }
//        }
//    }
//}
//
//
//
//@Composable
//fun StatusChip(status: String) {
//    val (color, backgroundColor) = when (status.lowercase()) {
//        "approved" -> Success to Success.copy(alpha = 0.1f)
//        "rejected" -> Error to Error.copy(alpha = 0.1f)
//        "pending" -> Warning to Warning.copy(alpha = 0.1f)
//        "leave_at_gate" -> TealMain to TealMain.copy(alpha = 0.1f)
//        else -> TextSecondary to TextSecondary.copy(alpha = 0.1f)
//    }
//
//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(20.dp))
//            .background(backgroundColor)
//            .padding(horizontal = 12.dp, vertical = 6.dp)
//    ) {
//        Text(
//            text = when (status.lowercase()) {
//                "approved" -> "Approved"
//                "rejected" -> "Rejected"
//                "pending" -> "Pending"
//                "leave_at_gate" -> "Leave at Gate"
//                else -> status
//            },
//            fontSize = 11.sp,
//            fontWeight = FontWeight.Medium,
//            color = color
//        )
//    }
//}
//
//// Helper function for formatting date and time
//fun formatDateTime(timestamp: Timestamp?): String {
//    if (timestamp == null) return "N/A"
//    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
//    return dateFormat.format(timestamp.toDate())
//}
//
//// Helper function for formatting time only
//fun formatTimeOnly(timestamp: Timestamp?): String {
//    if (timestamp == null) return "N/A"
//    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
//    return timeFormat.format(timestamp.toDate())
//}