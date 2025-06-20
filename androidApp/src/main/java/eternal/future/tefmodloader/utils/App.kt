package eternal.future.tefmodloader.utils

import android.os.Build
import eternal.future.tefmodloader.activity.MainActivity

object App {
    fun exit() {
        MainActivity.exit()
    }
    fun getCurrentArchitecture(): String {
        return Build.CPU_ABI
    }
}