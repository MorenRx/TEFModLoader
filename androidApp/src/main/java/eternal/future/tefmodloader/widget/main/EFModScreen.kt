package eternal.future.tefmodloader.widget.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eternal.future.tefmodloader.data.EFMod
import eternal.future.tefmodloader.data.LoaderSupport
import eternal.future.tefmodloader.data.PlatformSupport
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.activity.MainActivity
import eternal.future.tefmodloader.activity.main.screen.main.EFModScreen.mods
import eternal.future.tefmodloader.config.AppConf
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.utils.Net
import java.io.File
import java.io.FileOutputStream


object EFModScreen {

    @Composable
    fun EFModCard_o(mod: EFMod) {
        val content = LocalContext.current
        var showError by remember { mutableStateOf(false) }
        var errorMsg by remember { mutableStateOf("") }

        var showUpdate by remember { mutableStateOf(false) }
        val tempFile = File(content.externalCacheDir, "update.temp")
        val selectFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { url ->
            url?.let {
                content.contentResolver.openInputStream(url)?.use { inputStream ->
                    FileOutputStream(tempFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                        showUpdate = true
                    }
                }
            }
        }

        if (showUpdate) {
            LaunchedEffect(key1 = showUpdate) {
                val r = eternal.future.tefmodloader.utils.EFMod.update(tempFile.path, mod.path)
                mods.value = eternal.future.tefmodloader.utils.EFMod.loadModsFromDirectory(AppConf.PATH_EFMOD)
                if (!r.first) {
                    errorMsg = if (r.second != "error") r.second else I18N.string(R.string.loader_header_error)
                    showError = true
                }
                showUpdate = false
            }
        }

        val modAPI = mapOf(
            "private-path" to File(mod.path, "private").absolutePath,
            "data-path" to mod.path
        )

        EFModCard_Reuse(
            mod,
            onUpdateModClick = {
                selectFileLauncher.launch("*/*")
                showUpdate = true
            },
            onModPageClick = {
                MainActivity.mainViewModel.navigateTo(
                    "modpage", mapOf(
                        "title" to mod.info.name,
                        "page-class" to mod.pageClass,
                        "page-path" to File(mod.path, "page/android.jar").absolutePath,
                        "page-extraData" to modAPI
                    )
                )
            }
        )

        if (showUpdate) {
            AlertDialog(
                onDismissRequest = { },
                title = { I18N.text(R.string.updating) },
                text = {
                    Column {
                        Text("${I18N.text(R.string.loading)} ${mod.info.name}?")
                    }
                },
                confirmButton = {},
                dismissButton = {}
            )
        }

        if (showError) {
            AlertDialog(
                onDismissRequest = { showError = false },
                title = { I18N.text(R.string.update_error) },
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
    fun EFModCard_Reuse(
        mod: EFMod,
        onUpdateModClick: () -> Unit,
        onModPageClick: () -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }
        var showDeleteDialog by remember { mutableStateOf(false) }
        var showInfoDialog by remember { mutableStateOf(false) }
        var enabled by remember { mutableStateOf(mod.isEnabled) }
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
                        if (mod.icon != null) {
                            Image(
                                bitmap = mod.icon!!,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.compose_multiplatform),
                                contentDescription = "Default Icon",
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = mod.info.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "by ${mod.info.author}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "v${mod.info.version}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = enabled,
                            onCheckedChange = { check ->
                                val file = File(mod.path, "enabled")
                                if (check) file.mkdirs() else file.delete()
                                enabled = check
                            }
                        )
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
                                if (mod.info.page) {
                                    IconButton(onClick = onModPageClick) {
                                        Icon(
                                            Icons.Default.Animation,
                                            contentDescription = "Animation Mod"
                                        )
                                    }
                                }
                                IconButton(onClick = { showDeleteDialog = true }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete Mod",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                                IconButton(onClick = onUpdateModClick) {
                                    Icon(Icons.Default.Update, contentDescription = "Update Mod")
                                }
                                IconButton(onClick = { showInfoDialog = true }) {
                                    Icon(Icons.Default.Info, contentDescription = "Details")
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = mod.introduce.description,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { I18N.text(R.string.mod_delete) },
                    text = { Text("${I18N.text(R.string.confirm_delete)} ${mod.info.name}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDeleteDialog = false
                            eternal.future.tefmodloader.utils.EFMod.remove(mod.path)
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

            if (showInfoDialog) {
                AlertDialog(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onDismissRequest = { showInfoDialog = false },
                    title = { Text(text = "${I18N.text(R.string.details)} - ${mod.info.name}") },
                    text = {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                            TextButton(onClick = { Net.openUrlInBrowser(mod.github.overview) }) {
                                Text(mod.info.author)
                            }

                            if (mod.github.openSource) {
                                TextButton(onClick = { Net.openUrlInBrowser(mod.github.url) }) {
                                    Text(
                                        "Github"
                                    )
                                }
                            }

                            var isIntroExpanded by remember { mutableStateOf(false) }
                            ExpandableSection(
                                title = "${I18N.text(R.string.version)}: ${mod.info.version} | ${I18N.text(R.string.desc)}",
                                expanded = isIntroExpanded,
                                onExpandChange = { isIntroExpanded = it }) {
                                Text(
                                    mod.introduce.description,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            var isLoaderExpanded by remember { mutableStateOf(false) }
                            ExpandableSection(
                                title = I18N.string(R.string.loader_support),
                                expanded = isLoaderExpanded,
                                onExpandChange = { isLoaderExpanded = it }) {
                                SupportLoader(mod.loaders)
                            }

                            var isWindowsExpanded by remember { mutableStateOf(false) }
                            ExpandableSection(
                                title = I18N.string(R.string.loader_support_windows),
                                expanded = isWindowsExpanded,
                                onExpandChange = { isWindowsExpanded = it }) {
                                PlatformSupport(mod.platform.windows)
                            }

                            var isAndroidExpanded by remember { mutableStateOf(false) }
                            ExpandableSection(
                                title = I18N.string(R.string.loader_support_android),
                                expanded = isAndroidExpanded,
                                onExpandChange = { isAndroidExpanded = it }) {
                                PlatformSupport(mod.platform.android)
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showInfoDialog = false }) {
                            I18N.text(R.string.close)
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun EFModCard(mod: EFMod) {
        EFModCard_o(mod)
    }

    @Composable
    private fun SupportLoader(loaders: List<LoaderSupport>) {
        Column {
            for (loader in loaders) {
                var isExpandable by remember { mutableStateOf(false) }
                ExpandableSection(
                    title = loader.name,
                    expanded = isExpandable,
                    onExpandChange = { isExpandable = it },
                    content = {
                        Text(loader.supportedVersions.toString(), modifier = Modifier.padding(4.dp))
                    })
            }
        }
    }

    @Composable
    private fun ExpandableSection(
        title: String,
        expanded: Boolean,
        onExpandChange: (Boolean) -> Unit,
        content: @Composable () -> Unit
    ) {
        Column(
            modifier = Modifier
                .clickable { onExpandChange(!expanded) }
                .fillMaxWidth()
                .padding(horizontal = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
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
            Text("x64: ${platform.x86_64}", modifier = Modifier.padding(vertical = 4.dp))
            Text("x86: ${platform.x86}", modifier = Modifier.padding(vertical = 4.dp))
            Text("arm64: ${platform.arm64}", modifier = Modifier.padding(vertical = 4.dp))
            Text("arm32: ${platform.arm32}", modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}
