package eternal.future.tefmodloader.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import eternal.future.tefmodloader.activity.MainActivity

object Net {
    @SuppressLint("UseKtx")
    fun openUrlInBrowser(url: String) {
        try {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            ContextCompat.startActivity(MainActivity.getContext(), browserIntent, Bundle())
        } catch (e: Exception) {
            Log.e("utility.Net","无法打开链接: $url, 错误: ", e)
        }
    }
}