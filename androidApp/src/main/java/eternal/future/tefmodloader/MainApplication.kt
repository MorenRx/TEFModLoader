package eternal.future.tefmodloader

import android.annotation.SuppressLint
import android.app.Application
import eternal.future.tefmodloader.config.AppConf
import eternal.future.tefmodloader.config.AppPrefs
import eternal.future.tefmodloader.config.AppPrefsOld
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.utils.Apk

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppConf.init(this)

        //不太完善准备替换掉
        AppPrefsOld.init(this)

        AppPrefs.init(this)
        I18N.init(this)

        //临时添加
        Apk.init(this)

        System.loadLibrary("silkcasket")
    }
}