package eternal.future.tefmodloader.manager

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.config.AppPrefs
import java.util.Locale

object I18N {

    val allLanguages: List<Pair<Int, Locale>> = listOf(
        R.string.follow_system to Locale.ROOT,
        R.string.lang_zh_rcn to Locale.SIMPLIFIED_CHINESE,
        R.string.lang_zh_rhk to Locale.TRADITIONAL_CHINESE,
        R.string.lang_en to Locale.US
    )

    val optionsLanguage: List<Int> = allLanguages.map { it.first }

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context
        val language = AppPrefs.language
        if (language != 0) setLocale(allLanguages[language].second)

        Log.d("I18N", "初始化完毕")
    }

    fun setLocale(locale: Locale) {
        val resources = appContext.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun text(@StringRes resId: Int): CharSequence {
        return appContext.getText(resId)
    }

    fun string(@StringRes resId: Int): String {
        return appContext.getString(resId)
    }
}