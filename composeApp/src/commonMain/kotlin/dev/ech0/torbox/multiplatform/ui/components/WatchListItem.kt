package dev.ech0.torbox.multiplatform.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchListItem(meta: JsonObject, setWatchShowPage: (String) -> Unit, setWatchShowJson: (JsonObject) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).height(200.dp)
            .combinedClickable(onClick = {
                setWatchShowJson(meta)
                setWatchShowPage(meta["media_type"]!!.jsonPrimitive.content)
            }), verticalAlignment = Alignment.CenterVertically
    ) {
        if (meta.contains("poster_path")) {
            AsyncImage(
                model = tmdbApi.imageHelper(meta["poster_path"]!!.jsonPrimitive.content),
                contentDescription = null,
                modifier = Modifier.padding(8.dp).padding(end = 16.dp).clip(RoundedCornerShape(12.dp)).fillMaxHeight()
                    .width(125.dp),
                contentScale = ContentScale.FillHeight
            )
        }
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                (meta["name"] ?: meta["title"] ?: "").toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (meta.contains("overview")) {
                Text(
                    meta["overview"]!!.jsonPrimitive.content,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )
            }
        }
    }
    HorizontalDivider()
}