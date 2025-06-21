package eternal.future.tefmodloader.activity.guide.screen

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.InstallDesktop
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.activity.GuideActivity
import eternal.future.tefmodloader.activity.guide.widget.patch.OptionsExternal
import eternal.future.tefmodloader.config.AppPrefsOld
import eternal.future.tefmodloader.config.AppState
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.manager.OptionManager
import eternal.future.tefmodloader.widget.Option

object PatchScreen {
    val selectedFileUri = mutableStateOf<Uri?>(null)
}

@Composable
fun PatchScreen(navController: NavHostController, padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Option.OptionTitle(I18N.text(R.string.guide_enable_tip))

            Option.OptionSwitch(
                icon = Icons.Outlined.InstallDesktop,
                title = I18N.text(R.string.guide_default_loader),
                description = I18N.text(R.string.guide_default_loader_desc),
                checked = AppState.defaultLoader.value,
                onCheckedChange = { check ->
                    AppState.defaultLoader.value = check
                }
            )

            val optionsLog = listOf(
                I18N.text(R.string.disable) to 0,
                I18N.text(R.string.unlimited) to -1,
                "512 kb" to 512 * 1024,
                "1024 kb" to 1024 * 1024,
                "2048 kb" to 2048 * 1024,
                "4096 kb" to 4096 * 1024,
                "8192 kb" to 8192 * 1024
            )

            Option.OptionPopMenu(
                icon = Icons.AutoMirrored.Outlined.Assignment,
                title = I18N.text(R.string.setting_log_cache),
                defaultSelectorId = AppPrefsOld.logCacheMaximum,
                selectorList = optionsLog.map { it.first },
                onClick = {
                    AppPrefsOld.logCacheMaximum = optionsLog[it].second
                }
            )

            if (AppState.Mode.intValue != 0) {
                Option.OptionTitle(I18N.text(R.string.setting_title_advanced))
            }

            Option.OptionPopMenu(
                icon = Icons.Outlined.SportsEsports,
                title = I18N.text(R.string.setting_launch_mode),
                defaultSelectorId = AppState.Mode.intValue,
                selectorList = I18N.texts(OptionManager.optionsLauncherMode),
                onClick = {
                    AppState.Mode.intValue = it
                }
            )

            if (AppState.Mode.intValue == 1) {
                OptionsExternal()
            }
        }

        ExtendedFloatingActionButton(
            text = { Text(I18N.text(R.string.next)) },
            icon = { Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "Next") },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = {
                navController.navigate(GuideActivity.Screen.SCREEN_IMPORTANT_TIP)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
        )
    }
}

