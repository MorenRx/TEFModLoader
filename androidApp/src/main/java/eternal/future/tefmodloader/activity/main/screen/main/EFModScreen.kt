package eternal.future.tefmodloader.activity.main.screen.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InstallDesktop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import eternal.future.tefmodloader.data.EFMod
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.config.AppConf
import eternal.future.tefmodloader.widget.main.EFModScreen.EFModCard
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.math.roundToInt

object EFModScreen {

    var mods = mutableStateOf(listOf<EFMod>())

    @Composable
    fun EFModScreen() {
        val context = LocalContext.current
        var showError by remember { mutableStateOf(false) }
        var errorMsg by remember { mutableStateOf("") }

        var showInstall by remember { mutableStateOf(false) }

        val tempFile = File(context.externalCacheDir, "install.temp")

        val selectFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { url ->
            url?.let {
                context.contentResolver.openInputStream(url)?.use { inputStream ->
                    FileOutputStream(tempFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                        showInstall = true
                    }
                }
            }
        }

        if (showInstall) {
            val err = I18N.text(R.string.mod_header_error)
            LaunchedEffect(key1 = showInstall) {
                val r = eternal.future.tefmodloader.utils.EFMod.install(
                    tempFile.path,
                    File(AppConf.PATH_EFMOD, UUID.randomUUID().toString()).path
                )

                tempFile.delete()
                if (!r.first) {
                    errorMsg = if (r.second != "error") r.second else err
                    showError = true
                }
                mods.value = eternal.future.tefmodloader.utils.EFMod.loadModsFromDirectory(AppConf.PATH_EFMOD)
                showInstall = false
            }
        }

        EFModScreen_r {
            selectFileLauncher.launch("*/*")
        }

        if (showInstall) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(I18N.text(R.string.installing)) },
                text = {
                    Column {
                        I18N.text(R.string.loading)
                        CircularProgressIndicator()
                    }
                },
                confirmButton = {},
                dismissButton = {}
            )
        }

        if (showError) {
            AlertDialog(
                onDismissRequest = { showError = false },
                title = { Text(I18N.text(R.string.err_install)) },
                text = {
                    LazyColumn {
                        item {
                            Text(errorMsg)
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {}
            )
        }
    }

    @Composable
    fun EFModScreen_r(
        onBack: () -> Unit
    ) {
        mods.value = eternal.future.tefmodloader.utils.EFMod.loadModsFromDirectory(AppConf.PATH_EFMOD)

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { padding ->

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(mods.value.size) { index ->
                        val mod = mods.value[index]
                        EFModCard(mod)
                    }
                }

                var offsetX by remember { mutableStateOf(0f) }
                var offsetY by remember { mutableStateOf(0f) }

                ExtendedFloatingActionButton(
                    text = { Text(I18N.text(R.string.mod_install)) },
                    icon = { Icon(Icons.Default.InstallDesktop, contentDescription = "Install EFMod") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = onBack,
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