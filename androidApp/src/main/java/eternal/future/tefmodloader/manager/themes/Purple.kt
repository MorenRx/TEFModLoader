package eternal.future.tefmodloader.manager.themes

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import eternal.future.tefmodloader.R

object Purple: ThemeData() {
    override val nameId: Int = R.string.theme_purple

    override val lightScheme = lightColorScheme(
        primary = Color(0xFF5F5790),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFE5DEFF),
        onPrimaryContainer = Color(0xFF473F77),
        secondary = Color(0xFF605C71),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE5DFF9),
        onSecondaryContainer = Color(0xFF484459),
        tertiary = Color(0xFF7C5264),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFD8E6),
        onTertiaryContainer = Color(0xFF623B4C),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF93000A),
        background = Color(0xFFFDF8FF),
        onBackground = Color(0xFF1C1B20),
        surface = Color(0xFFFDF8FF),
        onSurface = Color(0xFF1C1B20),
        surfaceVariant = Color(0xFFE5E0EC),
        onSurfaceVariant = Color(0xFF48454E),
        outline = Color(0xFF79757F),
        outlineVariant = Color(0xFFC9C5D0),
        scrim = Color(0xFF000000),
        inverseSurface = Color(0xFF312F36),
        inverseOnSurface = Color(0xFFF4EFF7),
        inversePrimary = Color(0xFFC9BFFF),
        surfaceDim = Color(0xFFDDD8E0),
        surfaceBright = Color(0xFFFDF8FF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF7F2FA),
        surfaceContainer = Color(0xFFF1ECF4),
        surfaceContainerHigh = Color(0xFFEBE6EE),
        surfaceContainerHighest = Color(0xFFE5E1E9),
    )

    override val darkScheme = darkColorScheme(
        primary = Color(0xFFC9BFFF),
        onPrimary = Color(0xFF31285F),
        primaryContainer = Color(0xFF473F77),
        onPrimaryContainer = Color(0xFFE5DEFF),
        secondary = Color(0xFFC9C3DC),
        onSecondary = Color(0xFF312E41),
        secondaryContainer = Color(0xFF484459),
        onSecondaryContainer = Color(0xFFE5DFF9),
        tertiary = Color(0xFFEDB8CD),
        onTertiary = Color(0xFF482536),
        tertiaryContainer = Color(0xFF623B4C),
        onTertiaryContainer = Color(0xFFFFD8E6),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF141318),
        onBackground = Color(0xFFE5E1E9),
        surface = Color(0xFF141318),
        onSurface = Color(0xFFE5E1E9),
        surfaceVariant = Color(0xFF48454E),
        onSurfaceVariant = Color(0xFFC9C5D0),
        outline = Color(0xFF938F99),
        outlineVariant = Color(0xFF48454E),
        scrim = Color(0xFF000000),
        inverseSurface = Color(0xFFE5E1E9),
        inverseOnSurface = Color(0xFF312F36),
        inversePrimary = Color(0xFF5F5790),
        surfaceDim = Color(0xFF141318),
        surfaceBright = Color(0xFF3A383E),
        surfaceContainerLowest = Color(0xFF0E0D13),
        surfaceContainerLow = Color(0xFF1C1B20),
        surfaceContainer = Color(0xFF201F25),
        surfaceContainerHigh = Color(0xFF2B292F),
        surfaceContainerHighest = Color(0xFF35343A),
    )
}