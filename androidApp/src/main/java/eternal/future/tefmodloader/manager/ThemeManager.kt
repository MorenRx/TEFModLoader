package eternal.future.tefmodloader.manager

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.manager.themes.Autumn
import eternal.future.tefmodloader.manager.themes.Default
import eternal.future.tefmodloader.manager.themes.Green
import eternal.future.tefmodloader.manager.themes.Ocean
import eternal.future.tefmodloader.manager.themes.Pink
import eternal.future.tefmodloader.manager.themes.Purple
import eternal.future.tefmodloader.manager.themes.ThemeData
import eternal.future.tefmodloader.manager.themes.Yellow
import kotlin.collections.map


object ThemeManager {
    val allThemes: List<ThemeData> = listOf(Default, Ocean, Autumn, Green, Yellow, Pink, Purple)

    val optionsTheme: List<Pair<Int, ImageVector>> = allThemes.map { it -> it.nameId to it.icon }
    val optionsDarkMode: List<Int> = listOf(
        R.string.follow_system,
        R.string.dark_mode_enable,
        R.string.dark_mode_disable
    )

    @Composable
    fun ComposeTheme(
        darkMode: Int = 0,
        themeId: Int = 0,
        content: @Composable () -> Unit
    ) {
        val validThemeId = themeId.coerceIn(0, allThemes.lastIndex)

        val isDarkTheme = when (darkMode) {
            1 -> true
            2 -> false
            else -> isSystemInDarkTheme()
        }

        val colorScheme = if (validThemeId == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        } else {
            val currentTheme = allThemes[validThemeId]
            if (isDarkTheme) currentTheme.darkScheme
            else currentTheme.lightScheme
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography(),
            content = content
        )
    }
}

