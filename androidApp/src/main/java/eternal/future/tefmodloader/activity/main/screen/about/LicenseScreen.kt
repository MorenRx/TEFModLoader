package eternal.future.tefmodloader.activity.main.screen.about

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eternal.future.tefmodloader.activity.main.navigation.BackMode
import eternal.future.tefmodloader.activity.main.navigation.NavigationViewModel
import eternal.future.tefmodloader.utils.App
import eternal.future.tefmodloader.widget.AboutScreen.projectInfoCard
import eternal.future.tefmodloader.widget.AppTopBar
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.manager.I18N
import androidx.core.net.toUri

object LicenseScreen {

    data class LicenseInfo(
        val name: String,
        val description: String,
        val license: String,
        val url: String
    )

    object License {
        const val NONE: String = ""
        const val MIT: String = "MIT"
        const val AGPL_3_0: String = "AGPL-3.0"
        const val APACHE_2_0: String = "Apache-2.0"
    }

    val licenseInfoList = listOf(
        LicenseInfo(
            "EFModLoader",
            "一种为EFMod设计的高效侵入式模组加载器",
            "https://github.com/2079541547/EFModLoader",
            License.AGPL_3_0
        ),
        LicenseInfo(
            "ByNameModding",
            "ByNameModding is a library for modding il2cpp games by classes, methods, field names on Android. This edition is focused on working on Android with il2cpp. It includes everything you need for modding unity games.\n" +
                    "Requires C++20 minimum.",
            "https://github.com/ByNameModding/BNM-Android",
            License.MIT
        ),
        LicenseInfo(
            "SilkCasket",
            "一个注重灵活性的压缩包格式",
            "https://github.com/2079541547/SilkCasket",
            License.APACHE_2_0
        ),
        LicenseInfo(
            "Axml2xml",
            "An advance and enhanced axml compiler",
            "https://github.com/developer-krushna/Axml2xml",
            License.NONE
        )
    )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LicenseScreen(mainViewModel: NavigationViewModel) {
        val context = LocalContext.current

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val menuItems = mapOf(
                    I18N.text(R.string.back_home) to Pair(Icons.AutoMirrored.Filled.ExitToApp) {
                        mainViewModel.navigateBack(BackMode.TO_DEFAULT)
                    },
                    I18N.text(R.string.exit) to Pair(Icons.AutoMirrored.Filled.ExitToApp) { App.exit() },
                )
                AppTopBar(
                    title = I18N.text(R.string.title_license),
                    showMenu = true,
                    menuItems = menuItems,
                    showBackButton = true,
                    onBackClick = {
                        mainViewModel.navigateBack(BackMode.ONE_BY_ONE)
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding
            ) {
                item {
                    licenseInfoList.forEach {
                        projectInfoCard(
                            modifier = Modifier.padding(10.dp),
                            titleText = it.name,
                            descriptionText = it.description,
                            additionalInfoText = it.license,
                            onClick = {
                                context.startActivity(Intent(Intent.ACTION_VIEW, it.url.toUri()))
                            }
                        )
                    }
                }
            }
        }
    }
}

