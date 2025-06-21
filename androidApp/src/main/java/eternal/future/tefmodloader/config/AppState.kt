package eternal.future.tefmodloader.config

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import eternal.future.tefmodloader.manager.ThemeManager
import eternal.future.tefmodloader.utils.App
import java.io.File

object AppState {

    var autoPatch = mutableStateOf(true)
    var defaultLoader = mutableStateOf(true)

    var isBypass = mutableStateOf(false)
    var Debugging  = mutableStateOf(false)
    var ApkPath = mutableStateOf("")
    var OverrideVersion = mutableStateOf(false)
    var Mode = mutableIntStateOf(0)
    var gamePack = mutableStateOf(false)
    var screen_physical = mutableStateOf(false)
    var screen_rollback = mutableStateOf(false)
    var screen_revolve = mutableStateOf(false)

}