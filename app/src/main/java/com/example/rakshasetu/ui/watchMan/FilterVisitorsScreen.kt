package com.example.rakshasetu.ui.watchMan

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.watchman.WatchmanViewModel

val WatchmanTopBarColor = Color(0xFF0046CC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterVisitorsScreen(
    navController: NavController,
    viewModel: WatchmanViewModel = viewModel(),
    userRole: String? = "watchman"
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var selectedFlat by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }
    var selectedPurpose by remember { mutableStateOf("All") }

    // Get dynamic flats from ViewModel
    val flats by viewModel.flats.collectAsState()
    val flatNumbers = listOf("All") + flats.filter { it.isOccupied }.map { it.flatNumber }.distinct()

    val statuses = listOf("All", "Approved", "Pending", "Rejected", "Leave at Gate")
    val purposes = listOf("All", "Delivery", "Guest", "Cab", "House Help", "Maintenance")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filter Visitors", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WatchmanTopBarColor)
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
            // Date Range
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Date Range", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = startDate,
                                onValueChange = { startDate = it },
                                label = { Text("Start Date") },
                                placeholder = { Text("DD/MM/YYYY") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, unfocusedBorderColor = TextHint),
                                shape = RoundedCornerShape(8.dp)
                            )
                            OutlinedTextField(
                                value = endDate,
                                onValueChange = { endDate = it },
                                label = { Text("End Date") },
                                placeholder = { Text("DD/MM/YYYY") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, unfocusedBorderColor = TextHint),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }
            }

            // Filter by Flat (Dynamic from Firebase)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Filter by Flat", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            flatNumbers.chunked(3).forEach { rowFlats ->
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowFlats.forEach { flat ->
                                        FilterChip(
                                            selected = selectedFlat == flat,
                                            onClick = { selectedFlat = flat },
                                            label = { Text(flat) },
                                            modifier = Modifier.weight(1f),
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = Primary,
                                                selectedLabelColor = Color.White,
                                                labelColor = TextSecondary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Filter by Status
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Filter by Status", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            statuses.chunked(3).forEach { rowStatuses ->
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowStatuses.forEach { status ->
                                        FilterChip(
                                            selected = selectedStatus == status,
                                            onClick = { selectedStatus = status },
                                            label = { Text(status) },
                                            modifier = Modifier.weight(1f),
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = Primary,
                                                selectedLabelColor = Color.White,
                                                labelColor = TextSecondary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Filter by Purpose
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Filter by Purpose", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            purposes.chunked(3).forEach { rowPurposes ->
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowPurposes.forEach { purpose ->
                                        FilterChip(
                                            selected = selectedPurpose == purpose,
                                            onClick = { selectedPurpose = purpose },
                                            label = { Text(purpose) },
                                            modifier = Modifier.weight(1f),
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = Primary,
                                                selectedLabelColor = Color.White,
                                                labelColor = TextSecondary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Action Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            // Pass filters back to previous screen
                            navController.previousBackStackEntry?.savedStateHandle?.set("filters", mapOf(
                                "startDate" to startDate,
                                "endDate" to endDate,
                                "flat" to selectedFlat,
                                "status" to selectedStatus,
                                "purpose" to selectedPurpose
                            ))
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Success),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apply Filters")
                    }

                    Button(
                        onClick = {
                            startDate = ""
                            endDate = ""
                            selectedFlat = ""
                            selectedStatus = "All"
                            selectedPurpose = "All"
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Warning),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}