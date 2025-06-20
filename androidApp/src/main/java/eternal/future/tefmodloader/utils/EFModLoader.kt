package eternal.future.tefmodloader.utils

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import eternal.future.tefmodloader.config.AppConf
import net.peanuuutz.tomlkt.Toml
import net.peanuuutz.tomlkt.getArray
import net.peanuuutz.tomlkt.getBoolean
import net.peanuuutz.tomlkt.getInteger
import net.peanuuutz.tomlkt.getString
import net.peanuuutz.tomlkt.getTable
import eternal.future.tefmodloader.config.AppState
import eternal.future.tefmodloader.data.Compatibility
import eternal.future.tefmodloader.data.EFModLoader
import eternal.future.tefmodloader.data.Github
import eternal.future.tefmodloader.data.Info
import eternal.future.tefmodloader.data.Introduction
import eternal.future.tefmodloader.data.Loader
import eternal.future.tefmodloader.data.PlatformSupport
import eternal.future.tefmodloader.data.Platforms
import eternal.future.tefmodloader.data.SupportModeAndroid
import eternal.future.tefmodloader.data.SupportModeWindows
import eternal.future.tefmodloader.data.SupportModes
import eternal.future.tefmodloader.manager.I18N
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object EFModLoader {

    fun install(loaderFile: String, targetDirectory: String): Pair<Boolean, String> {
        EFLog.d("开始安装MOD文件: $loaderFile 到目录: $targetDirectory")
        val expectedHeader = byteArrayOf(
            0x53, 0x69, 0x6C, 0x6B, 0x43, 0x61, 0x73, 0x6B, 0x65, 0x74,
            0x00, 0x03, 0xFE.toByte(), 0x34, 0x01
        )

        try {
            FileInputStream(loaderFile).use { fis ->
                val header = ByteArray(expectedHeader.size)
                val bytesRead = fis.read(header)

                if (bytesRead == expectedHeader.size && header.contentEquals(expectedHeader)) {
                    EFLog.i("验证通过，开始释放LOADER文件: $loaderFile 到目标目录: $targetDirectory")
                    SilkCasket.release(AppConf.SilkCasket_Temp, loaderFile, targetDirectory)
                    EFLog.d("成功安装LOADER文件: $loaderFile 到目录: $targetDirectory")
                } else {
                    EFLog.e("文件头验证失败: 文件 '$loaderFile' 不是有效的LOADER文件")
                    return Pair(false, "error")
                }
            }
        } catch (e: IOException) {
            EFLog.e("安装LOADERv文件时发生IO异常", e)
            return Pair(false, e.toString())
        }
        return Pair(true, "succeed")
    }

    fun update(loaderFile: String, targetDirectory: String): Pair<Boolean, String> {
        val expectedHeader = byteArrayOf(
            0x53, 0x69, 0x6C, 0x6B, 0x43, 0x61, 0x73, 0x6B, 0x65, 0x74,
            0x00, 0x03, 0xFE.toByte(), 0x34, 0x01
        )

        try {
            FileInputStream(loaderFile).use { fis ->
                val header = ByteArray(expectedHeader.size)
                val bytesRead = fis.read(header)

                if (bytesRead == expectedHeader.size || header.contentEquals(expectedHeader)) {
                    SilkCasket.release(AppConf.SilkCasket_Temp, loaderFile, targetDirectory)
                } else {
                    return Pair(false, "error")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return Pair(false, e.toString())
        }

        return Pair(true, "")
    }

    fun remove(targetDirectory: String) {
        FileUtils.deleteDirectory(File(targetDirectory))
    }


    fun loadLoadersFromDirectory(targetDirectory: String): List<EFModLoader>  {

        val directory = File(targetDirectory)
        val loaders = mutableListOf<EFModLoader>()

        if (directory.exists() && directory.isDirectory) {
            for (loaderDir in directory.listFiles { file -> file.isDirectory }!!) {
                if (loaderDir != null && File(loaderDir, "efmodloader.toml").exists()) {
                    var LoaderIcon: ImageBitmap? = null
                    val iconFile = File(loaderDir, "efmodloader.icon")
                    if (iconFile.exists()) {
                        val Icon = iconFile.readBytes()

                        if (Icon.isNotEmpty()) {
                            LoaderIcon = BitmapFactory.decodeByteArray(Icon, 0, Icon.size).asImageBitmap()
                        }
                    }

                    val toml = Toml.parseToTomlTable(File(loaderDir, "efmodloader.toml").reader().readText())

                    val loader = EFModLoader(
                        info = Info(
                            name = toml.getTable("info").getString("name"),
                            author = toml.getTable("info").getString("author"),
                            version = toml.getTable("info").getString("version"),
                            page = false
                        ),
                        github = Github(
                            openSource = true,
                            overview = toml.getTable("github").getString("overview"),
                            url = toml.getTable("github").getString("url")
                        ),
                        platforms = Platforms(
                            windows = PlatformSupport(
                                arm64 = toml.getTable("platform").getTable("windows").getBoolean("arm64"),
                                arm32 = toml.getTable("platform").getTable("windows").getBoolean("arm32"),
                                x86_64 = toml.getTable("platform").getTable("windows").getBoolean("x86_64"),
                                x86 = toml.getTable("platform").getTable("windows").getBoolean("x86")
                            ),
                            android = PlatformSupport(
                                arm64 = toml.getTable("platform").getTable("android").getBoolean("arm64"),
                                arm32 = toml.getTable("platform").getTable("android").getBoolean("arm32"),
                                x86_64 = toml.getTable("platform").getTable("android").getBoolean("x86_64"),
                                x86 = toml.getTable("platform").getTable("android").getBoolean("x86")
                            )
                        ),
                        loader = Loader(
                            libName = toml.getTable("loader").getString("lib_name")
                        ),
                        supportModes = SupportModes(
                            android = SupportModeAndroid(
                                inline = toml.getTable("loader").getTable("support_mode").getTable("android").getBoolean("inline"),
                                external = toml.getTable("loader").getTable("support_mode").getTable("android").getBoolean("external"),
                                root = toml.getTable("loader").getTable("support_mode").getTable("android").getBoolean("root"),
                                share = toml.getTable("loader").getTable("support_mode").getTable("android").getBoolean("share")
                            ),
                            windows = SupportModeWindows(
                                hijack = toml.getTable("loader").getTable("support_mode").getTable("windows").getBoolean("hijack")
                            )
                        ),
                        compatibility = Compatibility(
                            supportedVersions = toml.getTable("compatible").getArray("supported_versions").map { it.toString() },
                            minimumStandards = toml.getTable("compatible").getInteger("minimum_standards").toInt(),
                            highestStandards = toml.getTable("compatible").getInteger("highest_standards").toInt()
                        ),
                        introduces =  Introduction(
                            description = toml.getTable("introduce").getString(I18N.getCurrentLocale())
                        ),
                        icon = LoaderIcon,
                        path = loaderDir.absolutePath,
                        isEnabled = File(loaderDir, "enabled").exists()
                    )

                    loaders.add(loader)
                }
            }
        }

        return loaders
    }

}