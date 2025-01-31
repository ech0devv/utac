package dev.ech0.torbox.multiplatform.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import dev.ech0.torbox.multiplatform.api.base
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource
import utac.composeapp.generated.resources.Res
import utac.composeapp.generated.resources.Doto

interface Scheme {
    val primaryLight: Color
    val onPrimaryLight: Color
    val primaryContainerLight: Color
    val onPrimaryContainerLight: Color
    val secondaryLight: Color
    val onSecondaryLight: Color
    val secondaryContainerLight: Color
    val onSecondaryContainerLight: Color
    val tertiaryLight: Color
    val onTertiaryLight: Color
    val tertiaryContainerLight: Color
    val onTertiaryContainerLight: Color
    val errorLight: Color
    val onErrorLight: Color
    val errorContainerLight: Color
    val onErrorContainerLight: Color
    val backgroundLight: Color
    val onBackgroundLight: Color
    val surfaceLight: Color
    val onSurfaceLight: Color
    val surfaceVariantLight: Color
    val onSurfaceVariantLight: Color
    val outlineLight: Color
    val outlineVariantLight: Color
    val scrimLight: Color
    val inverseSurfaceLight: Color
    val inverseOnSurfaceLight: Color
    val inversePrimaryLight: Color
    val surfaceDimLight: Color
    val surfaceBrightLight: Color
    val surfaceContainerLowestLight: Color
    val surfaceContainerLowLight: Color
    val surfaceContainerLight: Color
    val surfaceContainerHighLight: Color
    val surfaceContainerHighestLight: Color

    val primaryDark: Color
    val onPrimaryDark: Color
    val primaryContainerDark: Color
    val onPrimaryContainerDark: Color
    val secondaryDark: Color
    val onSecondaryDark: Color
    val secondaryContainerDark: Color
    val onSecondaryContainerDark: Color
    val tertiaryDark: Color
    val onTertiaryDark: Color
    val tertiaryContainerDark: Color
    val onTertiaryContainerDark: Color
    val errorDark: Color
    val onErrorDark: Color
    val errorContainerDark: Color
    val onErrorContainerDark: Color
    val backgroundDark: Color
    val onBackgroundDark: Color
    val surfaceDark: Color
    val onSurfaceDark: Color
    val surfaceVariantDark: Color
    val onSurfaceVariantDark: Color
    val outlineDark: Color
    val outlineVariantDark: Color
    val scrimDark: Color
    val inverseSurfaceDark: Color
    val inverseOnSurfaceDark: Color
    val inversePrimaryDark: Color
    val surfaceDimDark: Color
    val surfaceBrightDark: Color
    val surfaceContainerLowestDark: Color
    val surfaceContainerLowDark: Color
    val surfaceContainerDark: Color
    val surfaceContainerHighDark: Color
    val surfaceContainerHighestDark: Color
    val displayFontFamily: FontResource?
    val bodyFontFamily: FontResource?
}

object Torbox : Scheme {
    override val primaryLight = Color(0xFF39693B)
    override val onPrimaryLight = Color(0xFFFFFFFF)
    override val primaryContainerLight = Color(0xFFBAF0B7)
    override val onPrimaryContainerLight = Color(0xFF205026)
    override val secondaryLight = Color(0xFF526350)
    override val onSecondaryLight = Color(0xFFFFFFFF)
    override val secondaryContainerLight = Color(0xFFD5E8D0)
    override val onSecondaryContainerLight = Color(0xFF3A4B39)
    override val tertiaryLight = Color(0xFF39656B)
    override val onTertiaryLight = Color(0xFFFFFFFF)
    override val tertiaryContainerLight = Color(0xFFBCEBF2)
    override val onTertiaryContainerLight = Color(0xFF1F4D53)
    override val errorLight = Color(0xFFBA1A1A)
    override val onErrorLight = Color(0xFFFFFFFF)
    override val errorContainerLight = Color(0xFFFFDAD6)
    override val onErrorContainerLight = Color(0xFF93000A)
    override val backgroundLight = Color(0xFFF7FBF2)
    override val onBackgroundLight = Color(0xFF181D17)
    override val surfaceLight = Color(0xFFF7FBF2)
    override val onSurfaceLight = Color(0xFF181D17)
    override val surfaceVariantLight = Color(0xFFDEE5D9)
    override val onSurfaceVariantLight = Color(0xFF424940)
    override val outlineLight = Color(0xFF72796F)
    override val outlineVariantLight = Color(0xFFC2C9BD)
    override val scrimLight = Color(0xFF000000)
    override val inverseSurfaceLight = Color(0xFF2D322C)
    override val inverseOnSurfaceLight = Color(0xFFEEF2E9)
    override val inversePrimaryLight = Color(0xFF9FD49C)
    override val surfaceDimLight = Color(0xFFD7DBD3)
    override val surfaceBrightLight = Color(0xFFF7FBF2)
    override val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    override val surfaceContainerLowLight = Color(0xFFF1F5EC)
    override val surfaceContainerLight = Color(0xFFEBEFE6)
    override val surfaceContainerHighLight = Color(0xFFE6E9E1)
    override val surfaceContainerHighestLight = Color(0xFFE0E4DB)

    override val primaryDark = Color(0xFF9FD49C)
    override val onPrimaryDark = Color(0xFF053911)
    override val primaryContainerDark = Color(0xFF205026)
    override val onPrimaryContainerDark = Color(0xFFBAF0B7)
    override val secondaryDark = Color(0xFFB9CCB4)
    override val onSecondaryDark = Color(0xFF243424)
    override val secondaryContainerDark = Color(0xFF3A4B39)
    override val onSecondaryContainerDark = Color(0xFFD5E8D0)
    override val tertiaryDark = Color(0xFFA1CED5)
    override val onTertiaryDark = Color(0xFF00363C)
    override val tertiaryContainerDark = Color(0xFF1F4D53)
    override val onTertiaryContainerDark = Color(0xFFBCEBF2)
    override val errorDark = Color(0xFFFFB4AB)
    override val onErrorDark = Color(0xFF690005)
    override val errorContainerDark = Color(0xFF93000A)
    override val onErrorContainerDark = Color(0xFFFFDAD6)
    override val backgroundDark = Color(0xFF101410)
    override val onBackgroundDark = Color(0xFFE0E4DB)
    override val surfaceDark = Color(0xFF101410)
    override val onSurfaceDark = Color(0xFFE0E4DB)
    override val surfaceVariantDark = Color(0xFF424940)
    override val onSurfaceVariantDark = Color(0xFFC2C9BD)
    override val outlineDark = Color(0xFF8C9388)
    override val outlineVariantDark = Color(0xFF424940)
    override val scrimDark = Color(0xFF000000)
    override val inverseSurfaceDark = Color(0xFFE0E4DB)
    override val inverseOnSurfaceDark = Color(0xFF2D322C)
    override val inversePrimaryDark = Color(0xFF39693B)
    override val surfaceDimDark = Color(0xFF101410)
    override val surfaceBrightDark = Color(0xFF363A34)
    override val surfaceContainerLowestDark = Color(0xFF0B0F0A)
    override val surfaceContainerLowDark = Color(0xFF181D17)
    override val surfaceContainerDark = Color(0xFF1C211B)
    override val surfaceContainerHighDark = Color(0xFF272B26)
    override val surfaceContainerHighestDark = Color(0xFF313630)
    override val displayFontFamily = Res.font.Doto
    override val bodyFontFamily = null
}
object AMOLED : Scheme {
    override val primaryLight = Color(0xFF39693B)
    override val onPrimaryLight = Color(0xFFFFFFFF)
    override val primaryContainerLight = Color(0xFFBAF0B7)
    override val onPrimaryContainerLight = Color(0xFF205026)
    override val secondaryLight = Color(0xFF526350)
    override val onSecondaryLight = Color(0xFFFFFFFF)
    override val secondaryContainerLight = Color(0xFFD5E8D0)
    override val onSecondaryContainerLight = Color(0xFF3A4B39)
    override val tertiaryLight = Color(0xFF39656B)
    override val onTertiaryLight = Color(0xFFFFFFFF)
    override val tertiaryContainerLight = Color(0xFFBCEBF2)
    override val onTertiaryContainerLight = Color(0xFF1F4D53)
    override val errorLight = Color(0xFFBA1A1A)
    override val onErrorLight = Color(0xFFFFFFFF)
    override val errorContainerLight = Color(0xFFFFDAD6)
    override val onErrorContainerLight = Color(0xFF93000A)
    override val backgroundLight = Color(0xFFF7FBF2)
    override val onBackgroundLight = Color(0xFF181D17)
    override val surfaceLight = Color(0xFFF7FBF2)
    override val onSurfaceLight = Color(0xFF181D17)
    override val surfaceVariantLight = Color(0xFFDEE5D9)
    override val onSurfaceVariantLight = Color(0xFF424940)
    override val outlineLight = Color(0xFF72796F)
    override val outlineVariantLight = Color(0xFFC2C9BD)
    override val scrimLight = Color(0xFF000000)
    override val inverseSurfaceLight = Color(0xFF2D322C)
    override val inverseOnSurfaceLight = Color(0xFFEEF2E9)
    override val inversePrimaryLight = Color(0xFF9FD49C)
    override val surfaceDimLight = Color(0xFFD7DBD3)
    override val surfaceBrightLight = Color(0xFFF7FBF2)
    override val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    override val surfaceContainerLowLight = Color(0xFFF1F5EC)
    override val surfaceContainerLight = Color(0xFFEBEFE6)
    override val surfaceContainerHighLight = Color(0xFFE6E9E1)
    override val surfaceContainerHighestLight = Color(0xFFE0E4DB)

    override val primaryDark = Color(0xFF9FD49C)
    override val onPrimaryDark = Color(0xFF053911)
    override val primaryContainerDark = Color(0xFF000000)
    override val onPrimaryContainerDark = Color(0xFFBAF0B7)
    override val secondaryDark = Color(0xFFB9CCB4)
    override val onSecondaryDark = Color(0xFF243424)
    override val secondaryContainerDark = Color(0xFF000000)
    override val onSecondaryContainerDark = Color(0xFFD5E8D0)
    override val tertiaryDark = Color(0xFFA1CED5)
    override val onTertiaryDark = Color(0xFF00363C)
    override val tertiaryContainerDark = Color(0xFF1F4D53)
    override val onTertiaryContainerDark = Color(0xFFBCEBF2)
    override val errorDark = Color(0xFFFFB4AB)
    override val onErrorDark = Color(0xFF690005)
    override val errorContainerDark = Color(0xFF93000A)
    override val onErrorContainerDark = Color(0xFFFFDAD6)
    override val backgroundDark = Color(0xFF000000)
    override val onBackgroundDark = Color(0xFFE0E4DB)
    override val surfaceDark = Color(0xFF000000)
    override val onSurfaceDark = Color(0xFFE0E4DB)
    override val surfaceVariantDark = Color(0xFF424940)
    override val onSurfaceVariantDark = Color(0xFFC2C9BD)
    override val outlineDark = Color(0xFF8C9388)
    override val outlineVariantDark = Color(0xFF424940)
    override val scrimDark = Color(0xFF000000)
    override val inverseSurfaceDark = Color(0xFFE0E4DB)
    override val inverseOnSurfaceDark = Color(0xFF2D322C)
    override val inversePrimaryDark = Color(0xFF39693B)
    override val surfaceDimDark = Color(0xFF101410)
    override val surfaceBrightDark = Color(0xFF363A34)
    override val surfaceContainerLowestDark = Color(0xFF0B0F0A)
    override val surfaceContainerLowDark = Color(0xFF181D17)
    override val surfaceContainerDark = Color(0xFF000000)
    override val surfaceContainerHighDark = Color(0xFF000000)
    override val surfaceContainerHighestDark = Color(0xFF313630)
    override val displayFontFamily = null
    override val bodyFontFamily = null
}
object CherryBlossom: Scheme {
    override val primaryLight = Color(0xFF8C4A5D)
    override val onPrimaryLight = Color(0xFFFFFFFF)
    override val primaryContainerLight = Color(0xFFFFD9E1)
    override val onPrimaryContainerLight = Color(0xFF703346)
    override val secondaryLight = Color(0xFF75565D)
    override val onSecondaryLight = Color(0xFFFFFFFF)
    override val secondaryContainerLight = Color(0xFFFFD9E1)
    override val onSecondaryContainerLight = Color(0xFF5B3F46)
    override val tertiaryLight = Color(0xFF86521A)
    override val onTertiaryLight = Color(0xFFFFFFFF)
    override val tertiaryContainerLight = Color(0xFFFFDCBF)
    override val onTertiaryContainerLight = Color(0xFF6A3B02)
    override val errorLight = Color(0xFFBA1A1A)
    override val onErrorLight = Color(0xFFFFFFFF)
    override val errorContainerLight = Color(0xFFFFDAD6)
    override val onErrorContainerLight = Color(0xFF93000A)
    override val backgroundLight = Color(0xFFFFF8F8)
    override val onBackgroundLight = Color(0xFF22191B)
    override val surfaceLight = Color(0xFFFFF8F8)
    override val onSurfaceLight = Color(0xFF22191B)
    override val surfaceVariantLight = Color(0xFFF3DDE1)
    override val onSurfaceVariantLight = Color(0xFF514346)
    override val outlineLight = Color(0xFF847376)
    override val outlineVariantLight = Color(0xFFD6C2C5)
    override val scrimLight = Color(0xFF000000)
    override val inverseSurfaceLight = Color(0xFF382E30)
    override val inverseOnSurfaceLight = Color(0xFFFEEDEF)
    override val inversePrimaryLight = Color(0xFFFFB1C5)
    override val surfaceDimLight = Color(0xFFE6D6D8)
    override val surfaceBrightLight = Color(0xFFFFF8F8)
    override val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    override val surfaceContainerLowLight = Color(0xFFFFF0F2)
    override val surfaceContainerLight = Color(0xFFFBEAEC)
    override val surfaceContainerHighLight = Color(0xFFF5E4E6)
    override val surfaceContainerHighestLight = Color(0xFFEFDFE1)

    override val primaryDark = Color(0xFFFFB1C5)
    override val onPrimaryDark = Color(0xFF551D2F)
    override val primaryContainerDark = Color(0xFF703346)
    override val onPrimaryContainerDark = Color(0xFFFFD9E1)
    override val secondaryDark = Color(0xFFE3BDC5)
    override val onSecondaryDark = Color(0xFF422930)
    override val secondaryContainerDark = Color(0xFF5B3F46)
    override val onSecondaryContainerDark = Color(0xFFFFD9E1)
    override val tertiaryDark = Color(0xFFFEB876)
    override val onTertiaryDark = Color(0xFF4B2800)
    override val tertiaryContainerDark = Color(0xFF6A3B02)
    override val onTertiaryContainerDark = Color(0xFFFFDCBF)
    override val errorDark = Color(0xFFFFB4AB)
    override val onErrorDark = Color(0xFF690005)
    override val errorContainerDark = Color(0xFF93000A)
    override val onErrorContainerDark = Color(0xFFFFDAD6)
    override val backgroundDark = Color(0xFF191113)
    override val onBackgroundDark = Color(0xFFEFDFE1)
    override val surfaceDark = Color(0xFF191113)
    override val onSurfaceDark = Color(0xFFEFDFE1)
    override val surfaceVariantDark = Color(0xFF514346)
    override val onSurfaceVariantDark = Color(0xFFD6C2C5)
    override val outlineDark = Color(0xFF9E8C8F)
    override val outlineVariantDark = Color(0xFF514346)
    override val scrimDark = Color(0xFF000000)
    override val inverseSurfaceDark = Color(0xFFEFDFE1)
    override val inverseOnSurfaceDark = Color(0xFF382E30)
    override val inversePrimaryDark = Color(0xFF8C4A5D)
    override val surfaceDimDark = Color(0xFF191113)
    override val surfaceBrightDark = Color(0xFF413739)
    override val surfaceContainerLowestDark = Color(0xFF140C0E)
    override val surfaceContainerLowDark = Color(0xFF22191B)
    override val surfaceContainerDark = Color(0xFF261D1F)
    override val surfaceContainerHighDark = Color(0xFF312829)
    override val surfaceContainerHighestDark = Color(0xFF3C3234)
    override val displayFontFamily = null
    override val bodyFontFamily = null
}
object Blues : Scheme {
    override val primaryLight = Color(0xFF266489)
    override val onPrimaryLight = Color(0xFFFFFFFF)
    override val primaryContainerLight = Color(0xFFC9E6FF)
    override val onPrimaryContainerLight = Color(0xFF004B6F)
    override val secondaryLight = Color(0xFF50606E)
    override val onSecondaryLight = Color(0xFFFFFFFF)
    override val secondaryContainerLight = Color(0xFFD3E5F5)
    override val onSecondaryContainerLight = Color(0xFF384956)
    override val tertiaryLight = Color(0xFF64597B)
    override val onTertiaryLight = Color(0xFFFFFFFF)
    override val tertiaryContainerLight = Color(0xFFEADDFF)
    override val onTertiaryContainerLight = Color(0xFF4C4163)
    override val errorLight = Color(0xFFBA1A1A)
    override val onErrorLight = Color(0xFFFFFFFF)
    override val errorContainerLight = Color(0xFFFFDAD6)
    override val onErrorContainerLight = Color(0xFF93000A)
    override val backgroundLight = Color(0xFFF7F9FF)
    override val onBackgroundLight = Color(0xFF181C20)
    override val surfaceLight = Color(0xFFF7F9FF)
    override val onSurfaceLight = Color(0xFF181C20)
    override val surfaceVariantLight = Color(0xFFDDE3EA)
    override val onSurfaceVariantLight = Color(0xFF41474D)
    override val outlineLight = Color(0xFF72787E)
    override val outlineVariantLight = Color(0xFFC1C7CE)
    override val scrimLight = Color(0xFF000000)
    override val inverseSurfaceLight = Color(0xFF2D3135)
    override val inverseOnSurfaceLight = Color(0xFFEEF1F6)
    override val inversePrimaryLight = Color(0xFF95CDF7)
    override val surfaceDimLight = Color(0xFFD7DADF)
    override val surfaceBrightLight = Color(0xFFF7F9FF)
    override val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    override val surfaceContainerLowLight = Color(0xFFF1F4F9)
    override val surfaceContainerLight = Color(0xFFEBEEF3)
    override val surfaceContainerHighLight = Color(0xFFE5E8ED)
    override val surfaceContainerHighestLight = Color(0xFFE0E3E8)

    override val primaryDark = Color(0xFF95CDF7)
    override val onPrimaryDark = Color(0xFF00344E)
    override val primaryContainerDark = Color(0xFF004B6F)
    override val onPrimaryContainerDark = Color(0xFFC9E6FF)
    override val secondaryDark = Color(0xFFB7C9D9)
    override val onSecondaryDark = Color(0xFF22323F)
    override val secondaryContainerDark = Color(0xFF384956)
    override val onSecondaryContainerDark = Color(0xFFD3E5F5)
    override val tertiaryDark = Color(0xFFCEC0E8)
    override val onTertiaryDark = Color(0xFF352B4B)
    override val tertiaryContainerDark = Color(0xFF4C4163)
    override val onTertiaryContainerDark = Color(0xFFEADDFF)
    override val errorDark = Color(0xFFFFB4AB)
    override val onErrorDark = Color(0xFF690005)
    override val errorContainerDark = Color(0xFF93000A)
    override val onErrorContainerDark = Color(0xFFFFDAD6)
    override val backgroundDark = Color(0xFF101417)
    override val onBackgroundDark = Color(0xFFE0E3E8)
    override val surfaceDark = Color(0xFF101417)
    override val onSurfaceDark = Color(0xFFE0E3E8)
    override val surfaceVariantDark = Color(0xFF41474D)
    override val onSurfaceVariantDark = Color(0xFFC1C7CE)
    override val outlineDark = Color(0xFF8B9198)
    override val outlineVariantDark = Color(0xFF41474D)
    override val scrimDark = Color(0xFF000000)
    override val inverseSurfaceDark = Color(0xFFE0E3E8)
    override val inverseOnSurfaceDark = Color(0xFF2D3135)
    override val inversePrimaryDark = Color(0xFF266489)
    override val surfaceDimDark = Color(0xFF101417)
    override val surfaceBrightDark = Color(0xFF363A3E)
    override val surfaceContainerLowestDark = Color(0xFF0B0F12)
    override val surfaceContainerLowDark = Color(0xFF181C20)
    override val surfaceContainerDark = Color(0xFF1C2024)
    override val surfaceContainerHighDark = Color(0xFF262A2E)
    override val surfaceContainerHighestDark = Color(0xFF313539)

    override val displayFontFamily = null
    override val bodyFontFamily = null
}
val themes = mapOf<String, Scheme>(Pair("Torbox", Torbox), Pair("Cherry Blossom", CherryBlossom), Pair("Blues", Blues), Pair("AMOLED (dark theme only)", AMOLED))
@Composable
fun AppTheme(themeName: String, darkTheme: Boolean = true, content: @Composable() () -> Unit){
    val theme = themes[themeName]!!
    val scheme = if(!darkTheme){
        lightColorScheme(
            primary = theme.primaryLight,
            onPrimary = theme.onPrimaryLight,
            primaryContainer = theme.primaryContainerLight,
            onPrimaryContainer = theme.onPrimaryContainerLight,
            secondary = theme.secondaryLight,
            onSecondary = theme.onSecondaryLight,
            secondaryContainer = theme.secondaryContainerLight,
            onSecondaryContainer = theme.onSecondaryContainerLight,
            tertiary = theme.tertiaryLight,
            onTertiary = theme.onTertiaryLight,
            tertiaryContainer = theme.tertiaryContainerLight,
            onTertiaryContainer = theme.onTertiaryContainerLight,
            error = theme.errorLight,
            onError = theme.onErrorLight,
            errorContainer = theme.errorContainerLight,
            onErrorContainer = theme.onErrorContainerLight,
            background = theme.backgroundLight,
            onBackground = theme.onBackgroundLight,
            surface = theme.surfaceLight,
            onSurface = theme.onSurfaceLight,
            surfaceVariant = theme.surfaceVariantLight,
            onSurfaceVariant = theme.onSurfaceVariantLight,
            outline = theme.outlineLight,
            outlineVariant = theme.outlineVariantLight,
            scrim = theme.scrimLight,
            inverseSurface = theme.inverseSurfaceLight,
            inverseOnSurface = theme.inverseOnSurfaceLight,
            inversePrimary = theme.inversePrimaryLight,
            surfaceDim = theme.surfaceDimLight,
            surfaceBright = theme.surfaceBrightLight,
            surfaceContainerLowest = theme.surfaceContainerLowestLight,
            surfaceContainerLow = theme.surfaceContainerLowLight,
            surfaceContainer = theme.surfaceContainerLight,
            surfaceContainerHigh = theme.surfaceContainerHighLight,
            surfaceContainerHighest = theme.surfaceContainerHighestLight,
        )
    }else{
        darkColorScheme(
            primary = theme.primaryDark,
            onPrimary = theme.onPrimaryDark,
            primaryContainer = theme.primaryContainerDark,
            onPrimaryContainer = theme.onPrimaryContainerDark,
            secondary = theme.secondaryDark,
            onSecondary = theme.onSecondaryDark,
            secondaryContainer = theme.secondaryContainerDark,
            onSecondaryContainer = theme.onSecondaryContainerDark,
            tertiary = theme.tertiaryDark,
            onTertiary = theme.onTertiaryDark,
            tertiaryContainer = theme.tertiaryContainerDark,
            onTertiaryContainer = theme.onTertiaryContainerDark,
            error = theme.errorDark,
            onError = theme.onErrorDark,
            errorContainer = theme.errorContainerDark,
            onErrorContainer = theme.onErrorContainerDark,
            background = theme.backgroundDark,
            onBackground = theme.onBackgroundDark,
            surface = theme.surfaceDark,
            onSurface = theme.onSurfaceDark,
            surfaceVariant = theme.surfaceVariantDark,
            onSurfaceVariant = theme.onSurfaceVariantDark,
            outline = theme.outlineDark,
            outlineVariant = theme.outlineVariantDark,
            scrim = theme.scrimDark,
            inverseSurface = theme.inverseSurfaceDark,
            inverseOnSurface = theme.inverseOnSurfaceDark,
            inversePrimary = theme.inversePrimaryDark,
            surfaceDim = theme.surfaceDimDark,
            surfaceBright = theme.surfaceBrightDark,
            surfaceContainerLowest = theme.surfaceContainerLowestDark,
            surfaceContainerLow = theme.surfaceContainerLowDark,
            surfaceContainer = theme.surfaceContainerDark,
            surfaceContainerHigh = theme.surfaceContainerHighDark,
            surfaceContainerHighest = theme.surfaceContainerHighestDark,
        )
    }
    val baseline = Typography()
    var displayFontFamily = baseline.displayMedium.fontFamily
    var bodyFontFamily = baseline.bodyMedium.fontFamily
    if(theme.displayFontFamily != null){
        displayFontFamily = FontFamily(Font(theme.displayFontFamily!!))
    }
    if(theme.bodyFontFamily != null){
        bodyFontFamily = FontFamily(Font(theme.bodyFontFamily!!))
    }
    val AppTypography = Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )
    MaterialTheme(colorScheme = scheme, typography = AppTypography,content = content)
}