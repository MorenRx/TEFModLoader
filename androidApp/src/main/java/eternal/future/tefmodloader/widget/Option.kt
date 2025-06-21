package eternal.future.tefmodloader.widget

import android.R.attr.checked
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

object Option {

    @Composable
    fun OptionTitle(
        title: String,
    ) {
        Row(modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 52.dp)) {
            Text(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(start = 24.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                text = title,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }

    @Composable
    fun OptionSwitch(
        icon: ImageVector,
        title: String,
        description: String? = null,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val minHeight = if (description == null) 56.dp else 74.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = minHeight)
                .clickable (
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = { onCheckedChange(!checked) }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Spacer(Modifier.width(24.dp))
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Column(modifier = Modifier.weight(1f).padding(start = 24.dp, end = 12.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                if (description != null) {
                    Text(text = description, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                interactionSource = interactionSource
            )
            Spacer(Modifier.width(16.dp))
        }
    }

    @Composable
    fun OptionPopMenu(
        icon: ImageVector,
        title: String,
        defaultSelectorId: Int,
        selectorList: List<String>,
        onClick: (Int) -> Unit = {}
    ) {
        var expanded by remember { mutableStateOf(false) }
        var validId by remember { mutableIntStateOf(defaultSelectorId.coerceIn(0, selectorList.lastIndex)) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 74.dp)
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.width(24.dp))

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Column(modifier = Modifier.weight(1f).padding(start = 24.dp, end = 12.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = selectorList[validId], style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.width(16.dp))

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(x = 72.dp, y = 0.dp),
                shape = RoundedCornerShape(8.dp),
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ) {
                selectorList.forEachIndexed { index, id ->
                    DropdownMenuItem(
                        text = { Text(
                            modifier = Modifier.padding(start = 6.dp),
                            text = id,
                            style = MaterialTheme.typography.titleMedium
                        ) },
                        onClick = {
                            onClick(index)
                            validId = index
                            expanded = false
                        },
                        modifier = Modifier
                            .background(
                                if (validId == index) MaterialTheme.colorScheme.onBackground.copy(alpha = .1f)
                                else Color.Transparent
                            )
                    )
                }
            }

        }
    }
}