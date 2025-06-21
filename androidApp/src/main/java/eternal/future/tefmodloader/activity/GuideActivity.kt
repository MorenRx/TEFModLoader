package eternal.future.tefmodloader.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eternal.future.tefmodloader.activity.guide.screen.AgreementLoaderScreen
import eternal.future.tefmodloader.activity.guide.screen.AgreementUserScreen
import eternal.future.tefmodloader.activity.guide.screen.ImportantTipScreen
import eternal.future.tefmodloader.activity.guide.screen.PatchScreen
import eternal.future.tefmodloader.activity.guide.screen.WelComeScreen
import eternal.future.tefmodloader.config.AppPrefs
import eternal.future.tefmodloader.manager.ThemeManager.I18NTheme

class GuideActivity : ComponentActivity() {

    object Screen {
        const val SCREEN_WELCOME = "welcome"
        const val SCREEN_PATCH = "patch"
        const val SCREEN_AGREEMENT_USER = "agreement_user"
        const val SCREEN_AGREEMENT_LOADER = "agreement_loader"
        const val SCREEN_IMPORTANT_TIP = "important_tip"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            I18NTheme(AppPrefs.language.intValue, AppPrefs.theme.intValue, AppPrefs.darkMode.intValue) {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(it)
                }
            }
        }
    }
    @Composable
    private fun AppNavigation(padding: PaddingValues) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.SCREEN_WELCOME) {
            composable(Screen.SCREEN_WELCOME) { WelComeScreen(navController, padding) }
            composable(Screen.SCREEN_PATCH) { PatchScreen(navController, padding) }
            composable(Screen.SCREEN_AGREEMENT_USER) { AgreementUserScreen(navController, padding) }
            composable(Screen.SCREEN_AGREEMENT_LOADER) { AgreementLoaderScreen(navController, padding) }
            composable(Screen.SCREEN_IMPORTANT_TIP) { ImportantTipScreen(navController, padding) }
        }
    }
}