package eternal.future.tefmodloader.activity.main.screen

import android.net.Uri
import android.os.Process
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eternal.future.tefmodloader.utils.App
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.activity.main.navigation.BackMode
import eternal.future.tefmodloader.activity.main.navigation.NavigationViewModel
import eternal.future.tefmodloader.config.AppPrefs
import eternal.future.tefmodloader.config.AppState
import eternal.future.tefmodloader.manager.ThemeManager
import eternal.future.tefmodloader.widget.AppTopBar
import eternal.future.tefmodloader.widget.main.SettingScreen.ActionButton
import eternal.future.tefmodloader.widget.main.SettingScreen.Selector
import eternal.future.tefmodloader.widget.main.SettingScreen.SelectorWithIcon
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SettingScreen {
    private fun captureLogcatAndWriteTo(outputStream: OutputStream) {
        val tempFile = createTempFile("logcat_", ".log").apply {
            deleteOnExit()
        }

        try {
            val pid = Process.myPid()
            val command = arrayOf(
                "logcat",
                "--pid=$pid",
                "-v", "threadtime",
                "-b", "all",
                "-d"
            )

            Runtime.getRuntime().exec(command).run {
                tempFile.outputStream().buffered().use { fileStream ->
                    inputStream.copyTo(fileStream)
                }

                errorStream.bufferedReader().use { errorReader ->
                    errorReader.forEachLine { line ->
                        Log.e("LogcatError", line)
                    }
                }
            }

            tempFile.inputStream().buffered().use { fileInput ->
                fileInput.copyTo(outputStream)
            }

        } catch (e: Exception) {
            Log.e("LogcatCapture", "Failed to capture logcat", e)
            throw e
        } finally {
            outputStream.flush()
            outputStream.close()
            tempFile.delete()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingScreen(mainViewModel: NavigationViewModel) {
        val context = LocalContext.current
        val exportFileLauncher = rememberLauncherForActivityResult(CreateDocument("*/*")) { uri: Uri? ->
            uri?.let {
                context.contentResolver.openOutputStream(it).use { outputStream ->
                    if (outputStream != null) {
                        captureLogcatAndWriteTo(outputStream)
                    }
                }
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val menuItems = mapOf(I18N.text(R.string.exit) to Pair(Icons.AutoMirrored.Filled.ExitToApp) {
                    App.exit()
                })
                AppTopBar(
                    title = I18N.text(R.string.exit),
                    showMenu = true,
                    menuItems = menuItems,
                    showBackButton = true,
                    onBackClick = {
                        mainViewModel.navigateBack(BackMode.TO_DEFAULT)
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding
            ) {
                item {

                    val advancedList = listOf(
                        I18N.string(R.string.follow_system),
                        "arm64",
                        "arm32",
                        "x64",
                        "x86"
                    )

                    Text(I18N.string(R.string.setting_title_advanced), modifier = Modifier.padding(4.dp))

                    Selector(
                        title = I18N.text(R.string.setting_architecture),
                        defaultSelectorId = AppState.architecture.value,
                        selectorList = advancedList,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = {
                            configuration.setInt("architecture", it)
                            State.architecture.value = it
                        }
                    )



                    ActionButton(
                        icon = Icons.Default.Save,
                        title = setting.getString("export_logs"),
                        description = setting.getString("export_logs_content"),
                        onClick = {
                            val formatter = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
                            val fileName = "runtime-jvm-${formatter.format(Date())}.log"
                            exportFileLauncher.launch(fileName)
                        }
                    )

                }

                item {
                    Text(I18N.string(R.string.setting_title_general), modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp))

                    Selector(
                        title = I18N.text(R.string.setting_language),
                        defaultSelectorId = AppPrefs.language,
                        I18N.optionsLanguage.map { I18N.string(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = {
                            AppPrefs.language = it
                            I18N.setLocale(I18N.allLanguages.get(it).second)
                            mainViewModel.refreshCurrentScreen()
                        }
                    )

                    SelectorWithIcon(
                        title = I18N.text(R.string.setting_theme),
                        defaultSelectorId = AppPrefs.theme,
                        ThemeManager.optionsTheme.map { I18N.string(it.first) to it.second },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { AppPrefs.theme = it }
                    )

                    Selector(
                        title = I18N.text(R.string.setting_dark_mode),
                        defaultSelectorId = AppPrefs.darkMode,
                        ThemeManager.optionsDarkMode.map { I18N.string(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        onClick = { AppPrefs.darkMode = it }
                    )
                }
            }
        }
    }
}