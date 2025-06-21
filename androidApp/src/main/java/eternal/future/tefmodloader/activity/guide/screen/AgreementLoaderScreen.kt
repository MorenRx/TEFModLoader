package eternal.future.tefmodloader.activity.guide.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import eternal.future.tefmodloader.R
import eternal.future.tefmodloader.activity.guide.widget.AgreementCheckbox
import eternal.future.tefmodloader.manager.I18N

object AgreementLoaderScreen {
    val confirm = mutableStateOf(false)
}

@Composable
fun AgreementLoaderScreen(navController: NavHostController, padding: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AgreementCheckbox (
                title = I18N.text(R.string.agreement_loader_title),
                agreementText = I18N.text(R.string.agreement_loader_desc),
                checkBoxTitle = I18N.text(R.string.agreement_loader_consent),
                checkedState = AgreementLoaderScreen.confirm,
                onCheckBoxChange = { check ->
                    if (check) navController.popBackStack()
                }
            )
        }
    }
}