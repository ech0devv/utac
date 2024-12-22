package dev.ech0.torbox.ui.components

import android.R
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
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import dev.ech0.torbox.api.tmdbApi
import dev.ech0.torbox.optString
import org.json.JSONObject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchListItem(meta: JSONObject, setWatchShowPage: (String) -> Unit, setWatchShowJson: (JSONObject) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(200.dp)
            .combinedClickable(onClick = {
                setWatchShowJson(meta)
                setWatchShowPage(meta.getString("media_type"))
            }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(meta.has("poster_path")){
            AsyncImage(
                model =  tmdbApi.imageHelper(meta.getString("poster_path")),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxHeight()
                    .width(125.dp),
                contentScale = ContentScale.FillHeight
            )
        }
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text((optString(meta, "name") ?: optString(meta, "title") ?: ""), style = MaterialTheme.typography.titleMedium,  fontWeight = FontWeight.Bold)
            if (meta.has("overview")) {
                Text(
                    meta.getString("overview"),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )
            }
        }
    }
    HorizontalDivider()
}