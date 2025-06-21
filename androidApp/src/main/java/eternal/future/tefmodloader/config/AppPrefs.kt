package eternal.future.tefmodloader.config

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object AppPrefs {
    private lateinit var appContext: Context
    private val Context.dataStore by preferencesDataStore(name = "TEFModLoaderConfig")

    fun init(context: Context) {
        appContext = context.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = appContext.dataStore.data.first()
            darkMode.intValue = prefs[KEY_DARK_MODE] ?: 0
            language.intValue = prefs[KEY_LANGUAGE] ?: 0
            theme.intValue = prefs[KEY_THEME] ?: 0
        }
    }

    private val KEY_DARK_MODE = intPreferencesKey("dark_mode")
    val darkMode = mutableIntStateOf(0)
    fun setDarkMode(value: Int) {
        darkMode.intValue = value
        persist(KEY_DARK_MODE, value)
    }


    private val KEY_LANGUAGE = intPreferencesKey("language")
    val language = mutableIntStateOf(0)
    fun setLanguage(value: Int) {
        language.intValue = value
        persist(KEY_LANGUAGE, value)
    }


    private val KEY_THEME = intPreferencesKey("theme")
    val theme = mutableIntStateOf(0)
    fun setTheme(value: Int) {
        theme.intValue = value
        persist(KEY_THEME, value)
    }



    private fun persist(key: Preferences.Key<Int>, value: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            appContext.dataStore.edit { it[key] = value }
        }
    }
}