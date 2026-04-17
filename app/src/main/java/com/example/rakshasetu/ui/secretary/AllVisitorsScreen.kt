package com.example.rakshasetu.ui.secretary

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
import com.example.rakshasetu.data.models.Visitor
import com.example.rakshasetu.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllVisitorsScreen(
    navController: NavController,
    userRole: String? = "secretary",
    societyId: String
) {
    val viewModel: SecretaryViewModel = viewModel()
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val visitors by viewModel.allVisitors.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("all") }
    var selectedDate by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }

    val filters = listOf("all", "today", "week", "month", "custom")

    LaunchedEffect(societyId) {
        viewModel.loadAllVisitors(societyId)
    }

    // Filter visitors
    val filteredVisitors = remember(visitors, searchQuery, selectedFilter, selectedDate) {
        visitors.filter { visitor ->
            // Search filter
            val matchesSearch = searchQuery.isBlank() ||
                    visitor.visitorName.contains(searchQuery, ignoreCase = true) ||
                    visitor.flatNumber.contains(searchQuery, ignoreCase = true) ||
                    visitor.purpose.contains(searchQuery, ignoreCase = true)

            // Date filter
            val matchesDate = when (selectedFilter) {
                "today" -> {
                    visitor.entryTime?.toDate()?.let { isToday(it) } ?: false
                }
                "week" -> {
                    visitor.entryTime?.toDate()?.let { isThisWeek(it) } ?: false
                }
                "month" -> {
                    visitor.entryTime?.toDate()?.let { isThisMonth(it) } ?: false
                }
                "custom" -> {
                    selectedDate.isNotBlank() && visitor.entryTime?.toDate()?.let {
                        isDateMatch(it, selectedDate)
                    } ?: false
                }
                else -> true
            }

            matchesSearch && matchesDate
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
                        text = "All Visitors",
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
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by name, flat, or purpose...", color = TextHint) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = TextHint
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Surface,
                    unfocusedContainerColor = Surface,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = TextHint,
                    cursorColor = Primary
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Statistics Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // FIXED: weight is used as modifier, not function call
                Card(
                    modifier = Modifier.weight(1f),
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

            // Filter Chips
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

            // Visitors List
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
                            Icons.Default.People,
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
                        VisitorCard(visitor = visitor)
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onApply = { date ->
                selectedDate = date
                selectedFilter = "custom"
                showFilterDialog = false
            }
        )
    }
}

@Composable
fun VisitorCard(visitor: Visitor) {
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
            // Visitor Photo
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        when (visitor.status) {
                            "approved" -> Success.copy(alpha = 0.1f)
                            "pending" -> Warning.copy(alpha = 0.1f)
                            "rejected" -> Error.copy(alpha = 0.1f)
                            else -> Primary.copy(alpha = 0.1f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = when (visitor.status) {
                        "approved" -> Success
                        "pending" -> Warning
                        "rejected" -> Error
                        else -> Primary
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Visitor Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = visitor.visitorName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Flat ${visitor.flatNumber}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${visitor.purpose}${if (visitor.organization.isNotBlank()) " - ${visitor.organization}" else ""}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                visitor.entryTime?.toDate()?.let { date ->
                    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            tint = TextHint,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = dateFormat.format(date),
                            fontSize = 11.sp,
                            color = TextHint
                        )
                    }
                }
            }

            // Status Chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        when (visitor.status) {
                            "approved" -> Success.copy(alpha = 0.1f)
                            "pending" -> Warning.copy(alpha = 0.1f)
                            "rejected" -> Error.copy(alpha = 0.1f)
                            else -> TextHint.copy(alpha = 0.1f)
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = visitor.status.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    fontSize = 11.sp,
                    color = when (visitor.status) {
                        "approved" -> Success
                        "pending" -> Warning
                        "rejected" -> Error
                        else -> TextHint
                    },
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onApply: (String) -> Unit
) {
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter by Date") },
        text = {
            Column {
                Text(
                    text = "Select Date (DD/MM/YYYY)",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = day,
                        onValueChange = {
                            if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                                day = it
                            }
                        },
                        label = { Text("DD") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = month,
                        onValueChange = {
                            if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                                month = it
                            }
                        },
                        label = { Text("MM") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = year,
                        onValueChange = {
                            if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                                year = it
                            }
                        },
                        label = { Text("YYYY") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (day.length == 2 && month.length == 2 && year.length == 4) {
                        onApply("$day/$month/$year")
                    }
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

// Date helper functions
fun isToday(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_YEAR)
    val todayYear = calendar.get(Calendar.YEAR)

    calendar.time = date
    return calendar.get(Calendar.DAY_OF_YEAR) == today &&
            calendar.get(Calendar.YEAR) == todayYear
}

fun isThisWeek(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.time = date
    return calendar.get(Calendar.WEEK_OF_YEAR) == currentWeek &&
            calendar.get(Calendar.YEAR) == currentYear
}

fun isThisMonth(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.time = date
    return calendar.get(Calendar.MONTH) == currentMonth &&
            calendar.get(Calendar.YEAR) == currentYear
}

fun isDateMatch(date: Date, dateStr: String): Boolean {
    val parts = dateStr.split("/")
    if (parts.size != 3) return false

    val day = parts[0].toIntOrNull() ?: return false
    val month = parts[1].toIntOrNull() ?: return false
    val year = parts[2].toIntOrNull() ?: return false

    val calendar = Calendar.getInstance()
    calendar.time = date

    return calendar.get(Calendar.DAY_OF_MONTH) == day &&
            calendar.get(Calendar.MONTH) + 1 == month &&
            calendar.get(Calendar.YEAR) == year
}