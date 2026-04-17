package com.example.rakshasetu.ui.secretary

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
fun AddFlatsScreen(
    navController: NavController,
    userRole: String? = "secretary",
    societyId: String,
    blocks: List<String>
) {
    val viewModel: SecretaryViewModel = viewModel()
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val flats by viewModel.flats.collectAsState()

    var selectedBlock by remember { mutableStateOf(blocks.firstOrNull() ?: "") }
    var startFlat by remember { mutableStateOf("") }
    var endFlat by remember { mutableStateOf("") }
    var showPreview by remember { mutableStateOf(false) }

    // 🔥 NEW STATES
    var showEditDialog by remember { mutableStateOf(false) }
    var editableBlocks by remember { mutableStateOf(blocks.joinToString(",")) }

    // Validation
    var isStartFlatValid by remember { mutableStateOf(true) }
    var isEndFlatValid by remember { mutableStateOf(true) }

    val existingFlatsInBlock = flats.filter { it.blockNumber == selectedBlock }
    val existingFlatNumbers = existingFlatsInBlock.map { it.flatNumber.toIntOrNull() ?: 0 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Flats", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                actions = {
                    // 🔥 EDIT BUTTON ADDED
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Blocks", tint = Color.White)
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

            // BLOCK SELECTION (UNCHANGED UI)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Select Block",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            blocks.forEach { block ->
                                FilterChip(
                                    selected = selectedBlock == block,
                                    onClick = { selectedBlock = block },
                                    label = { Text("Block $block") },
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
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddFlatsScreen(
//    navController: NavController,
//    userRole: String? = "secretary",
//    societyId: String,
//    blocks: List<String>
//) {
//    val viewModel: SecretaryViewModel = viewModel()
//    val context = LocalContext.current
//    val isLoading by viewModel.isLoading.collectAsState()
//    val flats by viewModel.flats.collectAsState()
//
//    var selectedBlock by remember { mutableStateOf(blocks.firstOrNull() ?: "") }
//    var startFlat by remember { mutableStateOf("") }
//    var endFlat by remember { mutableStateOf("") }
//    var showPreview by remember { mutableStateOf(false) }
//
//    // Validation states
//    var isStartFlatValid by remember { mutableStateOf(true) }
//    var isEndFlatValid by remember { mutableStateOf(true) }
//
//    // Get existing flats for preview
//    val existingFlatsInBlock = flats.filter { it.blockNumber == selectedBlock }
//    val existingFlatNumbers = existingFlatsInBlock.map { it.flatNumber.toIntOrNull() ?: 0 }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Add Flats",
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
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Background)
//                .padding(paddingValues)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // Block Selection
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Surface
//                    ),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Select Block",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = TextPrimary,
//                            modifier = Modifier.padding(bottom = 12.dp)
//                        )
//
//                        // Block chips
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            blocks.forEach { block ->
//                                FilterChip(
//                                    selected = selectedBlock == block,
//                                    onClick = { selectedBlock = block },
//                                    label = { Text("Block $block") },
//                                    colors = FilterChipDefaults.filterChipColors(
//                                        selectedContainerColor = Primary,
//                                        selectedLabelColor = Color.White,
//                                        labelColor = TextSecondary
//                                    )
//                                )
//                            }
//                        }
//                    }
//                }
//            }

            // Flat Range Input
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
                            text = "Flat Range",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Start Flat
                            OutlinedTextField(
                                value = startFlat,
                                onValueChange = {
                                    startFlat = it.filter { char -> char.isDigit() }
                                    isStartFlatValid = true
                                },
                                label = { Text("Start Flat *") },
                                isError = !isStartFlatValid,
                                supportingText = {
                                    if (!isStartFlatValid) {
                                        Text("Required", color = Error, fontSize = 12.sp)
                                    }
                                },
                                modifier = Modifier.weight(1f),
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

                            // End Flat
                            OutlinedTextField(
                                value = endFlat,
                                onValueChange = {
                                    endFlat = it.filter { char -> char.isDigit() }
                                    isEndFlatValid = true
                                },
                                label = { Text("End Flat *") },
                                isError = !isEndFlatValid,
                                supportingText = {
                                    if (!isEndFlatValid) {
                                        Text("Required", color = Error, fontSize = 12.sp)
                                    }
                                },
                                modifier = Modifier.weight(1f),
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

                        Spacer(modifier = Modifier.height(8.dp))

                        // Preview Button
                        Button(
                            onClick = {
                                val start = startFlat.toIntOrNull()
                                val end = endFlat.toIntOrNull()

                                isStartFlatValid = start != null
                                isEndFlatValid = end != null

                                if (isStartFlatValid && isEndFlatValid && start!! <= end!!) {
                                    showPreview = true
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please enter valid flat numbers",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Secondary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Preview Flats")
                        }
                    }
                }
            }

            // Preview Section
            if (showPreview) {
                val start = startFlat.toIntOrNull() ?: 0
                val end = endFlat.toIntOrNull() ?: 0
                val flatsToAdd = (start..end).toList()
                val existingInRange = flatsToAdd.filter { it in existingFlatNumbers }
                val newFlats = flatsToAdd.filter { it !in existingFlatNumbers }

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
                                text = "Preview",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // Summary
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = flatsToAdd.size.toString(),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Primary
                                    )
                                    Text(
                                        text = "Total",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = newFlats.size.toString(),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Success
                                    )
                                    Text(
                                        text = "New",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = existingInRange.size.toString(),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Warning
                                    )
                                    Text(
                                        text = "Existing",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            if (existingInRange.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Existing flats: ${existingInRange.joinToString(", ")}",
                                    fontSize = 14.sp,
                                    color = Warning
                                )
                            }
                        }
                    }
                }

                // Add Flats Button
                if (newFlats.isNotEmpty()) {
                    item {
                        Button(
                            onClick = {
                                viewModel.addFlats(
                                    societyId = societyId,
                                    block = selectedBlock,
                                    startFlat = start,
                                    endFlat = end
                                ) { success, message ->
                                    if (success) {
                                        Toast.makeText(
                                            context,
                                            message ?: "Flats added successfully",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.popBackStack()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Failed: $message",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
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
                                    text = "Add ${newFlats.size} Flats",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Existing Flats List
            if (existingFlatsInBlock.isNotEmpty()) {
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
                                text = "Existing Flats in Block $selectedBlock",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            existingFlatsInBlock.take(10).forEach { flat ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(
                                                if (flat.occupied) Success else Warning,
                                                shape = CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Flat ${flat.flatNumber} - ${if (flat.occupied) "Occupied" else "Vacant"}",
                                        fontSize = 14.sp,
                                        color = if (flat.occupied) Success else Warning
                                    )
                                }
                            }

                            if (existingFlatsInBlock.size > 10) {
                                Text(
                                    text = "... and ${existingFlatsInBlock.size - 10} more",
                                    fontSize = 12.sp,
                                    color = TextHint,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        // =======================
        // 🔥 EDIT BLOCK DIALOG
        // =======================
        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit Blocks") },
                text = {
                    Column {
                        Text(
                            text = "Enter blocks (comma separated)",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = editableBlocks,
                            onValueChange = { editableBlocks = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("A,B,C") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val updatedBlocks = editableBlocks.split(",")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }

                        if (updatedBlocks.isEmpty()) {
                            Toast.makeText(context, "Blocks cannot be empty", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        viewModel.updateBlocks(
                            societyId,
                            updatedBlocks
                        ) { success, message ->
                            if (success) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                showEditDialog = false
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }

                    }) {
                        Text("Update")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
