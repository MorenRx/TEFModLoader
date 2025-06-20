package eternal.future.tefmodloader.manager.themes

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ThemeData {
    abstract val nameId: Int
    abstract val icon: ImageVector
    abstract val lightScheme: ColorScheme
    abstract val darkScheme: ColorScheme
}