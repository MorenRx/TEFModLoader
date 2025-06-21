package eternal.future.tefmodloader.manager

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.config.AppPrefs
import eternal.future.tefmodloader.config.AppPrefsOld
import eternal.future.tefmodloader.manager.ThemeManager.allThemes
import java.util.Locale

object I18N {

    val allLanguages = listOf(
        R.string.follow_system to Locale.ROOT,
        R.string.lang_zh_rcn to Locale.SIMPLIFIED_CHINESE,
        R.string.lang_zh_rhk to Locale.TRADITIONAL_CHINESE,
        R.string.lang_en to Locale.US
    )

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context
        val language = AppPrefsOld.language
        if (language != 0) setLocale(allLanguages[language].second)

        Log.d("I18N", "初始化完毕")
    }

    fun setLocale(locale: Locale) {
        val resources = appContext.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun getCurrentLocale(): Locale {
        return getLocale(AppPrefs.language.intValue)
    }

    fun getLocale(id: Int): Locale {
        val validId = id.coerceIn(0, allLanguages.lastIndex)
        val result: Locale = if (validId == 0)
            Locale.getDefault() else allLanguages[validId].second
        return result
    }

    @Composable
    fun text(@StringRes resId: Int): String {
        return stringResource(id = resId)
    }

    @Composable
    fun texts(@StringRes resIds: List<Int>): List<String> {
        return resIds.map { text(it) }
    }
}