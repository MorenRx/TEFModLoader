package eternal.future.tefmodloader.activity.main.screen.about

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eternal.future.tefmodloader.activity.main.navigation.BackMode
import eternal.future.tefmodloader.activity.main.navigation.NavigationViewModel
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.utils.App
import eternal.future.tefmodloader.widget.AppTopBar
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.widget.AboutScreen.projectInfoCard

object ThanksScreen {

    val thanks = listOf(
        "雨鹜" to I18N.string(R.string.about_contribution_yuwu),
        "QwQ3094" to I18N.string(R.string.about_contribution_qwq3094),
        "aaa1115910" to I18N.string(R.string.about_contribution_aaa1115910),

    )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ThanksScreen(mainViewModel: NavigationViewModel) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val menuItems = mapOf(
                    I18N.text(R.string.back_home) to Pair(Icons.AutoMirrored.Filled.ExitToApp) {
                        mainViewModel.navigateBack(
                            BackMode.TO_DEFAULT
                        )
                    },
                    I18N.text(R.string.exit) to Pair(Icons.AutoMirrored.Filled.ExitToApp) { App.exit() },
                )
                AppTopBar(
                    title = I18N.text(R.string.title_thanks),
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
                    thanks.forEach {
                        projectInfoCard(
                            modifier = Modifier.padding(10.dp),
                            titleText = it.first,
                            descriptionText = it.second,
                            additionalInfoText = "",
                            onClick = { }
                        )

                    }
                }
            }
        }
    }
}