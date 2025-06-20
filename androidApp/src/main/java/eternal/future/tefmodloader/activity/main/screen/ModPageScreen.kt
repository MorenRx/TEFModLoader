package eternal.future.tefmodloader.activity.main.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eternal.future.tefmodloader.activity.main.navigation.BackMode
import eternal.future.tefmodloader.activity.main.navigation.NavigationViewModel
import eternal.future.tefmodloader.utils.loadPageFromFile
import eternal.future.tefmodloader.widget.AppTopBar
import java.io.File

object ModPageScreen {

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun ModPageScreen(mainViewModel: NavigationViewModel) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AppTopBar(
                    title = mainViewModel.getExtraData("title") as String,
                    showMenu = false,
                    showBackButton = true,
                    onBackClick = {
                        mainViewModel.navigateBack(BackMode.TO_DEFAULT)
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                loadPageFromFile(
                    File(mainViewModel.getExtraData("page-path") as String),
                    mainViewModel.getExtraData("page-class") as String,
                    mainViewModel.getExtraData("page-extraData") as Map<String, Any>
                )
            }
        }
    }

}