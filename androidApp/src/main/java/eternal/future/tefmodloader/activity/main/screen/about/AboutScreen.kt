package eternal.future.tefmodloader.activity.main.screen.about

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import eternal.future.tefmodloader.utils.App
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.utils.Net.openUrlInBrowser
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.activity.main.navigation.BackMode
import eternal.future.tefmodloader.activity.main.navigation.NavigationViewModel
import eternal.future.tefmodloader.config.AppState
import eternal.future.tefmodloader.widget.AboutScreen.AppIconCard
import eternal.future.tefmodloader.widget.AboutScreen.UserAgreementDialog
import eternal.future.tefmodloader.widget.AboutScreen.aboutWidgets
import eternal.future.tefmodloader.widget.AboutScreen.expandableWidget
import eternal.future.tefmodloader.widget.AppTopBar
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(mainViewModel: NavigationViewModel) {

    // 隐藏的赞赏码弹窗状态
    var showHiddenDonation by remember { mutableStateOf(false) }
    var tapCount by remember { mutableStateOf(0) }
    val hiddenDonationTimeout = 5000L // 5秒内连续点击5次触发

    // 长按触发逻辑
    LaunchedEffect(tapCount) {
        if (tapCount > 0) {
            delay(hiddenDonationTimeout)
            tapCount = 0
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val menuItems = mapOf(
                I18N.text(R.string.exit) to Pair(Icons.AutoMirrored.Filled.ExitToApp) {
                    App.exit()
                }
            )
            AppTopBar(
                title = I18N.text(R.string.title_about),
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
                var n = 0
                var physical by remember { mutableStateOf(false) }
                AppIconCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    labelText = I18N.string(R.string.about_slogan),
                    onClick = {
                        physical = true
                        n++
                        if (n >= 25) {
                            AppState.screen_rollback.value = true
                        }
                    }
                )
            }

            // 隐藏触发区域 - 完全透明
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .alpha(0.001f)
                        .clickable {
                            tapCount++
                            if (tapCount >= 20) {
                                showHiddenDonation = true
                                tapCount = 0
                            }
                        }
                ) {}
            }

            item {
                var userDialog by remember { mutableStateOf(false) }
                var loaderDialog by remember { mutableStateOf(false) }

                if (userDialog) {
                   UserAgreementDialog(
                        title = I18N.string(R.string.agreement_user_title),
                        content = I18N.string(R.string.agreement_user_desc),
                        onDismiss = { userDialog = false },
                        confirmButtonText = I18N.string(R.string.close),
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    )
                }

                if (loaderDialog) {
                    UserAgreementDialog(
                        title = I18N.string(R.string.agreement_loader_title),
                        content = I18N.string(R.string.agreement_loader_desc),
                        onDismiss = { loaderDialog = false },
                        confirmButtonText = I18N.string(R.string.close),
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    )
                }

                Text(
                    "App",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(10.dp)
                )

                var version_n by remember { mutableStateOf(0) }

                val context = LocalContext.current
                val versionName: String = context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "??"
                aboutWidgets(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Info,
                    title = I18N.string(R.string.version),
                    contentDescription = versionName,
                    onClick = {
                        version_n++
                        if (version_n >= 30) {
                            AppState.screen_physical.value = true
                            if (version_n >= 50) {
                                AppState.screen_revolve.value = true
                            }
                        }
                    }
                )

                aboutWidgets(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Info,
                    title = I18N.string(R.string.agreement_user_consent),
                    contentDescription = "",
                    onClick = {
                        userDialog = true
                    }
                )

                aboutWidgets(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Info,
                    title = I18N.string(R.string.agreement_loader_consent),
                    contentDescription = "",
                    onClick = {
                        loaderDialog = true
                    }
                )

                aboutWidgets(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.AccountBalance,
                    title = I18N.string(R.string.about_repo_title),
                    contentDescription = I18N.string(R.string.about_repo_desc),
                    onClick = {
                        openUrlInBrowser("https://github.com/2079541547/TEFModLoader")
                    }
                )
            }

            val contributorList = listOf(
                R.string.about_development to listOf(
                    R.drawable.avatar_eternalfuture to "EternalFuture゙" to R.string.about_contribution_eternalfuture,
                ),
                R.string.about_contributor to listOf(
                    R.drawable.avatar_morenrx to "MorenRx" to R.string.about_contribution_morenrx,
                    R.drawable.avatar_jiangniaht to "JiangNight" to R.string.about_contribution_jiangnight,
                )
            )

            contributorList.forEach {
                item {
                    Text(I18N.string(it.first), modifier = Modifier.padding(10.dp))
                    it.second.forEach {
                        expandableWidget(
                            modifier = Modifier.fillMaxWidth(),
                            icon = painterResource(it.first.first),
                            title = it.first.second,
                            detailedInfo = I18N.string(it.second),
                            onClick = {},
                            isCircularIcon = true
                        )
                    }
                }
            }


            item {
                Text(I18N.string(R.string.more), modifier = Modifier.padding(10.dp))

                aboutWidgets(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.FavoriteBorder,
                    title = I18N.string(R.string.about_donate_title),
                    contentDescription = I18N.string(R.string.about_donate_desc),
                    onClick = {
                        openUrlInBrowser("https://gitlab.com/2079541547/tefmodloader/-/blob/main/Document/donation.md")
                    }
                )

                aboutWidgets(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.ThumbUp,
                    title = I18N.string(R.string.about_thanks_title),
                    contentDescription = I18N.string(R.string.about_thanks_desc),
                    onClick = { mainViewModel.navigateTo("thanks") }
                )

                aboutWidgets(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Info,
                    title = I18N.string(R.string.about_license_title),
                    contentDescription = I18N.string(R.string.about_license_desc),
                    onClick = { mainViewModel.navigateTo("license") }
                )

                aboutWidgets(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.BugReport,
                    title = I18N.string(R.string.about_feedback_title),
                    contentDescription = I18N.string(R.string.about_feedback_desc),
                    onClick = { openUrlInBrowser("https://gitlab.com/2079541547/tefmodloader/-/issues/new") }
                )
            }
        }

        if (showHiddenDonation) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                AlertDialog(
                    onDismissRequest = { showHiddenDonation = false },
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = 8.dp,
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    ),
                    title = {
                        Text(
                            text = "支持我们 ❤️",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Spacer(Modifier.height(8.dp))

                            Spacer(Modifier.height(24.dp))

                            Text(
                                text = "您的支持是我们前进的动力",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "由于某些原因请联系2079541547@qq.com捐赠\n默认捐赠将显示为匿名",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "如需上捐赠名单，请发送邮件至：\n2079541547@qq.com\n附上您的微信名称和捐赠金额，最后是你想在捐赠名单上显示的名称",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    },
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TextButton(
                                onClick = { showHiddenDonation = false },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("我明白了", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                )
            }
        }
    }
}