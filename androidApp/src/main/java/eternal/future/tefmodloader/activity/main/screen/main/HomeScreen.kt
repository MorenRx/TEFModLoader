package eternal.future.tefmodloader.activity.main.screen.main

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import eternal.future.Loader
import eternal.future.tefmodloader.activity.MainActivity
import eternal.future.tefmodloader.config.AppPrefsOld
import eternal.future.tefmodloader.utils.EFMod
import eternal.future.tefmodloader.utils.FileUtils
import eternal.future.tefmodloader.config.AppState
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.config.AppConf
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.utils.Apk
import eternal.future.tefmodloader.widget.main.HomeScreen.stateCard
import eternal.future.tefmodloader.widget.main.HomeScreen.updateLogCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.roundToInt

object HomeScreen {
    val hasUnityDataFile = run {
        runCatching { Class.forName("com.unity3d.player.UnityPlayerActivity") }.isSuccess
    }

    @Composable
    fun HomeScreen() {
        val context = LocalContext.current

        val snackbarHostState = SnackbarHostState()
        val scope: CoroutineScope = rememberCoroutineScope()

        var showExportDialog by remember { mutableStateOf(false) }
        var showPatchDialog by remember { mutableStateOf(false) }
        var showPatchingDialog by remember { mutableStateOf(false) }
        var showLaunchDialog by remember { mutableStateOf(false) }

        val exportFileLauncher = rememberLauncherForActivityResult(CreateDocument("application/vnd.android.package-archive")) { uri: Uri? ->
            uri?.let { documentUri ->
                try {
                    val apkFile = File(context.getExternalFilesDir(null), "patch/Game.apk")

                    context.contentResolver.openOutputStream(documentUri)?.use { outputStream ->
                        FileInputStream(apkFile).use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    apkFile.delete()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        if (AppPrefsOld.isFirstLaunch) {

            var showPatchedDialog by remember { mutableStateOf(true) }

            if (showPatchedDialog) {
                AlertDialog(
                    onDismissRequest = { /* 不允许关闭对话框防止误触 */ },
                    title = { Text(I18N.text(R.string.temp_patched)) },
                    text = {
                        Column {
                            Text(I18N.text(R.string.temp_patched_content))
                        }
                    },

                    confirmButton = {
                        TextButton(onClick = {
                            showPatchingDialog = true
                            AppPrefsOld.isFirstLaunch = false
                            showPatchedDialog = false
                            AppState.autoPatch.value = false
                        }) {
                            Text(I18N.text(R.string.confirm))
                        }
                    },

                    dismissButton = {
                        TextButton(onClick = {
                            AppPrefsOld.isFirstLaunch
                            showPatchedDialog = false
                            AppState.autoPatch.value = false
                        }) {
                            Text(I18N.text(R.string.cancel))
                        }
                    }
                )
            }
        }

        if (showExportDialog) {
            AlertDialog(
                onDismissRequest = { showExportDialog = false },
                title = { Text(I18N.text(R.string.export_file)) },
                text = {
                    Column {
                        Text(I18N.text(R.string.temp_export_the_file_content))
                    } },
                confirmButton = {
                    TextButton(onClick = {
                        exportFileLauncher.launch("patch.APK")
                        showExportDialog = false
                    }) {
                        Text(I18N.text(R.string.export))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        File(context.getExternalFilesDir(null), "patch/Game.apk").delete()
                        showExportDialog = false
                    }) {
                        Text( I18N.text(R.string.temp_cancel_and_delete_the_file))
                    }
                }
            )
        }

        if (showPatchDialog) {
            AlertDialog(
                onDismissRequest = { showPatchDialog = false },
                title = { Text(I18N.text(R.string.patch_game)) },
                text = {
                    //GuideScreen.Patch()
                },
                confirmButton = {
                    TextButton(onClick = {
                        AppState.autoPatch.value = false
                        showPatchDialog = false
                        showPatchingDialog = true
                    }) {
                        Text(I18N.text(R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        AppState.Mode.intValue = 0
                        AppState.Debugging.value = false
                        AppState.OverrideVersion.value = false
                        AppState.gamePack.value = false
                        AppState.ApkPath.value = ""
                        AppState.autoPatch.value = false
                        showPatchDialog = false
                    }) {
                        Text(I18N.text(R.string.temp_cancel_and_clear_the_configuration))
                    }
                }
            )
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(snackbarData = data)
                }
            },
        ) { a -> a


            if (showPatchingDialog) {
                AlertDialog(
                    onDismissRequest = { /* 不允许关闭对话框直到修补完成 */ },
                    title = { Text(I18N.text(R.string.patch_game)) },
                    text = {
                        Column {
                            Text(I18N.text(R.string.loading))
                            CircularProgressIndicator()
                        }
                    },
                    confirmButton = {}
                )

                val errPatch = I18N.text(R.string.err_patch)
                LaunchedEffect(showPatchingDialog) {
                    if (showPatchingDialog) {
                        withContext(Dispatchers.IO) {
                            val target = File(context.getExternalFilesDir(null), "patch/game.apk")

                            if (AppState.autoPatch.value) {
                                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                                    AppState.Mode.intValue = 1
                                }
                                Apk.extractWithPackageName("com.and.games505.TerrariaPaid", target.path)
                            }

                            if (AppState.ApkPath.value == "") {
                                Apk.extractWithPackageName(
                                    packageName = "com.and.games505.TerrariaPaid",
                                    targetPath = target.path
                                )
                            } else {
                                target.parentFile?.mkdirs()
                                Apk.copyApk(
                                    AppState.ApkPath.value,
                                    target.path
                                )
                            }

                            if (!target.exists()) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(errPatch)
                                }
                            } else {

                                Apk.patch(
                                    apkPath = File(
                                        context.getExternalFilesDir(null),
                                        "patch/game.apk"
                                    ).path,
                                    outPath = File(
                                        context.getExternalFilesDir(null),
                                        "patch/game.apk"
                                    ).path,
                                    mode = AppState.Mode.intValue,
                                    bypass = AppState.isBypass.value,
                                    debug = AppState.Debugging.value,
                                    overrideVersion = AppState.OverrideVersion.value,
                                )
                            }

                            showPatchingDialog = false
                        }
                    }
                }
            }

            var isLoading by remember { mutableStateOf(false) }
            var initializationError by remember { mutableStateOf<String?>(null) }
            val coroutineScope = rememberCoroutineScope()

            if (showLaunchDialog) {

                AlertDialog(
                    onDismissRequest = {
                        if (!isLoading) {
                            showLaunchDialog = false
                        }
                    },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 8.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                            Text(I18N.text(R.string.temp_launch_the_menu), style = MaterialTheme.typography.headlineSmall)
                        }
                    },
                    text = {
                        Column {
                            initializationError?.let { error ->
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            val errInit = I18N.text(R.string.err_init)
                            LazyColumn {
                                val game = Apk.getPackageNamesWithMetadata("TEFModLoader")

                                if (hasUnityDataFile) {
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable(enabled = !isLoading) {
                                                    coroutineScope.launch {
                                                        try {
                                                            isLoading = true
                                                            initializationError = null

                                                            withContext(Dispatchers.IO) {

                                                                File(
                                                                    context.filesDir,
                                                                    "TEFModLoader"
                                                                ).let {
                                                                    if (it.exists()) {
                                                                        FileUtils.deleteDirectory(it)
                                                                    }
                                                                }

                                                                EFMod.initialize(
                                                                    AppConf.PATH_EFMOD,
                                                                    AppConf.PATH_EFMOD_LOADER,
                                                                    File(
                                                                        context.filesDir,
                                                                        "TEFModLoader"
                                                                    ).path,
                                                                    "arm64-v8a"
                                                                )

                                                                eternal.future.State.Mode =
                                                                    1
                                                                eternal.future.State.Bypass =
                                                                    false
                                                                eternal.future.State.EFMod_c =
                                                                    AppConf.PATH_EFMOD;

                                                                eternal.future.State.Modx =
                                                                    File(
                                                                        context.filesDir,
                                                                        "TEFModLoader/Modx"
                                                                    )

                                                                eternal.future.State.EFMod =
                                                                    File(
                                                                        context.filesDir,
                                                                        "TEFModLoader/EFMod"
                                                                    )

                                                                eternal.future.State.gameActivity =
                                                                    Class.forName("com.unity3d.player.UnityPlayerActivity")

                                                                val gameActivity =
                                                                    Intent(
                                                                        context,
                                                                        eternal.future.State.gameActivity
                                                                    ).apply {
                                                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                                    }

                                                                context
                                                                    .startActivity(
                                                                        gameActivity
                                                                    )

                                                                Loader.initialize()
                                                            }
                                                        } catch (e: Exception) {
                                                            val errorMsg = "${errInit}: ${e.localizedMessage}"
                                                            initializationError = errorMsg
                                                            isLoading = false
                                                        }
                                                    }
                                                }
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                painter = painterResource(R.mipmap.ic_launcher),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .padding(end = 16.dp)
                                            )
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = context.packageName,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = "${I18N.text(R.string.mode)}: ${I18N.text(R.string.launch_mode_inline)}",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = "1.4.4.9.6",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                    }
                                }

                                item {
                                    game.forEach { (packageName, mode) ->
                                        val icon: Drawable? = remember(packageName) {
                                            try {
                                                context.packageManager.getApplicationIcon(packageName)
                                            } catch (e: PackageManager.NameNotFoundException) {
                                                null
                                            }
                                        }

                                        val unknownVersion = I18N.text(R.string.version_unknown)
                                        val versionInfo = remember(packageName) {
                                            try {
                                                context.packageManager.getPackageInfo(
                                                    packageName,
                                                    PackageManager.GET_META_DATA
                                                ).versionName ?: unknownVersion
                                            } catch (e: Exception) {
                                                unknownVersion
                                            }
                                        }

                                        val modeString = when (mode) {
                                            0 -> I18N.text(R.string.launch_mode_external)
                                            else -> I18N.text(R.string.launch_mode_inline)
                                        }

                                        if (mode != 1 || hasUnityDataFile) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable(enabled = !isLoading) {
                                                        coroutineScope.launch {
                                                            try {
                                                                isLoading = true
                                                                initializationError = null

                                                                withContext(Dispatchers.IO) {
                                                                    when (mode) {
                                                                        0 -> {
                                                                            val gameAbis =
                                                                                Apk.getSupportedAbi(
                                                                                    packageName
                                                                                )

                                                                            EFMod.initialize(
                                                                                AppConf.PATH_EFMOD,
                                                                                AppConf.PATH_EFMOD_LOADER,
                                                                                File(
                                                                                    Environment.getExternalStorageDirectory(),
                                                                                    "Documents/TEFModLoader"
                                                                                ).path,
                                                                                if (AppPrefsOld.architecture == 0) gameAbis else null
                                                                            )

                                                                            EFMod.initialize_data(
                                                                                AppConf.PATH_EFMOD,
                                                                                File(
                                                                                    Environment.getExternalStorageDirectory(),
                                                                                    "Documents/TEFModLoader/Data"
                                                                                ).path
                                                                            )

                                                                            AppPrefsOld.isExternalMode = true

                                                                            Apk.launchAppByPackageName(
                                                                                packageName
                                                                            )
                                                                            MainActivity.getContext().finishAffinity()
                                                                        }
                                                                    }
                                                                }
                                                            } catch (e: Exception) {
                                                                val errorMsg = "${errInit}: ${e.localizedMessage}"
                                                                initializationError = errorMsg
                                                                isLoading = false
                                                            }
                                                        }
                                                    }
                                                    .padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                icon?.let {
                                                    Image(
                                                        bitmap = it.toBitmap().asImageBitmap(),
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .size(60.dp)
                                                            .padding(end = 16.dp)
                                                    )
                                                }

                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = packageName,
                                                        style = MaterialTheme.typography.titleMedium,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    Text(
                                                        text = "${I18N.text(R.string.mode)}: $modeString",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                    Text(
                                                        text = "${I18N.text(R.string.version)} $versionInfo",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = { },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                if (!isLoading) {
                                    showLaunchDialog = false
                                }
                            },
                            enabled = !isLoading
                        ) {
                            I18N.text(R.string.cancel)
                        }
                    }
                )
            }

            Box(
                contentAlignment = Alignment.TopStart
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        stateCard(
                            title = I18N.text(R.string.temp_running),
                            description = I18N.text(R.string.temp_running_content),
                            isActive = true,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                if (File(context.getExternalFilesDir(null), "patch/Game.apk").exists()) {
                                    showExportDialog = true
                                } else {
                                    showPatchDialog = true
                                }
                            }
                        )
                    }

                    item {

                        val updateLogs = listOf(
                            I18N.text(R.string.update_log_100000_title) to I18N.text(R.string.update_log_100000_desc),
                            I18N.text(R.string.update_log_100400_title) to I18N.text(R.string.update_log_100400_desc)
                        )

                        updateLogCard(
                            title = I18N.text(R.string.update_log),
                            confirmButton = I18N.text(R.string.close),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            data = updateLogs,
                            onClick = {}
                        )
                    }
                }

                var offsetX by remember { mutableFloatStateOf(0f) }
                var offsetY by remember { mutableFloatStateOf(0f) }

                ExtendedFloatingActionButton(
                    icon = { Icon(Icons.Default.VideogameAsset, contentDescription = "Launch the game") },
                    text = { Text(I18N.text(R.string.launch_game)) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        if (Apk.doesAnyAppContainMetadata("TEFModLoader") || hasUnityDataFile) {
                            showLaunchDialog = true
                        } else {
                            if (File(context.getExternalFilesDir(null), "patch/game.apk").exists()) showExportDialog = true
                            else showPatchDialog = true
                        }
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
    }
}
