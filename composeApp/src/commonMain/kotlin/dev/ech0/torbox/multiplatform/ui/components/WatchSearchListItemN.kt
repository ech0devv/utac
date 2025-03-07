package dev.ech0.torbox.multiplatform.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.ech0.torbox.multiplatform.api.tmdbApi
import dev.ech0.torbox.multiplatform.ui.pages.watch.WatchSearchResult

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchSearchListItemN(it: WatchSearchResult, onClick: () -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            .height(200.dp).combinedClickable(onClick = {onClick()}),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = it.poster?.let { it1 -> tmdbApi.imageHelper(it1) },
            contentDescription = null,
            modifier = Modifier.padding(8.dp).padding(end = 16.dp)
                .clip(RoundedCornerShape(12.dp)).fillMaxHeight().width(125.dp),
            contentScale = ContentScale.FillHeight
        )
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                it.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            it.summary?.let { it1 ->
                Text(
                    it1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        }
    }
    HorizontalDivider()
}