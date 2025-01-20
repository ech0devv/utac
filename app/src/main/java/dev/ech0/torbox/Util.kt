package dev.ech0.torbox

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.navigation.NavController
import org.json.JSONObject


fun<T> execute(navController: NavController, action: () -> T): T? {
    return try {
        action()
    } catch (e: Exception) {
        navController.navigate("Error/${Uri.encode(e.toString())}")
        null
    }
}
// https://stackoverflow.com/a/23377941
fun optString(json: JSONObject, key: String?): String? {
    // http://code.google.com/p/android/issues/detail?id=13830
    return if (json.isNull(key)) null
    else json.optString(key, null)
}
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalFoundationApi::class)
@Composable
fun IconButtonLongClickable(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = IconButtonDefaults.standardShape,
    content: @Composable () -> Unit,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    Box(
        modifier =
            modifier
                .minimumInteractiveComponentSize()
                .size(IconButtonDefaults.smallContainerSize())
                .clip(shape)
                .background(color = if(enabled) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSurface, shape = shape)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick,
                    enabled = enabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                    indication = ripple()
                ),
        contentAlignment = Alignment.Center
    ) {
        val contentColor = if(enabled) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}