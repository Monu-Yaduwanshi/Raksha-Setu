//package com.example.rakshasetu.core.utils
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//import androidx.compose.ui.semantics.Role
//
///**
// * A safe clickable modifier that avoids the IndicationNodeFactory error
// * by explicitly setting indication to null
// */
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
//
///**
// * Alternative with ripple if needed
// */
//fun Modifier.rippleClickable(
//    onClick: () -> Unit
//): Modifier = composed {
//    val interactionSource = remember { MutableInteractionSource() }
//    val ripple = androidx.compose.material.ripple.rememberRipple()
//    this.clickable(
//        interactionSource = interactionSource,
//        indication = ripple,
//        onClick = onClick
//    )
//}
//
///**
// * For debugging - logs which composable is being clicked
// */
//fun Modifier.debugClickable(
//    tag: String = "DebugClick",
//    onClick: () -> Unit
//): Modifier = composed {
//    val interactionSource = remember { MutableInteractionSource() }
//    this.clickable(
//        interactionSource = interactionSource,
//        indication = null,
//        onClick = {
//            android.util.Log.d(tag, "Item clicked at ${System.currentTimeMillis()}")
//            onClick()
//        }
//    )
//}
package com.example.rakshasetu.core.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role

/**
 * A safe clickable modifier that avoids the IndicationNodeFactory error
 * by explicitly setting indication to null
 */
//fun Modifier.safeClickable(
//    onClick: () -> Unit
//): Modifier = this.then(
//    Modifier.pointerInput(Unit) {
//        detectTapGestures {
//            onClick()
//        }
//    }
//)
fun Modifier.safeClickable(
    onClick: () -> Unit
): Modifier = composed {
    this.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                onClick()
            }
        )
    }
}
/**
 * Alternative with ripple - FIXED: No Material ripple reference
 */
fun Modifier.rippleClickable(
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }

    this.clickable(
        interactionSource = interactionSource,
        indication = null,  // ✅ Safe for Material3
        onClick = onClick
    )
}

/**
 * For debugging - logs which composable is being clicked
 */
fun Modifier.debugClickable(
    tag: String = "DebugClick",
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    this.clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = {
            android.util.Log.d(tag, "Item clicked at ${System.currentTimeMillis()}")
            onClick()
        }
    )
}