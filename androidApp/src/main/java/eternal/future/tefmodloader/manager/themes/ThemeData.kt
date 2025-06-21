package eternal.future.tefmodloader.manager.themes

import androidx.compose.material3.ColorScheme

sealed class ThemeData {
    abstract val nameId: Int
    abstract val lightScheme: ColorScheme
    abstract val darkScheme: ColorScheme
}