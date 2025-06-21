package eternal.future.tefmodloader.config

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import eternal.future.tefmodloader.utils.App
import java.io.File

object AppConf {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context
        Log.d("Config", "初始化完毕")
    }

    const val SP_CONFIG_NAME = "TEFModLoaderConfig"
    const val SP_KEY_THEME = "theme"
    const val SP_KEY_LANGUAGE = "language"
    const val SP_KEY_DARK_MODE = "dark_mode"
    const val SP_KEY_FIRST_LAUNCH = "is_first_launch"
    const val SP_KEY_EXTERNAL_MODE = "is_external_mode"
    const val SP_KEY_LOG_CACHE_MAXIMUM = "log_cache_maximum"
    const val SP_KEY_ARCHITECTURE = "architecture"


    val SilkCasket_Temp by lazy { File(appContext.filesDir, "SilkCasket_Temp").path }
    val PATH_EFMOD by lazy { File(appContext.getExternalFilesDir(null), "EFMod").path }
    val PATH_EFMOD_LOADER by lazy { File(appContext.getExternalFilesDir(null), "EFModLoaderPath").path }
}