//package com.example.rakshasetu.core.utils
//
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//
//fun Modifier.safeClickable(
//    onClick: () -> Unit
//): Modifier = composed {
//    val interactionSource = remember { MutableInteractionSource() }
//    this.clickable(
//        interactionSource = interactionSource,
//        indication = null,
//        onClick = onClick
//    )
//}
