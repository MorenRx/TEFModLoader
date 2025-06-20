package eternal.future.tefmodloader.manager.themes

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import eternal.future.tefmodloader.R

object Autumn: ThemeData() {
    override val nameId: Int = R.string.theme_autumn

    override val lightScheme = lightColorScheme(
        primary = Color(0xFF8F4C38),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFDBD1),
        onPrimaryContainer = Color(0xFF723523),
        secondary = Color(0xFF77574E),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFDBD1),
        onSecondaryContainer = Color(0xFF5D4037),
        tertiary = Color(0xFF6C5D2F),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFF5E1A7),
        onTertiaryContainer = Color(0xFF534619),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF93000A),
        background = Color(0xFFFFF8F6),
        onBackground = Color(0xFF231917),
        surface = Color(0xFFFFF8F6),
        onSurface = Color(0xFF231917),
        surfaceVariant = Color(0xFFF5DED8),
        onSurfaceVariant = Color(0xFF53433F),
        outline = Color(0xFF85736E),
        outlineVariant = Color(0xFFD8C2BC),
        scrim = Color(0xFF000000),
        inverseSurface = Color(0xFF392E2B),
        inverseOnSurface = Color(0xFFFFEDE8),
        inversePrimary = Color(0xFFFFB5A0),
        surfaceDim = Color(0xFFE8D6D2),
        surfaceBright = Color(0xFFFFF8F6),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFFFF1ED),
        surfaceContainer = Color(0xFFFCEAE5),
        surfaceContainerHigh = Color(0xFFF7E4E0),
        surfaceContainerHighest = Color(0xFFF1DFDA)
    )

    override val darkScheme = darkColorScheme(
        primary = Color(0xFFFFB5A0),
        onPrimary = Color(0xFF561F0F),
        primaryContainer = Color(0xFF723523),
        onPrimaryContainer = Color(0xFFFFDBD1),
        secondary = Color(0xFFE7BDB2),
        onSecondary = Color(0xFF442A22),
        secondaryContainer = Color(0xFF5D4037),
        onSecondaryContainer = Color(0xFFFFDBD1),
        tertiary = Color(0xFFD8C58D),
        onTertiary = Color(0xFF3B2F05),
        tertiaryContainer = Color(0xFF534619),
        onTertiaryContainer = Color(0xFFF5E1A7),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF1A110F),
        onBackground = Color(0xFFF1DFDA),
        surface = Color(0xFF1A110F),
        onSurface = Color(0xFFF1DFDA),
        surfaceVariant = Color(0xFF53433F),
        onSurfaceVariant = Color(0xFFD8C2BC),
        outline = Color(0xFFA08C87),
        outlineVariant = Color(0xFF53433F),
        scrim = Color(0xFF000000),
        inverseSurface = Color(0xFFF1DFDA),
        inverseOnSurface = Color(0xFF392E2B),
        inversePrimary = Color(0xFF8F4C38),
        surfaceDim = Color(0xFF1A110F),
        surfaceBright = Color(0xFF423734),
        surfaceContainerLowest = Color(0xFF140C0A),
        surfaceContainerLow = Color(0xFF231917),
        surfaceContainer = Color(0xFF271D1B),
        surfaceContainerHigh = Color(0xFF322825),
        surfaceContainerHighest = Color(0xFF3D322F)
    )
}