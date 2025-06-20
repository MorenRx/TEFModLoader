package eternal.future.tefmodloader.manager.themes

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import eternal.future.tefmodloader.R

object Green : ThemeData() {
    override val nameId: Int = R.string.theme_green

    override val lightScheme = lightColorScheme(
        primary = Color(0xFF4C662B),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFCDEDA3),
        onPrimaryContainer = Color(0xFF354E16),
        secondary = Color(0xFF586249),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFDCE7C8),
        onSecondaryContainer = Color(0xFF404A33),
        tertiary = Color(0xFF386663),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFBCECE7),
        onTertiaryContainer = Color(0xFF1F4E4B),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF93000A),
        background = Color(0xFFF9FAEF),
        onBackground = Color(0xFF1A1C16),
        surface = Color(0xFFF9FAEF),
        onSurface = Color(0xFF1A1C16),
        surfaceVariant = Color(0xFFE1E4D5),
        onSurfaceVariant = Color(0xFF44483D),
        outline = Color(0xFF75796C),
        outlineVariant = Color(0xFFC5C8BA),
        scrim = Color(0xFF000000),
        inverseSurface = Color(0xFF2F312A),
        inverseOnSurface = Color(0xFFF1F2E6),
        inversePrimary = Color(0xFFB1D18A),
        surfaceDim = Color(0xFFDADBD0),
        surfaceBright = Color(0xFFF9FAEF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF3F4E9),
        surfaceContainer = Color(0xFFEEEFE3),
        surfaceContainerHigh = Color(0xFFE8E9DE),
        surfaceContainerHighest = Color(0xFFE2E3D8),
    )

    override val darkScheme = darkColorScheme(
        primary = Color(0xFFB1D18A),
        onPrimary = Color(0xFF1F3701),
        primaryContainer = Color(0xFF354E16),
        onPrimaryContainer = Color(0xFFCDEDA3),
        secondary = Color(0xFFBFCBAD),
        onSecondary = Color(0xFF2A331E),
        secondaryContainer = Color(0xFF404A33),
        onSecondaryContainer = Color(0xFFDCE7C8),
        tertiary = Color(0xFFA0D0CB),
        onTertiary = Color(0xFF003735),
        tertiaryContainer = Color(0xFF1F4E4B),
        onTertiaryContainer = Color(0xFFBCECE7),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF12140E),
        onBackground = Color(0xFFE2E3D8),
        surface = Color(0xFF12140E),
        onSurface = Color(0xFFE2E3D8),
        surfaceVariant = Color(0xFF44483D),
        onSurfaceVariant = Color(0xFFC5C8BA),
        outline = Color(0xFF8F9285),
        outlineVariant = Color(0xFF44483D),
        scrim = Color(0xFF000000),
        inverseSurface = Color(0xFFE2E3D8),
        inverseOnSurface = Color(0xFF2F312A),
        inversePrimary = Color(0xFF4C662B),
        surfaceDim = Color(0xFF12140E),
        surfaceBright = Color(0xFF383A32),
        surfaceContainerLowest = Color(0xFF0C0F09),
        surfaceContainerLow = Color(0xFF1A1C16),
        surfaceContainer = Color(0xFF1E201A),
        surfaceContainerHigh = Color(0xFF282B24),
        surfaceContainerHighest = Color(0xFF33362E),
    )
}