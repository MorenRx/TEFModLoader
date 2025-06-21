package eternal.future.tefmodloader.manager

import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.manager.I18N.allLanguages
import eternal.future.tefmodloader.manager.ThemeManager.allThemes

object OptionManager {

    val optionsTheme: List<Int> = allThemes.map { it -> it.nameId }

    val optionsDarkMode: List<Int> = listOf(
        R.string.follow_system,
        R.string.dark_mode_enable,
        R.string.dark_mode_disable
    )

    val optionsLanguage = allLanguages.map { it.first }

    val optionsLauncherMode = listOf(
        R.string.auto,
        R.string.launch_mode_external
        //R.string.launch_mode_share,
        //R.string.launch_mode_inline,
        //R.string.launch_mode_root
    )
}