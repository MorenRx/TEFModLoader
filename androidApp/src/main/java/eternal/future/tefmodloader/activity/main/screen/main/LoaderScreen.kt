package eternal.future.tefmodloader.activity.main.screen.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.InstallDesktop
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.config.AppConf
import eternal.future.tefmodloader.data.EFModLoader
import eternal.future.tefmodloader.data.PlatformSupport
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.utils.Net
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.math.roundToInt

object LoaderScreen {

    var loaders = mutableStateOf(listOf<EFModLoader>())

    @Composable
    fun LoaderScreen() {
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
            val errInstall = I18N.text(R.string.err_install)
            LaunchedEffect(key1 = showInstall) {
                val r = eternal.future.tefmodloader.utils.EFModLoader.install(tempFile.path, File(AppConf.PATH_EFMOD_LOADER, UUID.randomUUID().toString()).path)
                tempFile.delete()
                if (!r.first) {
                    errorMsg = (if (r.second != "error") r.second else errInstall)
                    showError = true
                }
                loaders.value = eternal.future.tefmodloader.utils.EFModLoader.loadLoadersFromDirectory(AppConf.PATH_EFMOD_LOADER)
                showInstall = false
            }
        }

        LoaderScreen_r {
            selectFileLauncher.launch("*/*")
        }

        if (showInstall) {
            AlertDialog(
                onDismissRequest = {  },
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
    fun LoaderScreen_r(
        installOnBack: () -> Unit
    ) {

        loaders.value = eternal.future.tefmodloader.utils.EFModLoader.loadLoadersFromDirectory(AppConf.PATH_EFMOD_LOADER)

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
                    items(loaders.value.size) { index ->
                        val loader = loaders.value[index]
                        LoaderCard(loader)
                    }
                }

                var offsetX by remember { mutableStateOf(0f) }
                var offsetY by remember { mutableStateOf(0f) }

                ExtendedFloatingActionButton(
                    text = { Text(I18N.text(R.string.loader_install)) },
                    icon = { Icon(Icons.Default.InstallDesktop, contentDescription = "Install Loader") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = installOnBack,
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

    @Composable
    fun LoaderCard_Reuse(
        loader: EFModLoader,
        onUpdateModClick: () -> Unit
    ) {

        var expanded by remember { mutableStateOf(false) }
        var showDeleteDialog by remember { mutableStateOf(false) }
        var enabled by remember { mutableStateOf(loader.isEnabled) }
        var isVisible by remember { mutableStateOf(true) }

        if (isVisible) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { expanded = !expanded },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        if (loader.icon != null) {
                            Image(
                                bitmap = loader.icon,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp).clip(
                                    CircleShape
                                )
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.compose_multiplatform),
                                contentDescription = "Default Icon",
                                modifier = Modifier.size(56.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = loader.info.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "by ${loader.info.author}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "v${loader.info.version}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Checkbox(checked = enabled, onCheckedChange = { check ->
                            val file = File(loader.path, "enabled")
                            if (check) file.mkdirs() else file.delete()
                            enabled = check
                        })
                    }

                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                        exit = shrinkVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                    ) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(onClick = { showDeleteDialog = true }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete Loader",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                                IconButton(onClick = { onUpdateModClick() }) {
                                    Icon(Icons.Default.Update, contentDescription = "Update Loader")
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = loader.introduces.description,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            TextButton(onClick = { Net.openUrlInBrowser(loader.github.url) }) {
                                Text("Github")
                            }

                            var isAndroidExpanded by remember { mutableStateOf(false) }
                            ExpandableSection(
                                title = R.string.loader_support_android,
                                expanded = isAndroidExpanded,
                                onExpandChange = { isAndroidExpanded = it }) {
                                PlatformSupport(loader.platforms.android)
                            }

                            var isWindowsExpanded by remember { mutableStateOf(false) }
                            ExpandableSection(
                                title = R.string.loader_support_windows,
                                expanded = isWindowsExpanded,
                                onExpandChange = { isWindowsExpanded = it }) {
                                PlatformSupport(loader.platforms.windows)
                            }
                        }
                    }
                }
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text(I18N.text(R.string.loader_delete)) },
                    text = { Text("${I18N.text(R.string.confirm_delete)} ${loader.info.name}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDeleteDialog = false
                            eternal.future.tefmodloader.utils.EFModLoader.remove(loader.path)
                            isVisible = false
                        }
                        ) {
                            I18N.text(R.string.confirm)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            I18N.text(R.string.cancel)
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun LoaderCard(loader: EFModLoader) {
        val context = LocalContext.current
        var showError by remember { mutableStateOf(false) }
        var errorMsg by remember { mutableStateOf("") }

        var showUpdate by remember { mutableStateOf(false) }
        val tempFile = File(context.externalCacheDir, "update.temp")
        val selectFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { url ->
            url?.let {
                context.contentResolver.openInputStream(url)?.use { inputStream ->
                    FileOutputStream(tempFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                        showUpdate = true
                    }
                }
            }
        }

        if (showUpdate) {
            val errLoaderHeader = I18N.text(R.string.loader_header_error)
            LaunchedEffect(key1 = showUpdate) {
                val r = eternal.future.tefmodloader.utils.EFModLoader.update(tempFile.path, loader.path)
                if (!r.first) {
                    errorMsg = if (r.second != "error") r.second else errLoaderHeader
                    showError = true
                }
                loaders.value = eternal.future.tefmodloader.utils.EFModLoader.loadLoadersFromDirectory(AppConf.PATH_EFMOD_LOADER)
                showUpdate = false
            }
        }

        LoaderCard_Reuse(loader) {
            selectFileLauncher.launch("*/*")
            showUpdate = true
        }

        if (showUpdate) {
            AlertDialog(
                onDismissRequest = {  },
                title = { Text(I18N.text(R.string.updating)) },
                text = {
                    Column {
                        Text("${I18N.text(R.string.loading)} ${loader.info.name}?")
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
                title = { Text(I18N.text(R.string.err_update)) },
                text = {
                    LazyColumn {
                        item {
                            Text(errorMsg)
                        }
                    } },
                confirmButton = {},
                dismissButton = {}
            )
        }
    }

    @Composable
    private fun ExpandableSection(@StringRes title: Int, expanded: Boolean, onExpandChange: (Boolean) -> Unit, content: @Composable () -> Unit) {
        Column(modifier = Modifier.clickable { onExpandChange(!expanded) }.fillMaxWidth().padding(horizontal = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = I18N.text(title), fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f).padding(vertical = 4.dp))
                Icon(imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
            }
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
            ) {
                Box(modifier = Modifier.padding(start = 16.dp)) {
                    content()
                }
            }
        }
    }

    @Composable
    private fun PlatformSupport(platform: PlatformSupport) {
        Column {
            Text("x86_64: ${platform.x86_64}", modifier = Modifier.padding(vertical = 4.dp))
            Text("x86_32: ${platform.x86}", modifier = Modifier.padding(vertical = 4.dp))
            Text("arm64: ${platform.arm64}", modifier = Modifier.padding(vertical = 4.dp))
            Text("arm32: ${platform.arm32}", modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}