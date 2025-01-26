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
import utac.composeapp.generated.resources.Doto
import utac.composeapp.generated.resources.Res

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

    val primaryLightMediumContrast: Color
    val onPrimaryLightMediumContrast: Color
    val primaryContainerLightMediumContrast: Color
    val onPrimaryContainerLightMediumContrast: Color
    val secondaryLightMediumContrast: Color
    val onSecondaryLightMediumContrast: Color
    val secondaryContainerLightMediumContrast: Color
    val onSecondaryContainerLightMediumContrast: Color
    val tertiaryLightMediumContrast: Color
    val onTertiaryLightMediumContrast: Color
    val tertiaryContainerLightMediumContrast: Color
    val onTertiaryContainerLightMediumContrast: Color
    val errorLightMediumContrast: Color
    val onErrorLightMediumContrast: Color
    val errorContainerLightMediumContrast: Color
    val onErrorContainerLightMediumContrast: Color
    val backgroundLightMediumContrast: Color
    val onBackgroundLightMediumContrast: Color
    val surfaceLightMediumContrast: Color
    val onSurfaceLightMediumContrast: Color
    val surfaceVariantLightMediumContrast: Color
    val onSurfaceVariantLightMediumContrast: Color
    val outlineLightMediumContrast: Color
    val outlineVariantLightMediumContrast: Color
    val scrimLightMediumContrast: Color
    val inverseSurfaceLightMediumContrast: Color
    val inverseOnSurfaceLightMediumContrast: Color
    val inversePrimaryLightMediumContrast: Color
    val surfaceDimLightMediumContrast: Color
    val surfaceBrightLightMediumContrast: Color
    val surfaceContainerLowestLightMediumContrast: Color
    val surfaceContainerLowLightMediumContrast: Color
    val surfaceContainerLightMediumContrast: Color
    val surfaceContainerHighLightMediumContrast: Color
    val surfaceContainerHighestLightMediumContrast: Color

    val primaryLightHighContrast: Color
    val onPrimaryLightHighContrast: Color
    val primaryContainerLightHighContrast: Color
    val onPrimaryContainerLightHighContrast: Color
    val secondaryLightHighContrast: Color
    val onSecondaryLightHighContrast: Color
    val secondaryContainerLightHighContrast: Color
    val onSecondaryContainerLightHighContrast: Color
    val tertiaryLightHighContrast: Color
    val onTertiaryLightHighContrast: Color
    val tertiaryContainerLightHighContrast: Color
    val onTertiaryContainerLightHighContrast: Color
    val errorLightHighContrast: Color
    val onErrorLightHighContrast: Color
    val errorContainerLightHighContrast: Color
    val onErrorContainerLightHighContrast: Color
    val backgroundLightHighContrast: Color
    val onBackgroundLightHighContrast: Color
    val surfaceLightHighContrast: Color
    val onSurfaceLightHighContrast: Color
    val surfaceVariantLightHighContrast: Color
    val onSurfaceVariantLightHighContrast: Color
    val outlineLightHighContrast: Color
    val outlineVariantLightHighContrast: Color
    val scrimLightHighContrast: Color
    val inverseSurfaceLightHighContrast: Color
    val inverseOnSurfaceLightHighContrast: Color
    val inversePrimaryLightHighContrast: Color
    val surfaceDimLightHighContrast: Color
    val surfaceBrightLightHighContrast: Color
    val surfaceContainerLowestLightHighContrast: Color
    val surfaceContainerLowLightHighContrast: Color
    val surfaceContainerLightHighContrast: Color
    val surfaceContainerHighLightHighContrast: Color
    val surfaceContainerHighestLightHighContrast: Color

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

    val primaryDarkMediumContrast: Color
    val onPrimaryDarkMediumContrast: Color
    val primaryContainerDarkMediumContrast: Color
    val onPrimaryContainerDarkMediumContrast: Color
    val secondaryDarkMediumContrast: Color
    val onSecondaryDarkMediumContrast: Color
    val secondaryContainerDarkMediumContrast: Color
    val onSecondaryContainerDarkMediumContrast: Color
    val tertiaryDarkMediumContrast: Color
    val onTertiaryDarkMediumContrast: Color
    val tertiaryContainerDarkMediumContrast: Color
    val onTertiaryContainerDarkMediumContrast: Color
    val errorDarkMediumContrast: Color
    val onErrorDarkMediumContrast: Color
    val errorContainerDarkMediumContrast: Color
    val onErrorContainerDarkMediumContrast: Color
    val backgroundDarkMediumContrast: Color
    val onBackgroundDarkMediumContrast: Color
    val surfaceDarkMediumContrast: Color
    val onSurfaceDarkMediumContrast: Color
    val surfaceVariantDarkMediumContrast: Color
    val onSurfaceVariantDarkMediumContrast: Color
    val outlineDarkMediumContrast: Color
    val outlineVariantDarkMediumContrast: Color
    val scrimDarkMediumContrast: Color
    val inverseSurfaceDarkMediumContrast: Color
    val inverseOnSurfaceDarkMediumContrast: Color
    val inversePrimaryDarkMediumContrast: Color
    val surfaceDimDarkMediumContrast: Color
    val surfaceBrightDarkMediumContrast: Color
    val surfaceContainerLowestDarkMediumContrast: Color
    val surfaceContainerLowDarkMediumContrast: Color
    val surfaceContainerDarkMediumContrast: Color
    val surfaceContainerHighDarkMediumContrast: Color
    val surfaceContainerHighestDarkMediumContrast: Color

    val primaryDarkHighContrast: Color
    val onPrimaryDarkHighContrast: Color
    val primaryContainerDarkHighContrast: Color
    val onPrimaryContainerDarkHighContrast: Color
    val secondaryDarkHighContrast: Color
    val onSecondaryDarkHighContrast: Color
    val secondaryContainerDarkHighContrast: Color
    val onSecondaryContainerDarkHighContrast: Color
    val tertiaryDarkHighContrast: Color
    val onTertiaryDarkHighContrast: Color
    val tertiaryContainerDarkHighContrast: Color
    val onTertiaryContainerDarkHighContrast: Color
    val errorDarkHighContrast: Color
    val onErrorDarkHighContrast: Color
    val errorContainerDarkHighContrast: Color
    val onErrorContainerDarkHighContrast: Color
    val backgroundDarkHighContrast: Color
    val onBackgroundDarkHighContrast: Color
    val surfaceDarkHighContrast: Color
    val onSurfaceDarkHighContrast: Color
    val surfaceVariantDarkHighContrast: Color
    val onSurfaceVariantDarkHighContrast: Color
    val outlineDarkHighContrast: Color
    val outlineVariantDarkHighContrast: Color
    val scrimDarkHighContrast: Color
    val inverseSurfaceDarkHighContrast: Color
    val inverseOnSurfaceDarkHighContrast: Color
    val inversePrimaryDarkHighContrast: Color
    val surfaceDimDarkHighContrast: Color
    val surfaceBrightDarkHighContrast: Color
    val surfaceContainerLowestDarkHighContrast: Color
    val surfaceContainerLowDarkHighContrast: Color
    val surfaceContainerDarkHighContrast: Color
    val surfaceContainerHighDarkHighContrast: Color
    val surfaceContainerHighestDarkHighContrast: Color
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

    override val primaryLightMediumContrast = Color(0xFF0D3F17)
    override val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
    override val primaryContainerLightMediumContrast = Color(0xFF477849)
    override val onPrimaryContainerLightMediumContrast = Color(0xFFFFFFFF)
    override val secondaryLightMediumContrast = Color(0xFF2A3A29)
    override val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
    override val secondaryContainerLightMediumContrast = Color(0xFF60725E)
    override val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)
    override val tertiaryLightMediumContrast = Color(0xFF083C42)
    override val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
    override val tertiaryContainerLightMediumContrast = Color(0xFF48747A)
    override val onTertiaryContainerLightMediumContrast = Color(0xFFFFFFFF)
    override val errorLightMediumContrast = Color(0xFF740006)
    override val onErrorLightMediumContrast = Color(0xFFFFFFFF)
    override val errorContainerLightMediumContrast = Color(0xFFCF2C27)
    override val onErrorContainerLightMediumContrast = Color(0xFFFFFFFF)
    override val backgroundLightMediumContrast = Color(0xFFF7FBF2)
    override val onBackgroundLightMediumContrast = Color(0xFF181D17)
    override val surfaceLightMediumContrast = Color(0xFFF7FBF2)
    override val onSurfaceLightMediumContrast = Color(0xFF0E120D)
    override val surfaceVariantLightMediumContrast = Color(0xFFDEE5D9)
    override val onSurfaceVariantLightMediumContrast = Color(0xFF313830)
    override val outlineLightMediumContrast = Color(0xFF4E544B)
    override val outlineVariantLightMediumContrast = Color(0xFF686F65)
    override val scrimLightMediumContrast = Color(0xFF000000)
    override val inverseSurfaceLightMediumContrast = Color(0xFF2D322C)
    override val inverseOnSurfaceLightMediumContrast = Color(0xFFEEF2E9)
    override val inversePrimaryLightMediumContrast = Color(0xFF9FD49C)
    override val surfaceDimLightMediumContrast = Color(0xFFC4C8BF)
    override val surfaceBrightLightMediumContrast = Color(0xFFF7FBF2)
    override val surfaceContainerLowestLightMediumContrast = Color(0xFFFFFFFF)
    override val surfaceContainerLowLightMediumContrast = Color(0xFFF1F5EC)
    override val surfaceContainerLightMediumContrast = Color(0xFFE6E9E1)
    override val surfaceContainerHighLightMediumContrast = Color(0xFFDADED5)
    override val surfaceContainerHighestLightMediumContrast = Color(0xFFCFD3CA)

    override val primaryLightHighContrast = Color(0xFF00340D)
    override val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
    override val primaryContainerLightHighContrast = Color(0xFF235328)
    override val onPrimaryContainerLightHighContrast = Color(0xFFFFFFFF)
    override val secondaryLightHighContrast = Color(0xFF203020)
    override val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
    override val secondaryContainerLightHighContrast = Color(0xFF3D4D3B)
    override val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)
    override val tertiaryLightHighContrast = Color(0xFF003237)
    override val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
    override val tertiaryContainerLightHighContrast = Color(0xFF225056)
    override val onTertiaryContainerLightHighContrast = Color(0xFFFFFFFF)
    override val errorLightHighContrast = Color(0xFF600004)
    override val onErrorLightHighContrast = Color(0xFFFFFFFF)
    override val errorContainerLightHighContrast = Color(0xFF98000A)
    override val onErrorContainerLightHighContrast = Color(0xFFFFFFFF)
    override val backgroundLightHighContrast = Color(0xFFF7FBF2)
    override val onBackgroundLightHighContrast = Color(0xFF181D17)
    override val surfaceLightHighContrast = Color(0xFFF7FBF2)
    override val onSurfaceLightHighContrast = Color(0xFF000000)
    override val surfaceVariantLightHighContrast = Color(0xFFDEE5D9)
    override val onSurfaceVariantLightHighContrast = Color(0xFF000000)
    override val outlineLightHighContrast = Color(0xFF272E26)
    override val outlineVariantLightHighContrast = Color(0xFF444B42)
    override val scrimLightHighContrast = Color(0xFF000000)
    override val inverseSurfaceLightHighContrast = Color(0xFF2D322C)
    override val inverseOnSurfaceLightHighContrast = Color(0xFFFFFFFF)
    override val inversePrimaryLightHighContrast = Color(0xFF9FD49C)
    override val surfaceDimLightHighContrast = Color(0xFFB6BAB2)
    override val surfaceBrightLightHighContrast = Color(0xFFF7FBF2)
    override val surfaceContainerLowestLightHighContrast = Color(0xFFFFFFFF)
    override val surfaceContainerLowLightHighContrast = Color(0xFFEEF2E9)
    override val surfaceContainerLightHighContrast = Color(0xFFE0E4DB)
    override val surfaceContainerHighLightHighContrast = Color(0xFFD2D6CD)
    override val surfaceContainerHighestLightHighContrast = Color(0xFFC4C8BF)

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

    override val primaryDarkMediumContrast = Color(0xFFB4EAB1)
    override val onPrimaryDarkMediumContrast = Color(0xFF002D0A)
    override val primaryContainerDarkMediumContrast = Color(0xFF6A9D6A)
    override val onPrimaryContainerDarkMediumContrast = Color(0xFF000000)
    override val secondaryDarkMediumContrast = Color(0xFFCFE2CA)
    override val onSecondaryDarkMediumContrast = Color(0xFF1A291A)
    override val secondaryContainerDarkMediumContrast = Color(0xFF839680)
    override val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)
    override val tertiaryDarkMediumContrast = Color(0xFFB6E4EB)
    override val onTertiaryDarkMediumContrast = Color(0xFF002A2F)
    override val tertiaryContainerDarkMediumContrast = Color(0xFF6C989F)
    override val onTertiaryContainerDarkMediumContrast = Color(0xFF000000)
    override val errorDarkMediumContrast = Color(0xFFFFD2CC)
    override val onErrorDarkMediumContrast = Color(0xFF540003)
    override val errorContainerDarkMediumContrast = Color(0xFFFF5449)
    override val onErrorContainerDarkMediumContrast = Color(0xFF000000)
    override val backgroundDarkMediumContrast = Color(0xFF101410)
    override val onBackgroundDarkMediumContrast = Color(0xFFE0E4DB)
    override val surfaceDarkMediumContrast = Color(0xFF101410)
    override val onSurfaceDarkMediumContrast = Color(0xFFFFFFFF)
    override val surfaceVariantDarkMediumContrast = Color(0xFF424940)
    override val onSurfaceVariantDarkMediumContrast = Color(0xFFD8DED3)
    override val outlineDarkMediumContrast = Color(0xFFADB4A9)
    override val outlineVariantDarkMediumContrast = Color(0xFF8B9288)
    override val scrimDarkMediumContrast = Color(0xFF000000)
    override val inverseSurfaceDarkMediumContrast = Color(0xFFE0E4DB)
    override val inverseOnSurfaceDarkMediumContrast = Color(0xFF272B26)
    override val inversePrimaryDarkMediumContrast = Color(0xFF225227)
    override val surfaceDimDarkMediumContrast = Color(0xFF101410)
    override val surfaceBrightDarkMediumContrast = Color(0xFF41463F)
    override val surfaceContainerLowestDarkMediumContrast = Color(0xFF050805)
    override val surfaceContainerLowDarkMediumContrast = Color(0xFF1A1F19)
    override val surfaceContainerDarkMediumContrast = Color(0xFF252923)
    override val surfaceContainerHighDarkMediumContrast = Color(0xFF2F342E)
    override val surfaceContainerHighestDarkMediumContrast = Color(0xFF3A3F39)

    override val primaryDarkHighContrast = Color(0xFFC7FEC3)
    override val onPrimaryDarkHighContrast = Color(0xFF000000)
    override val primaryContainerDarkHighContrast = Color(0xFF9BD099)
    override val onPrimaryContainerDarkHighContrast = Color(0xFF000F02)
    override val secondaryDarkHighContrast = Color(0xFFE2F6DD)
    override val onSecondaryDarkHighContrast = Color(0xFF000000)
    override val secondaryContainerDarkHighContrast = Color(0xFFB5C8B1)
    override val onSecondaryContainerDarkHighContrast = Color(0xFF020E03)
    override val tertiaryDarkHighContrast = Color(0xFFCAF8FF)
    override val onTertiaryDarkHighContrast = Color(0xFF000000)
    override val tertiaryContainerDarkHighContrast = Color(0xFF9DCAD1)
    override val onTertiaryContainerDarkHighContrast = Color(0xFF000E10)
    override val errorDarkHighContrast = Color(0xFFFFECE9)
    override val onErrorDarkHighContrast = Color(0xFF000000)
    override val errorContainerDarkHighContrast = Color(0xFFFFAEA4)
    override val onErrorContainerDarkHighContrast = Color(0xFF220001)
    override val backgroundDarkHighContrast = Color(0xFF101410)
    override val onBackgroundDarkHighContrast = Color(0xFFE0E4DB)
    override val surfaceDarkHighContrast = Color(0xFF101410)
    override val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
    override val surfaceVariantDarkHighContrast = Color(0xFF424940)
    override val onSurfaceVariantDarkHighContrast = Color(0xFFFFFFFF)
    override val outlineDarkHighContrast = Color(0xFFEBF2E6)
    override val outlineVariantDarkHighContrast = Color(0xFFBEC5B9)
    override val scrimDarkHighContrast = Color(0xFF000000)
    override val inverseSurfaceDarkHighContrast = Color(0xFFE0E4DB)
    override val inverseOnSurfaceDarkHighContrast = Color(0xFF000000)
    override val inversePrimaryDarkHighContrast = Color(0xFF225227)
    override val surfaceDimDarkHighContrast = Color(0xFF101410)
    override val surfaceBrightDarkHighContrast = Color(0xFF4D514B)
    override val surfaceContainerLowestDarkHighContrast = Color(0xFF000000)
    override val surfaceContainerLowDarkHighContrast = Color(0xFF1C211B)
    override val surfaceContainerDarkHighContrast = Color(0xFF2D322C)
    override val surfaceContainerHighDarkHighContrast = Color(0xFF383D37)
    override val surfaceContainerHighestDarkHighContrast = Color(0xFF434842)
    override val displayFontFamily = Res.font.Doto
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

    override val primaryLightMediumContrast = Color(0xFF5C2235)
    override val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
    override val primaryContainerLightMediumContrast = Color(0xFF9E586C)
    override val onPrimaryContainerLightMediumContrast = Color(0xFFFFFFFF)
    override val secondaryLightMediumContrast = Color(0xFF492F35)
    override val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
    override val secondaryContainerLightMediumContrast = Color(0xFF84656C)
    override val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)
    override val tertiaryLightMediumContrast = Color(0xFF532D00)
    override val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
    override val tertiaryContainerLightMediumContrast = Color(0xFF976127)
    override val onTertiaryContainerLightMediumContrast = Color(0xFFFFFFFF)
    override val errorLightMediumContrast = Color(0xFF740006)
    override val onErrorLightMediumContrast = Color(0xFFFFFFFF)
    override val errorContainerLightMediumContrast = Color(0xFFCF2C27)
    override val onErrorContainerLightMediumContrast = Color(0xFFFFFFFF)
    override val backgroundLightMediumContrast = Color(0xFFFFF8F8)
    override val onBackgroundLightMediumContrast = Color(0xFF22191B)
    override val surfaceLightMediumContrast = Color(0xFFFFF8F8)
    override val onSurfaceLightMediumContrast = Color(0xFF170F11)
    override val surfaceVariantLightMediumContrast = Color(0xFFF3DDE1)
    override val onSurfaceVariantLightMediumContrast = Color(0xFF403336)
    override val outlineLightMediumContrast = Color(0xFF5E4F52)
    override val outlineVariantLightMediumContrast = Color(0xFF79696C)
    override val scrimLightMediumContrast = Color(0xFF000000)
    override val inverseSurfaceLightMediumContrast = Color(0xFF382E30)
    override val inverseOnSurfaceLightMediumContrast = Color(0xFFFEEDEF)
    override val inversePrimaryLightMediumContrast = Color(0xFFFFB1C5)
    override val surfaceDimLightMediumContrast = Color(0xFFD2C3C5)
    override val surfaceBrightLightMediumContrast = Color(0xFFFFF8F8)
    override val surfaceContainerLowestLightMediumContrast = Color(0xFFFFFFFF)
    override val surfaceContainerLowLightMediumContrast = Color(0xFFFFF0F2)
    override val surfaceContainerLightMediumContrast = Color(0xFFF5E4E6)
    override val surfaceContainerHighLightMediumContrast = Color(0xFFE9D9DB)
    override val surfaceContainerHighestLightMediumContrast = Color(0xFFDECED0)

    override val primaryLightHighContrast = Color(0xFF4F182B)
    override val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
    override val primaryContainerLightHighContrast = Color(0xFF733548)
    override val onPrimaryContainerLightHighContrast = Color(0xFFFFFFFF)
    override val secondaryLightHighContrast = Color(0xFF3E252C)
    override val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
    override val secondaryContainerLightHighContrast = Color(0xFF5E4248)
    override val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)
    override val tertiaryLightHighContrast = Color(0xFF452400)
    override val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
    override val tertiaryContainerLightHighContrast = Color(0xFF6D3E04)
    override val onTertiaryContainerLightHighContrast = Color(0xFFFFFFFF)
    override val errorLightHighContrast = Color(0xFF600004)
    override val onErrorLightHighContrast = Color(0xFFFFFFFF)
    override val errorContainerLightHighContrast = Color(0xFF98000A)
    override val onErrorContainerLightHighContrast = Color(0xFFFFFFFF)
    override val backgroundLightHighContrast = Color(0xFFFFF8F8)
    override val onBackgroundLightHighContrast = Color(0xFF22191B)
    override val surfaceLightHighContrast = Color(0xFFFFF8F8)
    override val onSurfaceLightHighContrast = Color(0xFF000000)
    override val surfaceVariantLightHighContrast = Color(0xFFF3DDE1)
    override val onSurfaceVariantLightHighContrast = Color(0xFF000000)
    override val outlineLightHighContrast = Color(0xFF35292C)
    override val outlineVariantLightHighContrast = Color(0xFF544648)
    override val scrimLightHighContrast = Color(0xFF000000)
    override val inverseSurfaceLightHighContrast = Color(0xFF382E30)
    override val inverseOnSurfaceLightHighContrast = Color(0xFFFFFFFF)
    override val inversePrimaryLightHighContrast = Color(0xFFFFB1C5)
    override val surfaceDimLightHighContrast = Color(0xFFC4B5B7)
    override val surfaceBrightLightHighContrast = Color(0xFFFFF8F8)
    override val surfaceContainerLowestLightHighContrast = Color(0xFFFFFFFF)
    override val surfaceContainerLowLightHighContrast = Color(0xFFFEEDEF)
    override val surfaceContainerLightHighContrast = Color(0xFFEFDFE1)
    override val surfaceContainerHighLightHighContrast = Color(0xFFE1D1D3)
    override val surfaceContainerHighestLightHighContrast = Color(0xFFD2C3C5)

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

    override val primaryDarkMediumContrast = Color(0xFFFFD0DA)
    override val onPrimaryDarkMediumContrast = Color(0xFF471225)
    override val primaryContainerDarkMediumContrast = Color(0xFFC77A8F)
    override val onPrimaryContainerDarkMediumContrast = Color(0xFF000000)
    override val secondaryDarkMediumContrast = Color(0xFFFAD2DB)
    override val onSecondaryDarkMediumContrast = Color(0xFF361F25)
    override val secondaryContainerDarkMediumContrast = Color(0xFFAA888F)
    override val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)
    override val tertiaryDarkMediumContrast = Color(0xFFFFD4B0)
    override val onTertiaryDarkMediumContrast = Color(0xFF3B1F00)
    override val tertiaryContainerDarkMediumContrast = Color(0xFFC18447)
    override val onTertiaryContainerDarkMediumContrast = Color(0xFF000000)
    override val errorDarkMediumContrast = Color(0xFFFFD2CC)
    override val onErrorDarkMediumContrast = Color(0xFF540003)
    override val errorContainerDarkMediumContrast = Color(0xFFFF5449)
    override val onErrorContainerDarkMediumContrast = Color(0xFF000000)
    override val backgroundDarkMediumContrast = Color(0xFF191113)
    override val onBackgroundDarkMediumContrast = Color(0xFFEFDFE1)
    override val surfaceDarkMediumContrast = Color(0xFF191113)
    override val onSurfaceDarkMediumContrast = Color(0xFFFFFFFF)
    override val surfaceVariantDarkMediumContrast = Color(0xFF514346)
    override val onSurfaceVariantDarkMediumContrast = Color(0xFFECD7DB)
    override val outlineDarkMediumContrast = Color(0xFFC0ADB0)
    override val outlineVariantDarkMediumContrast = Color(0xFF9E8C8F)
    override val scrimDarkMediumContrast = Color(0xFF000000)
    override val inverseSurfaceDarkMediumContrast = Color(0xFFEFDFE1)
    override val inverseOnSurfaceDarkMediumContrast = Color(0xFF31282A)
    override val inversePrimaryDarkMediumContrast = Color(0xFF723447)
    override val surfaceDimDarkMediumContrast = Color(0xFF191113)
    override val surfaceBrightDarkMediumContrast = Color(0xFF4C4244)
    override val surfaceContainerLowestDarkMediumContrast = Color(0xFF0C0607)
    override val surfaceContainerLowDarkMediumContrast = Color(0xFF241B1D)
    override val surfaceContainerDarkMediumContrast = Color(0xFF2F2527)
    override val surfaceContainerHighDarkMediumContrast = Color(0xFF3A3032)
    override val surfaceContainerHighestDarkMediumContrast = Color(0xFF453B3D)

    override val primaryDarkHighContrast = Color(0xFFFFEBEE)
    override val onPrimaryDarkHighContrast = Color(0xFF000000)
    override val primaryContainerDarkHighContrast = Color(0xFFFFABC1)
    override val onPrimaryContainerDarkHighContrast = Color(0xFF21000A)
    override val secondaryDarkHighContrast = Color(0xFFFFEBEE)
    override val onSecondaryDarkHighContrast = Color(0xFF000000)
    override val secondaryContainerDarkHighContrast = Color(0xFFDFB9C1)
    override val onSecondaryContainerDarkHighContrast = Color(0xFF18060B)
    override val tertiaryDarkHighContrast = Color(0xFFFFEDDF)
    override val onTertiaryDarkHighContrast = Color(0xFF000000)
    override val tertiaryContainerDarkHighContrast = Color(0xFFF9B473)
    override val onTertiaryContainerDarkHighContrast = Color(0xFF160800)
    override val errorDarkHighContrast = Color(0xFFFFECE9)
    override val onErrorDarkHighContrast = Color(0xFF000000)
    override val errorContainerDarkHighContrast = Color(0xFFFFAEA4)
    override val onErrorContainerDarkHighContrast = Color(0xFF220001)
    override val backgroundDarkHighContrast = Color(0xFF191113)
    override val onBackgroundDarkHighContrast = Color(0xFFEFDFE1)
    override val surfaceDarkHighContrast = Color(0xFF191113)
    override val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
    override val surfaceVariantDarkHighContrast = Color(0xFF514346)
    override val onSurfaceVariantDarkHighContrast = Color(0xFFFFFFFF)
    override val outlineDarkHighContrast = Color(0xFFFFEBEE)
    override val outlineVariantDarkHighContrast = Color(0xFFD2BEC1)
    override val scrimDarkHighContrast = Color(0xFF000000)
    override val inverseSurfaceDarkHighContrast = Color(0xFFEFDFE1)
    override val inverseOnSurfaceDarkHighContrast = Color(0xFF000000)
    override val inversePrimaryDarkHighContrast = Color(0xFF723447)
    override val surfaceDimDarkHighContrast = Color(0xFF191113)
    override val surfaceBrightDarkHighContrast = Color(0xFF584D4F)
    override val surfaceContainerLowestDarkHighContrast = Color(0xFF000000)
    override val surfaceContainerLowDarkHighContrast = Color(0xFF261D1F)
    override val surfaceContainerDarkHighContrast = Color(0xFF382E30)
    override val surfaceContainerHighDarkHighContrast = Color(0xFF43393B)
    override val surfaceContainerHighestDarkHighContrast = Color(0xFF4F4446)
    override val displayFontFamily = null
    override val bodyFontFamily = null
}
val themes = mapOf<String, Scheme>(Pair("Torbox", Torbox), Pair("Cherry Blossom", CherryBlossom))
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