package dev.ech0.torbox.ui.pages

import android.graphics.RenderEffect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.preference.PreferenceManager
import coil3.compose.AsyncImage
import dev.ech0.torbox.BuildConfig
import dev.ech0.torbox.LocalNavController
import dev.ech0.torbox.R
import dev.ech0.torbox.api.TMDBApi
import dev.ech0.torbox.api.tmdbApi
import dev.ech0.torbox.api.torboxAPI
import dev.ech0.torbox.ui.components.ApiPrompt
import dev.ech0.torbox.ui.components.LoadingScreen
import dev.ech0.torbox.ui.components.TraktPrompt
import dev.ech0.torbox.ui.theme.amoledScheme
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(setColorScheme: (ColorScheme?) -> Unit = {}) {
    var apiPrompt by remember { mutableStateOf(false) }
    var traktPrompt by remember { mutableStateOf(false) }

    var shouldLoad by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    val context = LocalContext.current
    val preferences = remember { PreferenceManager.getDefaultSharedPreferences(context) }
    var amoChecked by remember { mutableStateOf(preferences.getBoolean("amoled", false)) }
    var usenetChecked by remember { mutableStateOf(preferences.getBoolean("usenet", true)) }
    var adultChecked by remember { mutableStateOf(preferences.getBoolean("adultContent", false)) }
    var searchChecked by remember { mutableStateOf(preferences.getBoolean("searchTop", false)) }
    var blurChecked by remember { mutableStateOf(preferences.getBoolean("blurDL", false)) }
    var openSourceDialog by remember { mutableStateOf(false) }
    var adultContentDialog by remember { mutableStateOf(false) }
    var showApikey by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        if (torboxAPI.getApiKey() != "") {
            torboxAPI.checkApiKey(torboxAPI.getApiKey(), context)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                if (shouldLoad) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        renderEffect =
                            RenderEffect.createBlurEffect(25f, 25f, Shader.TileMode.MIRROR).asComposeRenderEffect()
                    }
                }
            }
            .verticalScroll(rememberScrollState())) {
        Text(
            "Account",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 16.dp)
        )
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Person,
                "Account details",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column {
                Text("Account Details")
                var plan = "Unknown"
                var colors = arrayOf(
                    MaterialTheme.colorScheme.onSurfaceVariant,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.error
                )
                when (preferences.getInt("plan", 4)) {
                    0 -> plan = "Free"
                    1 -> plan = "Essential, thanks for supporting Torbox!"
                    2 -> plan = "Pro, thanks for supporting Torbox!"
                    3 -> plan = "Standard, thanks for supporting Torbox!"
                    4 -> plan = "Unknown"
                }
                Text(
                    text = "Plan: $plan",
                    color = colors[preferences.getInt("plan", 4)],
                    style = MaterialTheme.typography.bodySmall
                )
                // convert this to days away from now: 2025-01-28T21:04:56Z
                if (preferences.getString("userdata", "") != "") {
                    Text(
                        text = "Expires in: ${
                            ChronoUnit.DAYS.between(
                                Instant.now(), Instant.from(
                                    DateTimeFormatter.ISO_INSTANT.parse(
                                        preferences.getString("userdata", "{}")
                                            ?.let { JSONObject(it).getString("premium_expires_at") })
                                )
                            )
                        } days",
                        color = colors[preferences.getInt("plan", 4)],
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .padding(16.dp)
                .combinedClickable(onClick = {
                    apiPrompt = true
                })
        ) {
            Icon(
                Icons.Outlined.Key,
                "Set API Key",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Set API Key")
            Spacer(Modifier.weight(1f))
            Icon(
                Icons.Filled.ArrowRight, "Right Arrow", tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .padding(16.dp)
                .combinedClickable(onClick = {
                    scope.launch {
                        shouldLoad = true
                        try {
                            torboxAPI.checkApiKey(torboxAPI.getApiKey(), context)
                        } catch (e: Exception) {
                            navController.navigate("Error/${Uri.encode(e.toString())}")
                        }
                        shouldLoad = false
                    }
                })
        ) {
            Icon(
                Icons.Outlined.Refresh,
                "Refresh account details",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Refresh account details")
            Spacer(Modifier.weight(1f))
            Icon(
                Icons.Filled.ArrowRight, "Right Arrow", tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }/*Text(
            "Configuration",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )
        Row(
            modifier = Modifier.padding(16.dp).clickable {
                val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }, verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.VideoFile,
                "change default video player",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp),
                )
            Column() {
                Text("Change default video player")
                Text(
                    "Shortcut to open system settings",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.weight(1f))
            Icon(
                Icons.Filled.ArrowRight, "Right Arrow", tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }*/
        Text(
            "Tracking",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )
        if (preferences.getString("traktToken", "") == "") {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .combinedClickable(onClick = {
                        traktPrompt = true
                    }), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.trakt),
                    contentDescription = "Trakt",
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("Log in to Trakt")
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Filled.ArrowRight, "Right Arrow", tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }else{
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .combinedClickable(onClick = {
                        preferences.edit().putString("traktToken", "").apply()
                    }), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.trakt),
                    contentDescription = "Trakt",
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("Log out from Trakt")
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Filled.ArrowRight, "Right Arrow", tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            "Configuration",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.NoAdultContent,
                "Enable adult content.",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column() {
                Text("Enable adult content.")
                Text(
                    "Shows sexy stuff.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.weight(1f))
            Switch(
                checked = adultChecked, onCheckedChange = {
                    if (!it) {
                        preferences.edit().putBoolean("adultContent", false).apply()
                        Toast.makeText(context, "good boy", Toast.LENGTH_SHORT).show()
                        adultChecked = it
                    } else {
                        adultContentDialog = true
                    }
                }, thumbContent = {
                    AnimatedContent(adultChecked, transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
                    }) { checked ->
                        if (checked) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    }
                }, modifier = Modifier.padding(all = 0.dp)
            )
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Newspaper,
                "Disable usenet searching",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column() {
                Text("Enable usenet searching")
                Text(
                    "Can help fix really slow searches if turned off. Requires pro.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(250.dp),

                    )
            }
            Spacer(Modifier.weight(1f))
            Switch(
                checked = usenetChecked, onCheckedChange = {
                    preferences.edit().putBoolean("usenet", it).apply(); usenetChecked = it;
                }, enabled = preferences.getInt("plan", 4) == 2, thumbContent = {
                    AnimatedContent(usenetChecked, transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
                    }) { checked ->
                        if (checked) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    }
                }, modifier = Modifier.padding(all = 0.dp)
            )
        }
        Text(
            "Appearance",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )

        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Colorize,
                "Enable AMOLED Theme",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column() {
                Text("Enable AMOLED Theme")
                Text(
                    "Makes colors black, saves battery on most smartphones.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(250.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            Switch(
                checked = amoChecked, onCheckedChange = {
                    preferences.edit().putBoolean("amoled", it).apply(); amoChecked = it; if (it) {
                    setColorScheme(amoledScheme)
                } else {
                    setColorScheme(null)
                }
                }, thumbContent = {
                    AnimatedContent(amoChecked, transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
                    }) { checked ->
                        if (checked) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    }
                }, modifier = Modifier.padding(all = 0.dp)
            )
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Search,
                "Search bar position",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column() {
                Text("Search bar position")
                Text(
                    "Select where the search bar goes on your screen.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(250.dp),
                )
            }
            Spacer(Modifier.weight(1f))
            Switch(
                checked = searchChecked, onCheckedChange = {
                    preferences.edit().putBoolean("searchTop", it).apply(); searchChecked = it
                }, thumbContent = {

                    AnimatedContent(searchChecked, transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
                    }) { checked ->
                        if (checked) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowUp,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    }

                }, modifier = Modifier.padding(all = 0.dp)
            )
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.BlurOn,
                "Blur download names",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column() {
                Text("Blur download names")
                Text(
                    "Blur download names, for testing and tech support.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(250.dp),
                )
            }
            Spacer(Modifier.weight(1f))
            Switch(
                checked = blurChecked, onCheckedChange = {
                    preferences.edit().putBoolean("blurDL", it).apply(); blurChecked = it
                }, thumbContent = {

                    AnimatedContent(blurChecked, transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
                    }) { checked ->
                        if (checked) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    }

                }, modifier = Modifier.padding(all = 0.dp)
            )
        }
        Text(
            "Credits",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Celebration,
                "Merry Christmas, happy Hanukkah",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column() {
                Text("Merry Christmas, happy Hanukkah", Modifier.padding(start = 8.dp))
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = R.drawable.tmdb, contentDescription = null, modifier = Modifier.size(32.dp)
            )
            Column() {
                Text("Show metadata by TMDB", Modifier.padding(start = 8.dp))
            }

        }
        HorizontalDivider()
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Favorite,
                "made with love by ech0",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp),

                )
            Column() {
                Text("made with love by ech0", Modifier.padding(start = 8.dp))
            }
        }/*
                                                                val playVideo = Intent(Intent.ACTION_VIEW)
                                                        playVideo.setDataAndType(
                                                            Uri.parse(linkJSON.getString("data")), "video/x-unknown"
                                                        )
                                                        context.startActivity(playVideo)
         */
        HorizontalDivider()
        Row(
            modifier = Modifier
                .padding(16.dp)
                .combinedClickable(onClick = {
                    openSourceDialog = true
                }), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Code,
                "Open source licenses",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp),

                )
            Column() {
                Text("Open source licenses", Modifier.padding(start = 8.dp))
            }
        }
        Text(
            BuildConfig.VERSION_NAME,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
    if (openSourceDialog) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            AndroidView(factory = {
                WebView(it).apply {
                    loadUrl("file:///android_asset/open_source_licenses.html")
                }
            })
        }
    }
    if (apiPrompt) {
        ApiPrompt({ apiPrompt = false }, navController)
    }
    if (traktPrompt) {
        TraktPrompt({ traktPrompt = false }, navController)
    }
    if (adultContentDialog) {
        val haptics = LocalHapticFeedback.current
        Dialog(onDismissRequest = { adultContentDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                ) {
                Column(
                    modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.NoAdultContent, "Enable adult content?", modifier = Modifier.padding(bottom = 0.dp)
                    )
                    Text(
                        "Enable adult content?",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        "Adult content has been shown to alter brain chemistry, especially in younger individuals, which may lead to addiction and other negative effects. Are you sure you want to enable adult content?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .combinedClickable(onClick = {}, onLongClick = {
                                preferences.edit().putBoolean("adultContent", true).apply()
                                adultChecked = true
                                haptics.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
                                Toast.makeText(context, "you horny fuck", Toast.LENGTH_SHORT).show()
                                tmdbApi = TMDBApi(preferences)
                                adultContentDialog = false
                            })
                    ) {
                        Box(modifier = Modifier.padding(vertical = 12.dp, horizontal = 25.dp)) {
                            Text(
                                "I am over 18, and understand the risks. (hold)",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
    if (shouldLoad) {
        LoadingScreen()
    }
}