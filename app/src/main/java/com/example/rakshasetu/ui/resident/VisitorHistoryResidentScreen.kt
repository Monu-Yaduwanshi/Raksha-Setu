package com.example.rakshasetu.ui.resident

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
import java.text.SimpleDateFormat

import java.util.*

import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitorHistoryResidentScreen(
    navController: NavController,
    userRole: String? = "resident"
) {
    val viewModel: ResidentViewModel = viewModel()
    val context = LocalContext.current
    val history by viewModel.visitorHistory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showFilter by remember { mutableStateOf(false) }
    var filteredHistory by remember { mutableStateOf<List<Visitor>>(emptyList()) }

    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        currentUser?.uid?.let {
            viewModel.loadVisitorHistory(it)
        }
    }

    // Update filtered history when history changes
    LaunchedEffect(history) {
        filteredHistory = history
    }

    // Statistics
    val totalVisitors = history.size
    val approvedVisitors = history.count { it.status == "approved" }
    val pendingVisitors = history.count { it.status == "pending" }
    val rejectedVisitors = history.count { it.status == "rejected" }
    val leaveAtGateVisitors = history.count { it.status == "leave_at_gate" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Visitors",
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
                actions = {
                    IconButton(onClick = { showFilter = true }) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filter",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            // Statistics Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    label = "Total",
                    value = totalVisitors.toString(),
                    color = Primary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Approved",
                    value = approvedVisitors.toString(),
                    color = Success,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Pending",
                    value = pendingVisitors.toString(),
                    color = Warning,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Rejected",
                    value = rejectedVisitors.toString(),
                    color = Error,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Gate",
                    value = leaveAtGateVisitors.toString(),
                    color = Warning,
                    modifier = Modifier.weight(1f)
                )
            }

            // History List
            if (isLoading && history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (filteredHistory.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            tint = TextHint,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "No visitors yet",
                            fontSize = 16.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredHistory) { visitor ->
                        HistoryItemCard(
                            visitor = visitor,
                            viewModel = viewModel
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    // Filter Dialog
    if (showFilter) {
        FilterDialogResident(
            onDismiss = { showFilter = false },
            onApply = { startDate, endDate, status ->
                // Apply filter
                val filtered = history.filter { visitor ->
                    var matches = true

                    if (startDate.isNotBlank() && endDate.isNotBlank()) {
                        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        try {
                            val start = format.parse(startDate)
                            val end = format.parse(endDate)
                            val visitorDate = visitor.entryTime?.toDate()
                            if (visitorDate != null) {
                                matches = matches && visitorDate in start..end
                            }
                        } catch (e: Exception) {
                            // Date parsing error
                        }
                    }

                    if (status != "all") {
                        matches = matches && visitor.status == status
                    }

                    matches
                }
                filteredHistory = filtered
                showFilter = false
            }
        )
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun HistoryItemCard(
    visitor: Visitor,
    viewModel: ResidentViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TealMain.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = TealMain,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = visitor.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Text(
                    text = "${visitor.purpose} • ${viewModel.formatTime(visitor.entryTime)} • ${viewModel.formatDate(visitor.entryTime)}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            StatusChip(status = visitor.status)
        }
    }
}

@Composable
fun FilterDialogResident(
    onDismiss: () -> Unit,
    onApply: (String, String, String) -> Unit
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("all") }

    val statuses = listOf("all", "approved", "pending", "rejected", "leave_at_gate")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Visitors") },
        text = {
            Column {
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Start Date (DD/MM/YYYY)") },
                    placeholder = { Text("DD/MM/YYYY") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("End Date (DD/MM/YYYY)") },
                    placeholder = { Text("DD/MM/YYYY") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Status",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    statuses.forEach { status ->
                        FilterChip(
                            selected = selectedStatus == status,
                            onClick = { selectedStatus = status },
                            label = {
                                Text(
                                    when (status) {
                                        "all" -> "All"
                                        "approved" -> "Approved"
                                        "pending" -> "Pending"
                                        "rejected" -> "Rejected"
                                        "leave_at_gate" -> "Leave at Gate"
                                        else -> status
                                    }
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Primary,
                                selectedLabelColor = Color.White,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApply(startDate, endDate, selectedStatus)
                }
            ) {
                Text("Apply", color = Primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}