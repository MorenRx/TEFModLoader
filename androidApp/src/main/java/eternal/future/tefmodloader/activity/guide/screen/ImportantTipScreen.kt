package eternal.future.tefmodloader.activity.guide.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.activity.MainActivity.Companion.mainViewModel
import eternal.future.tefmodloader.activity.main.navigation.NavigationViewModel
import eternal.future.tefmodloader.config.AppConf
import eternal.future.tefmodloader.config.AppState
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.utils.EFModLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.jvm.javaClass

@Composable
fun ImportantTipScreen(navController: NavHostController, padding: PaddingValues) {
    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "学习")
            Text(text = "算命")
            Text(text = "百度")
        }


        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        ExtendedFloatingActionButton(
            text = { Text(I18N.text(R.string.next)) },
            icon = { Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "Next") },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = {
                start(context, scope, mainViewModel)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
        )
    }

}

fun start(context: Context, scope: CoroutineScope, mainViewModel: NavigationViewModel) {
    scope.launch {
        if (AppState.defaultLoader.value) {
            try {
                withContext(Dispatchers.IO) {
                    val tempFile = File.createTempFile("TEFModLoader", ".efml")
                    val target = File(AppConf.PATH_EFMOD_LOADER, "default")

                    FileOutputStream(tempFile).use { fileOutputStream ->
                        javaClass.classLoader?.getResourceAsStream("tefmodloader.efml")?.copyTo(fileOutputStream)
                    }

                    EFModLoader.install(tempFile.path, target.path)
                    File(target, "enabled").mkdirs()
                    tempFile.delete()
                }
            } catch (e: IOException) {
            }
        }

        // 导航操作回到主线程
        withContext(Dispatchers.Main) {
            mainViewModel.setInitialScreen("main")
            mainViewModel.navigateTo("main")
        }
    }
}