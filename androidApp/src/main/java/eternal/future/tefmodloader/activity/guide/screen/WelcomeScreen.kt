package eternal.future.tefmodloader.activity.guide.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.activity.GuideActivity
import eternal.future.tefmodloader.config.AppPrefs
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.manager.OptionManager
import eternal.future.tefmodloader.widget.Option

@Composable
fun WelComeScreen(navController: NavHostController, padding: PaddingValues) {

    Box(modifier = Modifier.fillMaxSize().padding(padding)) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = I18N.text(R.string.title_welcome),
                    fontSize = 24.sp
                )
            }

            Card(
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding( horizontal = 24.dp)
            ) {
                Option.OptionTitle(I18N.text(R.string.setting_title_general))

                Option.OptionPopMenu(
                    icon = Icons.Outlined.Language,
                    title = I18N.text(R.string.setting_language),
                    defaultSelectorId = AppPrefs.language.intValue,
                    selectorList = I18N.texts(OptionManager.optionsLanguage),
                    onClick = {
                        AppPrefs.setLanguage(it)
                    }
                )

                Option.OptionPopMenu(
                    icon = Icons.Outlined.Palette,
                    title = I18N.text(R.string.setting_theme),
                    defaultSelectorId = AppPrefs.theme.intValue,
                    selectorList = I18N.texts(OptionManager.optionsTheme),
                    onClick = {
                        AppPrefs.setTheme(it)
                    }
                )

                Option.OptionPopMenu(
                    icon = Icons.Outlined.DarkMode,
                    title = I18N.text(R.string.setting_dark_mode),
                    defaultSelectorId = AppPrefs.darkMode.intValue,
                    selectorList = I18N.texts(OptionManager.optionsDarkMode),
                    onClick = {
                        AppPrefs.setDarkMode(it)
                    }
                )

                Option.OptionTitle(I18N.text(R.string.agreement_user_title))

                Option.OptionSwitch(
                    icon = Icons.Outlined.Description,
                    title = I18N.text(R.string.agreement_user_title),
                    checked = AgreementUserScreen.confirm.value,
                    onCheckedChange = {
                        navController.navigate(GuideActivity.Screen.SCREEN_AGREEMENT_USER)
                    }
                )

                Option.OptionSwitch(
                    icon = Icons.Outlined.Description,
                    title = I18N.text(R.string.agreement_loader_title),
                    checked = AgreementLoaderScreen.confirm.value,
                    onCheckedChange = {
                        navController.navigate(GuideActivity.Screen.SCREEN_AGREEMENT_LOADER)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        if (AgreementUserScreen.confirm.value && AgreementLoaderScreen.confirm.value) {
            ExtendedFloatingActionButton(
                text = { Text(I18N.text(R.string.next)) },
                icon = { Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "Next") },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = { navController.navigate(GuideActivity.Screen.SCREEN_PATCH) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(30.dp)
            )
        }
    }
}