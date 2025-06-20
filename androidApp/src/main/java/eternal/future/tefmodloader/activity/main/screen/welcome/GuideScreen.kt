package eternal.future.tefmodloader.activity.main.screen.welcome

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.AutoFixNormal
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InstallDesktop
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eternal.future.tefmodloader.activity.main.navigation.DefaultScreen
import eternal.future.tefmodloader.activity.main.navigation.NavigationViewModel
import eternal.future.tefmodloader.activity.main.navigation.ScreenRegistry
import eternal.future.tefmodloader.config.AppPrefs
import eternal.future.tefmodloader.utils.EFModLoader
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.config.AppConf
import eternal.future.tefmodloader.config.AppState
import eternal.future.tefmodloader.manager.ThemeManager
import eternal.future.tefmodloader.widget.main.SettingScreen
import eternal.future.tefmodloader.widget.welcome.GuideScreen.AgreementCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.roundToInt


object GuideScreen {

    val viewModel = NavigationViewModel()

    var userPact = false;
    var modLoaderPact = false;


    init {
        listOf(
            DefaultScreen("personalize"),
            DefaultScreen("disposition"),
            DefaultScreen("agreement"),
            DefaultScreen("agreement_loader"),
            DefaultScreen("disposition_2")
        ).forEach {
            ScreenRegistry.register(it)
        }
        viewModel.setInitialScreen("personalize")
    }

    val showNext_disposition = mutableStateOf(false)

    @Composable
    fun disposition() {

        showNext_disposition.value = true

        SettingScreen.ModernCheckBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            title = I18N.string(R.string.guide_auto_patch),
            contentDescription = I18N.string(R.string.guide_auto_patch_desc),
            isChecked = AppState.autoPatch.value,
            onCheckedChange = { check ->
                AppState.autoPatch.value = check
            },
            icon = Icons.Default.AutoFixNormal
        )
    }

    @Composable
    fun Patch() {
        val selectFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { url ->
            url?.let {
                AppState.ApkPath.value = it.toString()
            }
        }
        var showSelector by remember { mutableStateOf(true) }

        val modeList = listOf(
            R.string.launch_mode_external
            //R.string.launch_mode_share,
            //R.string.launch_mode_inline,
            //R.string.launch_mode_root
        )

        LazyColumn {
            item {
                SettingScreen.Selector(
                    title = I18N.string(R.string.setting_launch_mode),
                    defaultSelectorId = AppState.Mode.intValue,
                    modeList.map { I18N.string(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = { select ->
                        AppState.Mode.intValue = select
                        showSelector = AppState.Mode.intValue != 3 && AppState.Mode.intValue != 2
                    }
                )

                if (showSelector) {
                    SettingScreen.ModernCheckBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        title = I18N.string(R.string.setting_override),
                        contentDescription = I18N.string(R.string.setting_override_desc),
                        isChecked = AppState.OverrideVersion.value,
                        onCheckedChange = { select ->
                            AppState.OverrideVersion.value = select
                        }
                    )



                    SettingScreen.ModernCheckBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        title = I18N.string(R.string.setting_bypass),
                        contentDescription = I18N.string(R.string.setting_bypass_desc),
                        isChecked = AppState.isBypass.value,
                        onCheckedChange = { select ->
                            AppState.isBypass.value = select
                        }
                    )

                    SettingScreen.ModernCheckBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        title = I18N.string(R.string.setting_debug),
                        contentDescription = I18N.string(R.string.setting_debug_desc),
                        isChecked = AppState.Debugging.value,
                        onCheckedChange = { select ->
                            AppState.Debugging.value = select
                        }
                    )

                    Text(
                        I18N.string(R.string.setting_custom_apk_desc),
                        modifier = Modifier.padding(10.dp)
                    )

                    SettingScreen.GeneralTextInput(
                        title = I18N.string(R.string.setting_custom_apk),
                        value = AppState.ApkPath.value,
                        onValueChange = {},
                        trailingIcon = {
                            IconButton(onClick = {
                                selectFileLauncher.launch("application/vnd.android.package-archive")
                            }) {
                                Icon(Icons.Default.Folder, contentDescription = "选择文件夹")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun disposition_2(mainViewModel: NavigationViewModel) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Column {
                Patch()
            }
            var offsetX by remember { mutableFloatStateOf(0f) }
            var offsetY by remember { mutableFloatStateOf(0f) }
            val scope = rememberCoroutineScope()
            ExtendedFloatingActionButton(
                text = { I18N.text(R.string.next) },
                icon = { Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "Next") },
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    start(scope, mainViewModel)
                },
                modifier = Modifier
                    .offset {
                        IntOffset(
                            offsetX.roundToInt(),
                            offsetY.roundToInt()
                        )
                    }
                    .align(Alignment.BottomEnd)
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                    .padding(20.dp)
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GuideScreen(mainViewModel: NavigationViewModel) {
        val currentScreenWithAnimation by viewModel.currentScreen.collectAsState()
        Scaffold { innerPadding ->
            Crossfade(
                modifier = Modifier.padding(innerPadding),
                targetState = currentScreenWithAnimation,
                animationSpec = tween(durationMillis = 500)
            ) { state ->
                state.let { (screen, _) ->
                    if (screen != null) {
                        when (screen.id) {
                            "personalize" -> personalize(mainViewModel)
                            "disposition" -> Disposition(
                                { disposition() },
                                mainViewModel
                            )

                            "disposition_2" -> disposition_2(mainViewModel)
                            "agreement" -> agreement()
                            "agreement_loader" -> agreement_loader()
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun personalize(mainViewModel: NavigationViewModel) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = I18N.string(R.string.title_welcome),
                fontSize = 24.sp,
                modifier = Modifier
                    .height(400.dp)
                    .align(Alignment.Center),
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {

                SettingScreen.Selector(
                    title = I18N.text(R.string.setting_language),
                    defaultSelectorId = AppPrefs.language,
                    I18N.optionsLanguage.map { I18N.string(it) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        AppPrefs.language = it
                        mainViewModel.refreshCurrentScreen()
                    }
                )
                
                SettingScreen.SelectorWithIcon(
                    title = I18N.text(R.string.setting_theme),
                    defaultSelectorId = AppPrefs.theme,
                    ThemeManager.optionsTheme.map { I18N.string(it.first) to it.second },
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        AppPrefs.theme = it
                    }
                )

                SettingScreen.Selector(
                    title = I18N.text(R.string.setting_dark_mode),
                    defaultSelectorId = AppPrefs.darkMode,
                    ThemeManager.optionsDarkMode.map { I18N.string(it) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        AppPrefs.darkMode = it
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            viewModel.navigateTo("agreement")
                        }
                        .padding(horizontal = 22.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = userPact,
                        onCheckedChange = null
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = I18N.string(R.string.agreement_user_title),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            viewModel.navigateTo("agreement_loader")
                        }
                        .padding(horizontal = 22.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = modLoaderPact,
                        onCheckedChange = null
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = I18N.string(R.string.agreement_loader_title),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))

            }

            if (userPact && modLoaderPact) {
                ExtendedFloatingActionButton(
                    text = { I18N.text(R.string.next) },
                    icon = { Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "Next") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { viewModel.navigateTo("disposition") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(30.dp)
                )
            }
        }
    }

    @Composable
    private fun agreement() {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AgreementCard(
                    title = I18N.string(R.string.agreement_user_title),
                    agreementText = I18N.string(R.string.agreement_user_desc),
                    checkBoxTitle = I18N.string(R.string.agreement_user_consent),
                    onCheckBoxChange = { check ->
                        userPact = true
                        viewModel.navigateTo("personalize")
                    }
                )
            }
        }
    }

    @Composable
    fun agreement_loader() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AgreementCard(
                    title = I18N.string(R.string.agreement_loader_title),
                    agreementText = I18N.string(R.string.agreement_loader_desc),
                    checkBoxTitle = I18N.string(R.string.agreement_loader_consent),
                    onCheckBoxChange = { check ->
                        modLoaderPact = true
                        viewModel.navigateTo("personalize")
                    }
                )
            }
        }
    }

    fun start(scope: CoroutineScope, mainViewModel: NavigationViewModel) {
        scope.launch {
            if (AppState.defaultLoader.value) {
                try {
                    withContext(Dispatchers.IO) {
                        val tempFile = File.createTempFile("TEFModLoader", ".efml")
                        val target = File(AppConf.PATH_EFMOD_LOADER, "default")

                        FileOutputStream(tempFile).use { fileOutputStream ->
                            javaClass.classLoader?.getResourceAsStream("tefmodloader.efml")?.copyTo(fileOutputStream)
                        }

                        EFModLoader.install(tempFile.path, target.path)
                        File(target, "enabled").mkdirs()
                        tempFile.delete()
                    }
                } catch (e: IOException) {
                }
            }

            // 导航操作回到主线程
            withContext(Dispatchers.Main) {
                mainViewModel.setInitialScreen("main")
                mainViewModel.navigateTo("main")
            }
        }
    }

    @Composable
    fun Disposition(UI: @Composable () -> Unit, mainViewModel: NavigationViewModel) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                UI()

                SettingScreen.ModernCheckBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    title = I18N.string(R.string.guide_default_loader),
                    contentDescription = I18N.string(R.string.guide_default_loader_desc),
                    isChecked = AppState.defaultLoader.value,
                    onCheckedChange = { check ->
                        AppState.defaultLoader.value = check
                    },
                    icon = Icons.Default.InstallDesktop
                )

                val optionsLogTitle = listOf(
                    I18N.string(R.string.disable),
                    I18N.string(R.string.unlimited),
                    "512 kb",
                    "1024 kb",
                    "2048 kb",
                    "4096 kb",
                    "8192 kb"
                )

                val optionsLog = listOf(
                    0,
                    -1,
                    512 * 1024,
                    1024 * 1024,
                    2048 * 1024,
                    4096 * 1024,
                    8192 * 1024
                )

                SettingScreen.Selector(
                    title = I18N.text(R.string.setting_log_cache),
                    defaultSelectorId = AppPrefs.logCacheMaximum,
                    optionsLogTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = {
                        AppPrefs.logCacheMaximum = optionsLog[it]
                    }
                )
            }

            if (showNext_disposition.value) {
                val scope = rememberCoroutineScope()
                ExtendedFloatingActionButton(
                    text = { I18N.text(R.string.next) },
                    icon = { Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "Next") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        if (!AppState.autoPatch.value) {
                            viewModel.navigateTo("disposition_2")
                        } else {
                            start(scope, mainViewModel)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp)
                )
            }
        }
    }
}