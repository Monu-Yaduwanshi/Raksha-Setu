package com.example.rakshasetu.ui.secretary

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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.rakshasetu.data.models.Flat
import com.example.rakshasetu.data.models.Society
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.auth.User

//val SecretaryTopBarColor = Color(0xFF0CB381)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageSocietyScreen(
    navController: NavController,
    userRole: String? = "secretary",
    societyId: String
) {
    val viewModel: SecretaryViewModel = viewModel()
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val society by viewModel.society.collectAsState()
    val flats by viewModel.flats.collectAsState()
    val residents by viewModel.residents.collectAsState()
    val watchmen by viewModel.watchmen.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    var showRemoveDialog by remember { mutableStateOf<Pair<String, String>?>(null) }

    val tabs = listOf("Overview", "Residents", "Watchmen", "Flats")

    LaunchedEffect(societyId) {
        viewModel.loadSociety(societyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Society",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Surface,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Primary
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) Primary else TextSecondary,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Content
            when (selectedTab) {
                0 -> OverviewTab(
                    society = society,
                    flats = flats,
                    residents = residents,
                    watchmen = watchmen
                )
                1 -> ResidentsTab(
                    residents = residents,
                    flats = flats,
                    onRemove = { residentId, flatId ->
                        showRemoveDialog = Pair(residentId, flatId)
                    }
                )
                2 -> WatchmenTab(watchmen = watchmen)
                3 -> FlatsTab(flats = flats)
            }
        }
    }

    // Remove Confirmation Dialog
    showRemoveDialog?.let { (residentId, flatId) ->
        AlertDialog(
            onDismissRequest = { showRemoveDialog = null },
            title = { Text("Confirm Removal") },
            text = { Text("Are you sure you want to remove this resident?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.removeResident(residentId, flatId) { success, message ->
                            if (success) {
                                Toast.makeText(context, "Resident removed", Toast.LENGTH_SHORT).show()
                                viewModel.loadSociety(societyId)
                            } else {
                                Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                            }
                            showRemoveDialog = null
                        }
                    }
                ) {
                    Text("Remove", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun OverviewTab(
    society: Society?,
    flats: List<Flat>,
    residents: List<User>,
    watchmen: List<User>
) {
    val occupiedFlats = flats.count { it.occupied }
    val vacantFlats = flats.size - occupiedFlats

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (society != null) {
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
                            text = society.societyName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        InfoRow("Address", society.address)
                        InfoRow("City", society.city)
                        InfoRow("Pincode", society.pincode)
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
                        text = "Statistics",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCardSmall(
                            label = "Total Flats",
                            value = flats.size.toString(),
                            color = Primary
                        )
                        StatCardSmall(
                            label = "Occupied",
                            value = occupiedFlats.toString(),
                            color = Success
                        )
                        StatCardSmall(
                            label = "Vacant",
                            value = vacantFlats.toString(),
                            color = Warning
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCardSmall(
                            label = "Residents",
                            value = residents.size.toString(),
                            color = TealMain
                        )
                        StatCardSmall(
                            label = "Watchmen",
                            value = watchmen.size.toString(),
                            color = Secondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResidentsTab(
    residents: List<User>,
    flats: List<Flat>,
    onRemove: (String, String) -> Unit
) {
    if (residents.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No residents added yet",
                color = TextSecondary,
                fontSize = 16.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(residents) { resident ->
                val flatId = flats.find { it.residentId == resident.userId }?.flatId
                ResidentCard(
                    resident = resident,
                    onRemove = { flatId?.let { onRemove(resident.userId, it) } }
                )
            }
        }
    }
}

@Composable
fun ResidentCard(
    resident: User,
    onRemove: () -> Unit
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
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = resident.fullName.first().toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = resident.fullName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Flat ${resident.blockNumber}-${resident.flatNumber}",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Text(
                    text = resident.phone,
                    fontSize = 12.sp,
                    color = TextHint
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = Error
                )
            }
        }
    }
}

@Composable
fun WatchmenTab(watchmen: List<User>) {
    if (watchmen.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No watchmen added yet",
                color = TextSecondary,
                fontSize = 16.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(watchmen) { watchman ->
                WatchmanCard(watchman = watchman)
            }
        }
    }
}

@Composable
fun WatchmanCard(watchman: User) {
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
                    .background(Secondary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = watchman.fullName.first().toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = watchman.fullName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = watchman.phone,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun FlatsTab(flats: List<Flat>) {
    if (flats.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No flats added yet",
                color = TextSecondary,
                fontSize = 16.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(flats) { flat ->
                FlatCard(flat = flat)
            }
        }
    }
}

@Composable
fun FlatCard(flat: Flat) {
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
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (flat.occupied) Success.copy(alpha = 0.1f)
                        else Warning.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${flat.blockNumber}-${flat.flatNumber}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (flat.occupied) Success else Warning
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Block ${flat.blockNumber}, Flat ${flat.flatNumber}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                if (flat.occupied) {
                    Text(
                        text = "Occupied by: ${flat.residentName}",
                        fontSize = 14.sp,
                        color = Success
                    )
                } else {
                    Text(
                        text = "Vacant",
                        fontSize = 14.sp,
                        color = Warning
                    )
                }
            }
        }
    }
}

@Composable
fun StatCardSmall(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.width(70.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}