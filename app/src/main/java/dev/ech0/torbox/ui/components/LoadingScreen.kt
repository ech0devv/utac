package dev.ech0.torbox.ui.components

import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {
    LoadingScreen("Just a sec...")
}
@Composable
fun LoadingScreen(value: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures {} }
    ) {}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .wrapContentWidth(align = Alignment.CenterHorizontally),
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(40.dp),
            strokeWidth = 5.dp
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}