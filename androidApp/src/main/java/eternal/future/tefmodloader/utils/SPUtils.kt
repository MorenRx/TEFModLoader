package eternal.future.tefmodloader.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SPUtils(context: Context, configName: String) {
    private val sp: SharedPreferences = context.getSharedPreferences(configName, Context.MODE_PRIVATE);

    fun getString(key: String, default: String): String =
        sp.getString(key, default) ?: default

    fun putString(key: String, value: String) {
        sp.edit { putString(key, value) }
    }

    fun getInt(key: String, default: Int): Int =
        sp.getInt(key, default)

    fun putInt(key: String, value: Int) {
        sp.edit { putInt(key, value) }
    }

    fun getBoolean(key: String, default: Boolean): Boolean =
        sp.getBoolean(key, default)

    fun putBoolean(key: String, value: Boolean) {
        sp.edit { putBoolean(key, value) }
    }
}