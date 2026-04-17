package com.example.rakshasetu.ui.watchMan

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
import com.example.rakshasetu.core.utils.safeClickable
import com.example.rakshasetu.ui.secretary.SecretaryTopBarColor
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.watchman.VisitorItem
import com.example.rakshasetu.viewModel.viewModel.watchman.WatchmanViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitorHistoryScreen(
    navController: NavController,
    userRole: String? = "watchman"
) {
    val viewModel: WatchmanViewModel = viewModel()
    val context = LocalContext.current
    val visitors by viewModel.visitorHistory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedFilter by remember { mutableStateOf("all") }
    var showFilterDialog by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var selectedFlat by remember { mutableStateOf("") }

    val filters = listOf("all", "today", "week", "month", "custom")

    // Load history
    LaunchedEffect(Unit) {
        viewModel.loadVisitorHistory()
        viewModel.loadTodayStats()
    }

    // Filter visitors
    val filteredVisitors = remember(visitors, selectedFilter, startDate, endDate, selectedFlat) {
        visitors.filter { visitor ->
            var matches = true

            // Date filter
            if (selectedFilter != "all") {
                val visitorDate = visitor.entryTime
                matches = matches && when (selectedFilter) {
                    "today" -> isToday(visitorDate)
                    "week" -> isThisWeek(visitorDate)
                    "month" -> isThisMonth(visitorDate)
                    "custom" -> {
                        visitorDate?.let {
                            isBetweenDates(it, startDate, endDate)
                        } ?: false
                    }
                    else -> true
                }
            }

            // Flat filter
            if (selectedFlat.isNotBlank()) {
                matches = matches && visitor.flat.contains(selectedFlat, ignoreCase = true)
            }

            matches
        }
    }

    // Statistics
    val totalVisitors = visitors.size
    val approvedVisitors = visitors.count { it.status == "approved" }
    val pendingVisitors = visitors.count { it.status == "pending" }
    val rejectedVisitors = visitors.count { it.status == "rejected" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Visitor History",
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
                    IconButton(onClick = { showFilterDialog = true }) {
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
            // Statistics Cards (no clickable - keep as is)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = totalVisitors.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        Text(
                            text = "Total",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = approvedVisitors.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Success
                        )
                        Text(
                            text = "Approved",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = pendingVisitors.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Warning
                        )
                        Text(
                            text = "Pending",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = rejectedVisitors.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Error
                        )
                        Text(
                            text = "Rejected",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Filter Chips (no clickable issues - FilterChip is fine)
            ScrollableTabRow(
                selectedTabIndex = filters.indexOf(selectedFilter).coerceIn(0, filters.size - 1),
                containerColor = Surface,
                edgePadding = 16.dp,
                indicator = {}
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                when (filter) {
                                    "all" -> "All"
                                    "today" -> "Today"
                                    "week" -> "This Week"
                                    "month" -> "This Month"
                                    else -> "Custom"
                                }
                            )
                        },
                        modifier = Modifier.padding(end = 8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Primary,
                            selectedLabelColor = Color.White,
                            labelColor = TextSecondary
                        )
                    )
                }
            }

            // History List
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (filteredVisitors.isEmpty()) {
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
                            text = "No visitors found",
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
                    items(filteredVisitors) { visitor ->
                        // ✅ FIXED: HistoryCard with safeClickable
                        HistoryCard(visitor = visitor, onClick = {
                            // Navigate to visitor details when implemented
                        })
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    // Filter Dialog (no clickable issues - AlertDialog buttons are fine)
    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onApply = { start, end, flat ->
                startDate = start
                endDate = end
                selectedFlat = flat
                selectedFilter = "custom"
                showFilterDialog = false
            }
        )
    }
}

// ✅ FIXED: HistoryCard with safeClickable
@Composable
fun HistoryCard(visitor: VisitorItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .safeClickable(onClick = onClick),  // ✅ Using safeClickable
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Visitor Photo Thumbnail
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Secondary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Visitor Details
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
                    text = "${visitor.flat} • ${visitor.time}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                visitor.entryTime?.let {
                    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    Text(
                        text = dateFormat.format(it.toDate()),
                        fontSize = 11.sp,
                        color = TextHint
                    )
                }
            }

            StatusChip(status = visitor.status)
        }
    }
}

@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onApply: (String, String, String) -> Unit
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var flatNumber by remember { mutableStateOf("") }

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
                OutlinedTextField(
                    value = flatNumber,
                    onValueChange = { flatNumber = it },
                    label = { Text("Flat Number") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApply(startDate, endDate, flatNumber)
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

// Date helper functions (keep as is)
fun isToday(date: Timestamp?): Boolean {
    if (date == null) return false
    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_YEAR)
    val todayYear = calendar.get(Calendar.YEAR)

    calendar.time = date.toDate()
    return calendar.get(Calendar.DAY_OF_YEAR) == today &&
            calendar.get(Calendar.YEAR) == todayYear
}

fun isThisWeek(date: Timestamp?): Boolean {
    if (date == null) return false
    val calendar = Calendar.getInstance()
    val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.time = date.toDate()
    return calendar.get(Calendar.WEEK_OF_YEAR) == currentWeek &&
            calendar.get(Calendar.YEAR) == currentYear
}

fun isThisMonth(date: Timestamp?): Boolean {
    if (date == null) return false
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.time = date.toDate()
    return calendar.get(Calendar.MONTH) == currentMonth &&
            calendar.get(Calendar.YEAR) == currentYear
}

fun isBetweenDates(date: Timestamp, startDateStr: String, endDateStr: String): Boolean {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return try {
        val start = format.parse(startDateStr)
        val end = format.parse(endDateStr)
        val dateObj = date.toDate()
        dateObj in start..end
    } catch (e: Exception) {
        false
    }
}