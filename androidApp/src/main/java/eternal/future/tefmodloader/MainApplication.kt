package eternal.future.tefmodloader

import android.annotation.SuppressLint
import android.app.Application
import eternal.future.tefmodloader.config.AppConf
import eternal.future.tefmodloader.utils.App
import eternal.future.tefmodloader.config.AppPrefs
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.utils.Zip
import java.io.File

class MainApplication: Application() {

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    override fun onCreate() {
        super.onCreate()
        AppConf.init(this)
        AppPrefs.init(this)
        I18N.init(this)

        File(filesDir, "SilkCasket").let {
            if (!it.exists()) {
                val zipPath = Zip.copyZipFromResources("SilkCasket.zip", "${it.parent}")
                Zip.unzipSpecificFilesIgnorePath(zipPath, it.path,  "android/${App.getCurrentArchitecture()}/libsilkcasket.so")
                File(zipPath).delete()
            }
            System.load(it.path)
            print("已加载")
        }
    }

}