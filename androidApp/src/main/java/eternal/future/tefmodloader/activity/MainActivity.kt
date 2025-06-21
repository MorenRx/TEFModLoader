package eternal.future.tefmodloader.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import eternal.future.tefmodloader.activity.main.easteregg.GravityAffectedContent
import eternal.future.tefmodloader.activity.main.easteregg.Screen
import eternal.future.tefmodloader.activity.main.navigation.BackMode
import eternal.future.tefmodloader.activity.main.navigation.DefaultScreen
import eternal.future.tefmodloader.activity.main.navigation.NavigationViewModel
import eternal.future.tefmodloader.activity.main.navigation.ScreenRegistry
import eternal.future.tefmodloader.activity.main.screen.HelpScreen
import eternal.future.tefmodloader.activity.main.screen.ModPageScreen
import eternal.future.tefmodloader.activity.main.screen.SettingScreen
import eternal.future.tefmodloader.activity.main.screen.TerminalScreen
import eternal.future.tefmodloader.activity.main.screen.about.AboutScreen
import eternal.future.tefmodloader.activity.main.screen.about.LicenseScreen
import eternal.future.tefmodloader.activity.main.screen.about.ThanksScreen
import eternal.future.tefmodloader.activity.main.screen.main.MainScreen
import eternal.future.tefmodloader.config.AppConf
import eternal.future.tefmodloader.config.AppPrefsOld
import eternal.future.tefmodloader.config.AppState
import eternal.future.tefmodloader.manager.ThemeManager.ComposeTheme
import eternal.future.tefmodloader.utils.EFMod
import java.io.File

class MainActivity : ComponentActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: Activity

        fun getContext(): Activity {
            return instance
        }

        fun exit() {
            instance.finishAffinity()
        }

        val mainViewModel = NavigationViewModel()
    }


    private var isBottomScreen: Boolean = false
    private var lastBackPressedTime: Long = 0
    private val backPressInterval: Long = 1000L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (AppPrefsOld.isFirstLaunch) {
            startActivity(Intent(this, GuideActivity::class.java))
            finishAffinity()
            return
        }

        checkPermission()
        instance = this
        enableEdgeToEdge()

        setContent {
            ComposeTheme {
                initializeScreens(mainViewModel)
                NavigationHost(mainViewModel)
            }
        }

        setupBackPressedHandler()
    }

    private fun setupBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!isBottomScreen) {
                    mainViewModel.navigateBack(BackMode.ONE_BY_ONE)
                    return
                }

                val currentTime = System.currentTimeMillis()
                if (currentTime - lastBackPressedTime < backPressInterval) {
                    finishAffinity()
                } else {
                    lastBackPressedTime = currentTime
                    Toast.makeText(this@MainActivity, "再按一次退出", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun NavigationHost(viewModel: NavigationViewModel) {
        ComposeTheme(darkMode = AppPrefsOld.darkMode, themeId = AppPrefsOld.theme) {
            val currentScreenWithAnimation by viewModel.currentScreen.collectAsState()
            val content = @Composable {
                Scaffold {
                    Crossfade(
                        targetState = currentScreenWithAnimation,
                        animationSpec = tween(durationMillis = 500)
                    ) { state ->
                        state.let { (screen, _) ->
                            if (screen != null) {
                                isBottomScreen = false
                                when (screen.id) {
                                    "welcome" -> WelcomeScreen(viewModel)
                                    "main" -> {
                                        isBottomScreen = true
                                        MainScreen.MainScreen(viewModel)
                                    }

                                    "terminal" -> TerminalScreen.TerminalScreen(viewModel)
                                    "about" -> AboutScreen(viewModel)
                                    "help" -> HelpScreen.HelpScreen(viewModel)
                                    "license" -> LicenseScreen.LicenseScreen(viewModel)
                                    "thanks" -> ThanksScreen.ThanksScreen(viewModel)
                                    "settings" -> SettingScreen.SettingScreen(viewModel)
                                    "modpage" -> ModPageScreen.ModPageScreen(viewModel)
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }

            if (AppState.screen_physical.value) {
                GravityAffectedContent(
                    gravity = 100f,
                    mass = 100f,
                    elasticity = 5f,
                    containerHeight = 1000f,
                    containerWidth = 300f,
                    velocityDecay = 0.25f,
                    modifier = Modifier.Companion.fillMaxWidth()
                ) {
                    if (AppState.screen_revolve.value) {
                        Screen.ClockwiseRotatingContent(-1) { content() }
                    } else content()
                }
            } else if (AppState.screen_rollback.value) Screen.Rotation { content() } else content()
        }
    }

    @Composable
    fun WelcomeScreen(viewModel: NavigationViewModel) {
        val isFirst = AppPrefsOld.isFirstLaunch

        LaunchedEffect(Unit) {
            if (AppPrefsOld.isExternalMode) {
                EFMod.update_data(
                    File(Environment.getExternalStorageDirectory(), "Documents/TEFModLoader/Data").path,
                    AppConf.PATH_EFMOD
                )
                AppPrefsOld.isExternalMode = false
            }
        }

        viewModel.removeCurrentScreen()
        viewModel.setInitialScreen("main")
    }

    fun initializeScreens(viewModel: NavigationViewModel) {
        listOf(
            DefaultScreen("welcome"),
            DefaultScreen("main"),
            DefaultScreen("about"),
            DefaultScreen("help"),
            DefaultScreen("license"),
            DefaultScreen("thanks"),
            DefaultScreen("settings"),
            DefaultScreen("terminal"),
            DefaultScreen("modpage")
        ).forEach {
            ScreenRegistry.register(it)
        }
        viewModel.setInitialScreen("welcome")
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 1001)
            }

            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = String.format("package:%s", applicationContext.packageName).toUri()
                startActivityForResult(intent, 1001)
            }
        } else {
            val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
            val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

            if (ContextCompat.checkSelfPermission(this, readPermission) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, writePermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(readPermission, writePermission), 1001)
            }
        }
    }
}