package eternal.future.tefmodloader.activity.main.screen.main

import android.R.attr.label
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import eternal.future.tefmodloader.activity.main.navigation.BackMode
import eternal.future.tefmodloader.activity.main.navigation.DefaultScreen
import eternal.future.tefmodloader.activity.main.navigation.NavigationViewModel
import eternal.future.tefmodloader.activity.main.navigation.Screen
import eternal.future.tefmodloader.activity.main.navigation.ScreenRegistry
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.widget.AppTopBar

object MainScreen {

    val viewModel = NavigationViewModel()

    init {
        listOf(
            DefaultScreen("home"),
            DefaultScreen("efmod"),
            DefaultScreen("loader")
        ).forEach {
            ScreenRegistry.register(it)
        }
        viewModel.setInitialScreen("home")
    }

    val title = mutableStateOf(R.string.title_home)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(mainViewModel: NavigationViewModel) {

        val currentScreenWithAnimation by viewModel.currentScreen.collectAsState()

        Scaffold(
            topBar = {
                val menuItems = mutableMapOf(
                    I18N.text(R.string.title_about) to Pair(Icons.Default.Info) { mainViewModel.navigateTo("about") },
                    I18N.text(R.string.title_help) to Pair(Icons.AutoMirrored.Filled.Help) { mainViewModel.navigateTo("help") },
                    I18N.text(R.string.title_settings) to Pair(Icons.Default.Settings) { mainViewModel.navigateTo("settings") },
                    I18N.text(R.string.title_terminal) to Pair(Icons.Default.Terminal) { mainViewModel.navigateTo("terminal") },
                    I18N.text(R.string.exit) to Pair(Icons.AutoMirrored.Filled.ExitToApp) { mainViewModel.navigateBack(BackMode.ONE_BY_ONE) }
                )
                AppTopBar(
                    title = I18N.text(title.value),
                    menuItems = menuItems
                )
            },
            bottomBar = {

                val items = listOf(
                    Triple("home", Icons.Default.Home, R.string.title_home),
                    Triple("efmod", Icons.Filled.Extension, R.string.title_manager_mod),
                    Triple("loader", Icons.Filled.Build, R.string.title_manager_loader),
                )

                NavigationBar {
                    items.forEach {
                        NavigationBarItem(
                            selected = title.value == it.third,
                            onClick = {
                                title.value = it.third
                                viewModel.navigateTo(it.first)
                            },
                            icon = { Icon(it.second,I18N.text(it.third)) },
                            label = { Text(I18N.text(it.third)) },
                            alwaysShowLabel = false
                        )
                    }
                }
            }
        ) { innerPadding ->
            Crossfade(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                targetState = currentScreenWithAnimation, animationSpec = tween(durationMillis = 500)
            ) { state ->
                state.let { (screen, _) ->
                    if (screen != null) {
                        when (screen.id) {
                            "home" -> {
                                HomeScreen.HomeScreen()
                                title.value = R.string.title_home
                            }
                            "efmod" -> {
                                EFModScreen.EFModScreen()
                                title.value = R.string.title_manager_mod
                            }
                            "loader" -> {

                                LoaderScreen.LoaderScreen()
                                title.value = R.string.title_manager_loader
                            }
                            else -> UnknownScreen(screen)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun UnknownScreen(screen: Screen) {
        Column {
            Text("Unknown screen: ${screen.id}")
            Button(modifier = Modifier,
                content = {
                    Text("Back to default")
                },
                onClick = {
                    viewModel.navigateBack(BackMode.TO_DEFAULT)
                }
            )
        }
    }
}