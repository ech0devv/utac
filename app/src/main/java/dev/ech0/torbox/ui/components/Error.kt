package dev.ech0.torbox.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.ech0.torbox.api.torboxAPI

val errorStrings = arrayOf(
    "oopsies :3",
    "well shit.",
    "that's not supposed to happen.",
    "that's totally supposed to happen.",
    "owchiee :(",
    "MAYDAY MAYDAY MAYDAY!!",
    "not againnn",
    "AAA A BUG",
    "holy hell",
    "holy api calls",
    "This isnt even a bruh moment anymore. What the fuck man.",
    "I'm sad. Life is sad.",
    "man",
    "You cheeky little sausage. You know what you did.",
    "Not my problem.",
    "Too bad, so sad.",
    "It wasnt me, I swear!",
    "PANPAN!! PANPAN!! PANPAN!!!",
    "You've gotta be kidding me.",
    "No more linux isos :(",
    "oopsie woopsie (´ω｀), we made a fucky wucky~~ (◡w◡) :3",
    "this is what you get. we know what you did. all sins can be repented with time and effort. start now.",
    "rawr x3 uwu *nuzzles you* *pounces on you* uwu u so warm :3"
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DisplayError(
    recoverable: Boolean = false,
    what: String = Exception("Achievement get: How did we get here?").toString()
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(align = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(
            text = ">.<",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            style = MaterialTheme.typography.displayLargeEmphasized
        )
        Text(
            text = errorStrings.random(),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .padding(horizontal = 8.dp),
            style = MaterialTheme.typography.titleMedium
        )
        if(!what.matches(Regex(".*\\..*\\..*", RegexOption.DOT_MATCHES_ALL))){
            Text(
                text = what.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }else {
            Text(
                text = "looks like we messed up. ${if (recoverable) "reloading..." else ""}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (!recoverable) {
            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(what.toString()))
                    Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.padding(bottom = 8.dp)
            ) { Text("grab what()") }
            Text(
                text = "If you didn't expect this to happen, click the above button and send it to the developer.\nIf you did expect it to happen, send it to me anyways. So I can tell the expect the program to expect it to happen. Or something.",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 24.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}