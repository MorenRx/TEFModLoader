package eternal.future.tefmodloader.config

import android.content.Context
import android.util.Log
import eternal.future.tefmodloader.utils.SPUtils


object AppPrefsOld {

    private lateinit var appContext: Context
    private lateinit var utils: SPUtils

    fun init(context: Context) {
        appContext = context
        utils = SPUtils(context, AppConf.SP_CONFIG_NAME)

        Log.d("SP", "配置:${AppConf.SP_CONFIG_NAME}初始化完毕")
    }


    var isFirstLaunch: Boolean
        get() = utils.getBoolean(AppConf.SP_KEY_FIRST_LAUNCH, true)
        set(value) = utils.putBoolean(AppConf.SP_KEY_FIRST_LAUNCH, value)

    var isExternalMode: Boolean
        get() = utils.getBoolean(AppConf.SP_KEY_EXTERNAL_MODE, false)
        set(value) = utils.putBoolean(AppConf.SP_KEY_EXTERNAL_MODE, value)

    var theme: Int
        get() = utils.getInt(AppConf.SP_KEY_THEME, 0)
        set(value) = utils.putInt(AppConf.SP_KEY_THEME, value)

    var language: Int
        get() = utils.getInt(AppConf.SP_KEY_LANGUAGE, 0)
        set(value) = utils.putInt(AppConf.SP_KEY_LANGUAGE, value)

    var darkMode: Int
        get() = utils.getInt(AppConf.SP_KEY_DARK_MODE, 0)
        set(value) = utils.putInt(AppConf.SP_KEY_DARK_MODE, value)

    var logCacheMaximum: Int
        get() = utils.getInt(AppConf.SP_KEY_LOG_CACHE_MAXIMUM, 0)
        set(value) = utils.putInt(AppConf.SP_KEY_LOG_CACHE_MAXIMUM, value)

    var architecture: Int
        get() = utils.getInt(AppConf.SP_KEY_ARCHITECTURE, 0)
        set(value) = utils.putInt(AppConf.SP_KEY_ARCHITECTURE, value)


}
