package dev.ech0.torbox.multiplatform.ui.pages


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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.russhwolf.settings.Settings
import dev.ech0.torbox.multiplatform.BuildConfig
import dev.ech0.torbox.multiplatform.LocalNavController
import dev.ech0.torbox.multiplatform.api.*
import dev.ech0.torbox.multiplatform.theme.AppTheme
import dev.ech0.torbox.multiplatform.theme.themes
import dev.ech0.torbox.multiplatform.ui.components.ApiPrompt
import dev.ech0.torbox.multiplatform.ui.components.LoadingScreen
import dev.ech0.torbox.multiplatform.ui.components.TraktPrompt
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.painterResource
import utac.composeapp.generated.resources.Res
import utac.composeapp.generated.resources.tmdb
import utac.composeapp.generated.resources.trakt

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(setColorScheme: (String) -> Unit = {}, setDarkTheme: (Boolean) -> Unit) {
    var apiPrompt by remember { mutableStateOf(false) }
    var traktPrompt by remember { mutableStateOf(false) }

    var shouldLoad by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    var dropdownOpen by remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(Settings().getBoolean("dark", true)) }
    var usenetChecked by remember { mutableStateOf(Settings().getBoolean("usenet", true)) }
    var adultChecked by remember { mutableStateOf(Settings().getBoolean("adultContent", false)) }
    var searchChecked by remember { mutableStateOf(Settings().getBoolean("searchTop", false)) }
    var blurChecked by remember { mutableStateOf(Settings().getBoolean("blurDL", false)) }

    var openSourceDialog by remember { mutableStateOf(false) }
    var adultContentDialog by remember { mutableStateOf(false) }
    var showApikey by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        if (torboxAPI.getApiKey() != "") {
            torboxAPI.checkApiKey(torboxAPI.getApiKey())
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .blur(
                if (shouldLoad) {
                    10.dp
                } else {
                    0.dp
                }
            )
            .verticalScroll(rememberScrollState())
    ) {
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
                when (Settings().getInt("plan", 4)) {
                    0 -> plan = "Free"
                    1 -> plan = "Essential, thanks for supporting Torbox!"
                    2 -> plan = "Pro, thanks for supporting Torbox!"
                    3 -> plan = "Standard, thanks for supporting Torbox!"
                    4 -> plan = "Unknown"
                }
                Text(
                    text = "Plan: $plan",
                    color = colors[Settings().getInt("plan", 4)],
                    style = MaterialTheme.typography.bodySmall
                )
                if (Settings().getString("userdata", "") != "") {
                    Text(
                        text = "Expires in: ${
                            Clock.System.now().daysUntil(
                                Instant.parse(
                                    Settings().getString("userdata", "{}")
                                        .let { Json.parseToJsonElement(it).jsonObject["premium_expires_at"]!!.jsonPrimitive.content ?: "0000-01-01T00:00:00Z" }),
                                timeZone = TimeZone.currentSystemDefault()
                            )
                        } days",
                        color = colors[Settings().getInt("plan", 4)],
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
                Icons.Filled.ArrowRight,
                "Right Arrow",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                            torboxAPI.checkApiKey(torboxAPI.getApiKey())
                        } catch (e: Exception) {
                            navController.navigate("Error/${e.toString().encodeURLPath()}")
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
                Icons.Filled.ArrowRight,
                "Right Arrow",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
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
        if (Settings().getString("traktToken", "") == "") {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .combinedClickable(onClick = {
                        traktPrompt = true
                    }), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.trakt),
                    contentDescription = "Trakt",
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("Log in to Trakt")
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Filled.ArrowRight,
                    "Right Arrow",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .combinedClickable(onClick = {
                        Settings().putString("traktToken", "")
                        traktApi = Trakt()
                    }), verticalAlignment = Alignment.CenterVertically
            ) {
                /*Icon(
                    painter = painterResource(R.drawable.trakt),
                    contentDescription = "Trakt",
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )*/
                Text("Log out from Trakt")
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Filled.ArrowRight,
                    "Right Arrow",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                        Settings().putBoolean("adultContent", false)
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
                    Settings().putBoolean("usenet", it); usenetChecked = it;
                }, enabled = Settings().getInt("plan", 4) == 2, thumbContent = {
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
                Text("Pick theme")
                Text(
                    "Make UTAC look all pretty :3",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(250.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            Box {
                Button(onClick = {dropdownOpen = true}){
                    Text("Pick")
                }
                DropdownMenu(expanded = dropdownOpen, onDismissRequest = { dropdownOpen = false }) {
                    themes.forEach {
                        val dark = Settings().getBoolean(
                            "dark",
                            true
                        )
                        AppTheme(it.key, dark){
                            Box(modifier = Modifier.background(if(dark) it.value.backgroundDark else it.value.backgroundLight)){
                                DropdownMenuItem(text = {
                                    Column {
                                        Text(it.key, style = MaterialTheme.typography.titleSmall)
                                        Text(it.key, style = MaterialTheme.typography.bodySmall)
                                    }
                                }, onClick = {
                                    setColorScheme(it.key)
                                    dropdownOpen = false
                                })
                            }
                        }
                    }
                }
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.DarkMode,
                "Dark theme",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column() {
                Text("Dark theme")
                Text(
                    "Bravo Six, Going Dark",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(250.dp),
                )
            }
            Spacer(Modifier.weight(1f))
            Switch(
                checked = darkMode, onCheckedChange = {
                    Settings().putBoolean("dark", it); darkMode = it; setDarkTheme(it)
                }, thumbContent = {
                    AnimatedContent(darkMode, transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
                    }) { checked ->
                        if (checked) {
                            Icon(
                                imageVector = Icons.Filled.DarkMode,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.LightMode,
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
                    Settings().putBoolean("searchTop", it); searchChecked = it
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
                    Settings().putBoolean("blurDL", it); blurChecked = it
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
                "ech0's birthday in ... days",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column() {
                Text(
                    "ech0's birthday in ${
                        Clock.System.now().daysUntil(
                            Instant.parse("2025-01-26T09:30:00-05:00"),
                            timeZone = TimeZone.currentSystemDefault()
                        )
                    } days", Modifier.padding(start = 8.dp)
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.tmdb),
                contentDescription = "TMDB Logo",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
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
            BuildConfig.VERSION,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall
        )
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
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.NoAdultContent,
                        "Enable adult content?",
                        modifier = Modifier.padding(bottom = 0.dp)
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
                                Settings().putBoolean("adultContent", true)
                                adultChecked = true
                                haptics.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
                                tmdbApi = TMDBApi()
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