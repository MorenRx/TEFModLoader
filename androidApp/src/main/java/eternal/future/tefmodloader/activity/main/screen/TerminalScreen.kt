package eternal.future.tefmodloader.activity.main.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eternal.future.tefmodloader.utils.App
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.activity.main.navigation.BackMode
import eternal.future.tefmodloader.activity.main.navigation.NavigationViewModel
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.widget.AppTopBar

object TerminalScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TerminalScreen(mainViewModel: NavigationViewModel) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val menuItems = mapOf(I18N.text(R.string.exit) to Pair(Icons.AutoMirrored.Filled.ExitToApp) { App.exit() })
                AppTopBar(
                    title = "Terminal",
                    showMenu = true,
                    menuItems = menuItems,
                    showBackButton = true,
                    onBackClick = {
                        mainViewModel.navigateBack(BackMode.TO_DEFAULT)
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                eternal.future.tefmodloader.activity.main.debug.TerminalScreen()
            }
        }
    }
}