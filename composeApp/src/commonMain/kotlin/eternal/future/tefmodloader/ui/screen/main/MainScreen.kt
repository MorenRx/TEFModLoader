package eternal.future.tefmodloader.ui.screen.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import eternal.future.tefmodloader.ui.navigation.NavigationViewModel
import eternal.future.tefmodloader.utility.Locales

val mainScreen = Locales()

expect object MainScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(mainViewModel: NavigationViewModel)
}