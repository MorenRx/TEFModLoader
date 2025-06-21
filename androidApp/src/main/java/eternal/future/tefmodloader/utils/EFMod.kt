package eternal.future.tefmodloader.utils

import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import eternal.future.tefmodloader.config.AppConf
import eternal.future.tefmodloader.config.AppPrefsOld
import eternal.future.tefmodloader.data.EFMod
import eternal.future.tefmodloader.data.Github
import eternal.future.tefmodloader.data.Info
import eternal.future.tefmodloader.data.Introduction
import eternal.future.tefmodloader.data.LoaderSupport
import eternal.future.tefmodloader.data.PlatformSupport
import eternal.future.tefmodloader.data.Platforms
import eternal.future.tefmodloader.manager.I18N
import net.peanuuutz.tomlkt.Toml
import net.peanuuutz.tomlkt.asTomlTable
import net.peanuuutz.tomlkt.getArray
import net.peanuuutz.tomlkt.getBoolean
import net.peanuuutz.tomlkt.getInteger
import net.peanuuutz.tomlkt.getString
import net.peanuuutz.tomlkt.getTable
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object EFMod {

    fun install(modFile: String, targetDirectory: String): Pair<Boolean, String> {
        val expectedHeader = byteArrayOf(
            0x53, 0x69, 0x6C, 0x6B, 0x43, 0x61, 0x73, 0x6B, 0x65, 0x74,
            0x00, 0x03, 0xFE.toByte(), 0x34, 0x01
        )

        try {
            FileInputStream(modFile).use { fis ->
                val header = ByteArray(expectedHeader.size)
                val bytesRead = fis.read(header)

                if (bytesRead == expectedHeader.size && header.contentEquals(expectedHeader)) {
                    SilkCasket.release(AppConf.SilkCasket_Temp, modFile, targetDirectory)
                } else {
                    return Pair(false, "error")
                }
            }
        } catch (e: IOException) {
            return Pair(false, e.toString())
        }
        return Pair(true, "succeed")
    }

    fun update(modFile: String, targetDirectory: String): Pair<Boolean, String> {
        val expectedHeader = byteArrayOf(
            0x53, 0x69, 0x6C, 0x6B, 0x43, 0x61, 0x73, 0x6B, 0x65, 0x74,
            0x00, 0x03, 0xFE.toByte(), 0x34, 0x01
        )

        try {
            FileInputStream(modFile).use { fis ->
                val header = ByteArray(expectedHeader.size)
                val bytesRead = fis.read(header)

                if (bytesRead == expectedHeader.size && header.contentEquals(expectedHeader)) {
                    SilkCasket.release(AppConf.SilkCasket_Temp, modFile, targetDirectory)
                } else {
                    return Pair(false, "error")
                }
            }
        } catch (e: IOException) {
            return Pair(false, e.toString())
        }
        return Pair(true, "succeed")
    }

    fun remove(targetDirectory: String) {
        try {
            FileUtils.deleteDirectory(File(targetDirectory))
        } catch (e: IOException) {
        }
    }

    fun initialize_data(modPath: String, targetDirectory: String) {
        try {
            val targetDir = File(targetDirectory)
            if (!targetDir.exists() && !targetDir.mkdirs()) {
                return
            }

            val mods = loadModsFromDirectory(modPath)
            mods.forEach { mod ->
                val f = File(mod.path)
                File(f, "private").let { privateDir ->
                    if (privateDir.exists()) {
                        FileUtils.moveRecursivelyEfficient(privateDir, File(targetDirectory, "${privateDir.parentFile.name}/private"))
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    fun update_data(externallyPrivate: String, modPath: String) {
        try {
            val mods = loadModsFromDirectory(modPath)
            mods.forEach { mod ->
                val f = File(mod.path)
                File(f, "private").let { privateDir ->
                    File(externallyPrivate, "${privateDir.parentFile.name}/private").let { externalDir ->
                        if (externalDir.exists() && externalDir.isDirectory) {
                            if (!privateDir.exists() && !privateDir.mkdirs()) {
                                return@let
                            }

                            FileUtils.moveRecursivelyEfficient(externalDir, privateDir)
                        }
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    fun initialize(modPath: String, loaderPath: String, targetDirectory: String, Architecture: String? = null) {

        data class Loader(
            val name: String,
            val path: String,
            val libName: String,
            val version: List<String>,
            val supportedStandards: Pair<Int, Int>
        )

        val mods = loadModsFromDirectory(modPath)
        val loaders = EFModLoader.loadLoadersFromDirectory(loaderPath)

        val loadersMap = mutableStateListOf<Loader>()
        val initializeMap = mutableMapOf<Loader, MutableList<String>>()

        var architecture = when (AppPrefsOld.architecture) {
            1 -> "arm64-v8a"
            2 -> "armeabi-v7a"
            3 -> "x64"
            4 -> "x86"
            else -> App.getCurrentArchitecture()
        }

        Architecture?.let {
            architecture = it
        }

        loaders.forEach { loaderInfo ->
            if (loaderInfo.isEnabled) {
                val versions = mutableStateListOf<String>()
                versions.add(loaderInfo.info.version)
                loaderInfo.compatibility.supportedVersions.forEach { vv -> versions.add(vv) }

                val loader = Loader(
                    name = "${loaderInfo.info.name}-${loaderInfo.info.author}",
                    path = loaderInfo.path,
                    libName = loaderInfo.loader.libName,
                    version = versions,
                    supportedStandards = Pair(loaderInfo.compatibility.highestStandards, loaderInfo.compatibility.minimumStandards)
                )
                loadersMap.add(loader)
            }
        }

        mods.forEach { mod ->
            if (mod.isEnabled) {
                if (!mod.Modx) {
                    var matched = false
                    mod.loaders.forEach { loader ->
                        loadersMap.forEach { ll ->
                            if (ll.name == loader.name &&
                                ll.version.toSet().intersect(loader.supportedVersions.toSet()).isNotEmpty() &&
                                mod.standards <= ll.supportedStandards.first &&
                                mod.standards >= ll.supportedStandards.second) {
                                if (!matched) {
                                    if (!initializeMap.containsKey(ll)) {
                                        initializeMap[ll] = mutableListOf()
                                    }
                                    initializeMap[ll]?.add(mod.path)
                                    matched = true
                                }
                            }
                        }
                    }
                } else {
                    val sourceDir = File(mod.path, "lib/android/$architecture")
                    val targetDir = File(targetDirectory, "Modx/${File(mod.path).name}")
                    FileUtils.copyRecursivelyEfficient(sourceDir, targetDir)
                }
            }
        }

        initializeMap.forEach { (loader, modPaths) ->
            val loaderDir = File(loader.path)
            val loaderTargetDir = File(targetDirectory, "EFMod/${loaderDir.name}")
            val loaderLib = "lib${loader.libName}.so"

            FileUtils.copyRecursivelyEfficient(File(loaderDir, "lib/android/$architecture"), loaderTargetDir)

            val originalFile = File(loaderTargetDir, loaderLib)
            val newFile = File(loaderTargetDir, "loader-core")
            if (originalFile.exists()) {
                originalFile.renameTo(newFile)
            }

            modPaths.forEach { modPath ->
                val sourceDir = File(modPath, "lib/android/$architecture")
                val targetDir = File(loaderTargetDir, "Mod/${File(modPath).name}")
                FileUtils.copyRecursivelyEfficient(sourceDir, targetDir)
            }
        }
    }


    fun loadModsFromDirectory(targetDirectory: String): List<EFMod>  {
        val directory = File(targetDirectory)
        val mods = mutableListOf<EFMod>()

        if (directory.exists() && directory.isDirectory) {
            for (modDir in directory.listFiles { file -> file.isDirectory }!!) {
                if (modDir != null && File(modDir, "efmod.toml").exists()) {
                    var ModIcon: ImageBitmap? = null
                    val iconFile = File(modDir, "efmod.icon")
                    if (iconFile.exists()) {
                        val Icon = File(
                                modDir,
                                "efmod.icon"
                            ).readBytes()

                        if (Icon.isNotEmpty()) {

                            ModIcon = BitmapFactory.decodeByteArray(Icon, 0, Icon.size).asImageBitmap()
                        }
                    }

                    val toml = Toml.parseToTomlTable(File(modDir, "efmod.toml").reader().readText())

                    val loaderSupportList = mutableListOf<LoaderSupport>()

                    for (loader_suppor in toml.getArray("loaders")) {
                        loaderSupportList.add(
                            LoaderSupport(
                                name = loader_suppor.asTomlTable().getString("name"),
                                supportedVersions = loader_suppor.asTomlTable().getArray("supported_versions").map { it.toString() }
                            )
                        )
                    }


                    val mod = EFMod(
                        info = Info(
                            name = toml.getTable("info").getString("name"),
                            author = toml.getTable("info").getString("author"),
                            version = toml.getTable("info").getString("version"),
                            page = File(modDir, "page/android.jar").exists()
                        ),
                        github = Github(
                            openSource = toml.getTable("github").getBoolean("open_source"),
                            overview = toml.getTable("github").getString("overview"),
                            url = toml.getTable("github").getString("url")
                        ),
                        platform = Platforms(
                            windows = PlatformSupport(
                                arm64 = toml.getTable("platform").getTable("windows")
                                    .getBoolean("arm64"),
                                arm32 = toml.getTable("platform").getTable("windows")
                                    .getBoolean("arm32"),
                                x86_64 = toml.getTable("platform").getTable("windows")
                                    .getBoolean("x86_64"),
                                x86 = toml.getTable("platform").getTable("windows")
                                    .getBoolean("x86")
                            ),
                            android = PlatformSupport(
                                arm64 = toml.getTable("platform").getTable("android")
                                    .getBoolean("arm64"),
                                arm32 = toml.getTable("platform").getTable("android")
                                    .getBoolean("arm32"),
                                x86_64 = toml.getTable("platform").getTable("android")
                                    .getBoolean("x86_64"),
                                x86 = toml.getTable("platform").getTable("android")
                                    .getBoolean("x86")
                            )
                        ),
                        loaders = loaderSupportList,
                        introduce = Introduction(
                            description = toml.getTable("introduce")
                                .getString(I18N.getCurrentLocale())
                        ),
                        path = modDir.absolutePath,
                        icon = ModIcon,
                        isEnabled = File(modDir, "enabled").exists(),
                        standards = toml.getTable("mod").getInteger("standards").toInt(),
                        Modx = toml.getTable("mod").getBoolean("modx"),
                        pageClass = try {
                            toml.getTable("mod").getString("pageClass")
                        } catch (e: Exception) {
                            "default.PageClass"
                        }
                    )

                    mods.add(mod)
                }
            }
        }
        return mods
    }
}