package com.example.rakshasetu.ui.secretary

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

val SecretaryTopBarColor = Color(0xFF0046CC)

// Data class for Block Range


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSocietyScreen(
    navController: NavController,
    userRole: String? = "secretary"
) {
    val secretaryViewModel: SecretaryViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    val isLoading by secretaryViewModel.isLoading.collectAsState()
    val userData by authViewModel.userData.collectAsState()
    val society by secretaryViewModel.society.collectAsState()

    val societyId = userData?.societyId ?: ""

    var isEditMode by remember { mutableStateOf(false) }
    var isSocietyExists by remember { mutableStateOf(false) }

    // Form fields
    var societyName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }

    // 🔥 Block ranges for flat creation
    var blockRanges by remember {
        mutableStateOf(mutableStateListOf(BlockRange()))
    }

    // Load society
    LaunchedEffect(societyId) {
        if (societyId.isNotBlank()) {
            secretaryViewModel.loadSociety(societyId)
        }
    }

    // Prefill when society loaded - UPDATED to load block ranges from existing flats
    LaunchedEffect(society) {
        society?.let {
            isSocietyExists = true
            societyName = it.societyName
            address = it.address
            city = it.city
            pincode = it.pincode

            // 🔥 Load existing block ranges from flats
            val existingBlocks = secretaryViewModel.getBlockRangesFromFlats(societyId)
            if (existingBlocks.isNotEmpty()) {
                blockRanges.clear()
                blockRanges.addAll(existingBlocks)
            } else if (it.blocks.isNotEmpty()) {
                // Fallback to blocks from society document
                blockRanges.clear()
                it.blocks.forEach { block ->
                    blockRanges.add(BlockRange(blockName = block, startFlat = "", endFlat = ""))
                }
            }
        }
    }

    val isEditable = !isSocietyExists || isEditMode

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isSocietyExists) "Society Details" else "Create Society",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (isSocietyExists && !isEditMode) {
                        TextButton(onClick = { isEditMode = true }) {
                            Text("Edit", color = Color.White)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
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

            // Society Details Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            "Society Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = societyName,
                            onValueChange = { societyName = it },
                            label = { Text("Society Name") },
                            enabled = isEditable,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Address") },
                            enabled = isEditable,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                            OutlinedTextField(
                                value = city,
                                onValueChange = { city = it },
                                label = { Text("City") },
                                enabled = isEditable,
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = pincode,
                                onValueChange = { pincode = it },
                                label = { Text("Pincode") },
                                enabled = isEditable,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 🔥 Blocks & Flats Card (For both new and edit mode)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            if (isSocietyExists) "Blocks & Flats (Add New Blocks)" else "Blocks & Flats",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        blockRanges.forEachIndexed { index, range ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {

                                    OutlinedTextField(
                                        value = range.blockName,
                                        onValueChange = {
                                            blockRanges[index] = range.copy(blockName = it.uppercase())
                                        },
                                        label = { Text("Block (A, B, C)") },
                                        enabled = isEditable,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row {
                                        OutlinedTextField(
                                            value = range.startFlat,
                                            onValueChange = {
                                                blockRanges[index] = range.copy(startFlat = it)
                                            },
                                            label = { Text("Start Flat") },
                                            enabled = isEditable,
                                            modifier = Modifier.weight(1f)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        OutlinedTextField(
                                            value = range.endFlat,
                                            onValueChange = {
                                                blockRanges[index] = range.copy(endFlat = it)
                                            },
                                            label = { Text("End Flat") },
                                            enabled = isEditable,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    // Remove button (only in edit mode and for new blocks)
                                    if (isEditable && blockRanges.size > 1) {
                                        TextButton(
                                            onClick = {
                                                blockRanges.removeAt(index)
                                            }
                                        ) {
                                            Text("Remove Block", color = Error)
                                        }
                                    }
                                }
                            }
                        }

                        // Add Block Button (only in edit mode or new society)
                        if (isEditable) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    onClick = {
                                        blockRanges.add(BlockRange())
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Primary
                                    )
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add Block")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Add Block")
                                }
                            }
                        }
                    }
                }
            }

            // For existing society - show existing blocks summary
            if (isSocietyExists && !isEditMode && society != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                "Existing Blocks",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            society?.blocks?.forEach { block ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(Success, shape = RoundedCornerShape(4.dp))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Block $block",
                                        fontSize = 14.sp,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = {

                        if (isSocietyExists && isEditMode) {
                            // UPDATE SOCIETY WITH FLAT EDIT (SAFE - NO DELETE OF OCCUPIED FLATS)
                            if (societyName.isBlank()) {
                                Toast.makeText(context, "Please enter society name", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            // Validate new block ranges
                            var hasError = false
                            blockRanges.forEach { range ->
                                if (range.blockName.isNotBlank()) {
                                    if (range.startFlat.isBlank() || range.endFlat.isBlank()) {
                                        Toast.makeText(context, "Please enter flat range for block ${range.blockName}", Toast.LENGTH_SHORT).show()
                                        hasError = true
                                        return@forEach
                                    }
                                    val start = range.startFlat.toIntOrNull()
                                    val end = range.endFlat.toIntOrNull()
                                    if (start == null || end == null || start > end) {
                                        Toast.makeText(context, "Invalid flat range for block ${range.blockName}", Toast.LENGTH_SHORT).show()
                                        hasError = true
                                        return@forEach
                                    }
                                }
                            }

                            if (hasError) return@Button

                            // Filter only blocks that have valid ranges (for new blocks)
                            val newBlockRanges = blockRanges.filter {
                                it.blockName.isNotBlank() && it.startFlat.isNotBlank() && it.endFlat.isNotBlank()
                            }

                            secretaryViewModel.updateSocietyWithFlatEdit(
                                societyId = societyId,
                                societyName = societyName,
                                address = address,
                                city = city,
                                pincode = pincode,
                                blockRanges = newBlockRanges
                            ) { success, message ->
                                if (success) {
                                    Toast.makeText(context, "Society Updated Successfully!", Toast.LENGTH_LONG).show()
                                    isEditMode = false
                                    secretaryViewModel.loadSociety(societyId)
                                } else {
                                    Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
                                }
                            }

                        } else if (!isSocietyExists) {
                            // CREATE NEW SOCIETY
                            if (societyName.isBlank()) {
                                Toast.makeText(context, "Please enter society name", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if (blockRanges.isEmpty() || blockRanges.all { it.blockName.isBlank() }) {
                                Toast.makeText(context, "Please add at least one block", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            var hasError = false
                            blockRanges.forEach { range ->
                                if (range.blockName.isBlank()) {
                                    Toast.makeText(context, "Block name cannot be empty", Toast.LENGTH_SHORT).show()
                                    hasError = true
                                    return@forEach
                                }
                                if (range.startFlat.isBlank() || range.endFlat.isBlank()) {
                                    Toast.makeText(context, "Please enter flat range for block ${range.blockName}", Toast.LENGTH_SHORT).show()
                                    hasError = true
                                    return@forEach
                                }
                                val start = range.startFlat.toIntOrNull()
                                val end = range.endFlat.toIntOrNull()
                                if (start == null || end == null || start > end) {
                                    Toast.makeText(context, "Invalid flat range for block ${range.blockName}", Toast.LENGTH_SHORT).show()
                                    hasError = true
                                    return@forEach
                                }
                            }

                            if (hasError) return@Button

                            secretaryViewModel.createSocietyWithBlocks(
                                societyName = societyName,
                                address = address,
                                city = city,
                                pincode = pincode,
                                blockRanges = blockRanges.toList()
                            ) { success, createdSocietyId ->
                                if (success) {
                                    Toast.makeText(context, "Society Created Successfully!", Toast.LENGTH_LONG).show()
                                    authViewModel.refreshUserData()
                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(context, "Failed to create society", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    enabled = isEditable && !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Success)
                ) {
                    Text(
                        if (isSocietyExists) {
                            if (isEditMode) "Update Society" else "Saved"
                        } else "Create Society",
                        color = Color.White
                    )
                }
            }
        }
    }
}