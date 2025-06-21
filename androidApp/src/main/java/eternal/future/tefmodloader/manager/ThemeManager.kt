package eternal.future.tefmodloader.manager

import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
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

    @Composable
    fun I18NTheme(
        language: Int,
        theme: Int,
        darkMode: Int,
        content: @Composable () -> Unit
    ) {
        val context = LocalContext.current
        val localizedContext = context.createConfigurationContext(remember(language) {
            Configuration(context.resources.configuration).apply {
                setLocale(I18N.getLocale(language))
            }
        })

        val isDarkTheme = when (darkMode) {
            1 -> true
            2 -> false
            else -> isSystemInDarkTheme()
        }

        val validThemeId = theme.coerceIn(0, allThemes.lastIndex)

        val colorScheme = if (validThemeId == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (isDarkTheme) dynamicDarkColorScheme(localizedContext)
            else dynamicLightColorScheme(localizedContext)
        } else {
            val currentTheme = allThemes[validThemeId]
            if (isDarkTheme) currentTheme.darkScheme
            else currentTheme.lightScheme
        }

        val registryOwner = LocalActivityResultRegistryOwner.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current

        CompositionLocalProvider(
            LocalContext provides localizedContext,
            LocalActivityResultRegistryOwner provides registryOwner!!,
            LocalLifecycleOwner provides lifecycleOwner,
            LocalSavedStateRegistryOwner provides savedStateRegistryOwner
        ) {
            MaterialTheme(
                colorScheme = colorScheme,
                typography = Typography(),
                content = content
            )
        }
    }

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

