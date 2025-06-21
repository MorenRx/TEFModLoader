package eternal.future.tefmodloader.activity.guide.widget.patch

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.activity.guide.screen.PatchScreen
import eternal.future.tefmodloader.config.AppState
import eternal.future.tefmodloader.manager.I18N
import eternal.future.tefmodloader.widget.main.SettingScreen


@Composable
fun OptionsExternal() {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        PatchScreen.selectedFileUri.value = uri
    }

    SettingScreen.ModernCheckBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        title = I18N.text(R.string.setting_override),
        contentDescription = I18N.text(R.string.setting_override_desc),
        isChecked = AppState.OverrideVersion.value,
        onCheckedChange = { select ->
            AppState.OverrideVersion.value = select
        }
    )

    SettingScreen.ModernCheckBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        title = I18N.text(R.string.setting_bypass),
        contentDescription = I18N.text(R.string.setting_bypass_desc),
        isChecked = AppState.isBypass.value,
        onCheckedChange = { select ->
            AppState.isBypass.value = select
        }
    )

    SettingScreen.ModernCheckBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        title = I18N.text(R.string.setting_debug),
        contentDescription = I18N.text(R.string.setting_debug_desc),
        isChecked = AppState.Debugging.value,
        onCheckedChange = { select ->
            AppState.Debugging.value = select
        }
    )

    Text(
        I18N.text(R.string.setting_custom_apk_desc),
        modifier = Modifier.padding(10.dp)
    )

    SettingScreen.GeneralTextInput(
        title = I18N.text(R.string.setting_custom_apk),
        value = AppState.ApkPath.value,
        onValueChange = {},
        trailingIcon = {
            IconButton(onClick = {
                launcher.launch("application/vnd.android.package-archive")
            }) {
                Icon(Icons.Default.Folder, contentDescription = "选择文件夹")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}