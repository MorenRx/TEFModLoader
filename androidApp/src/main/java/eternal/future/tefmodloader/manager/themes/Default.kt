package eternal.future.tefmodloader.manager.themes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import eternal.future.tefmodloader.R

object Default: ThemeData() {
    override val nameId: Int = R.string.theme_default

    override val icon: ImageVector = Icons.Default.Info

    override var lightScheme = Ocean.lightScheme
    override var darkScheme = Ocean.darkScheme


}