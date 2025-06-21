package eternal.future.tefmodloader.widget.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

object SettingScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Selector(
        title: String,
        defaultSelectorId: Int,
        selectorList: List<String>,
        modifier: Modifier,
        onClick: (Int) -> Unit = {}
    ) {
        val validId = defaultSelectorId.coerceIn(0, selectorList.lastIndex)
        var selectedId by remember { mutableIntStateOf(validId) }
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.padding(10.dp)
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectorList[selectedId],
                onValueChange = {},
                label = { Text(title) },
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand",
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                },
                modifier = modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                selectorList.forEachIndexed { index, id ->
                    DropdownMenuItem(
                        text = { Text(id) },
                        onClick = {
                            onClick(index)
                            selectedId = index
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SelectorWithIcon(
        title: String,
        defaultSelectorId: Int = 0,
        selectorList: List<Pair<String, ImageVector>>,
        modifier: Modifier,
        onClick: (Int) -> Unit = {}
    ) {
        val validId = defaultSelectorId.coerceIn(0, selectorList.lastIndex)
        var selectedId by remember { mutableIntStateOf(validId) }

        var expanded by remember { mutableStateOf(false) }
        val icon = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.padding(10.dp)
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectorList[selectedId].first,
                onValueChange = {},
                label = { Text(title) },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = selectorList[selectedId].second,
                            contentDescription = "Theme Icon"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = icon,
                            contentDescription = "Expand",
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    }
                },
                modifier = modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                selectorList.forEachIndexed { index, pair ->
                    DropdownMenuItem(
                        onClick = {
                            selectedId = index
                            onClick(index)
                            expanded = false
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = pair.second, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(pair.first)
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun GeneralTextInput(
        title: String,
        value: String,
        onValueChange: (String) -> Unit,
        leadingIcon: (@Composable () -> Unit)? = null,
        trailingIcon: (@Composable () -> Unit)? = null,
        modifier: Modifier = Modifier,
        placeholder: String? = null,
        singleLine: Boolean = true
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(title) },
            leadingIcon = leadingIcon?.let { { it() } },
            trailingIcon = trailingIcon?.let { { it() } },
            modifier = modifier,
            placeholder = if (!placeholder.isNullOrBlank()) {
                { Text(placeholder) }
            } else null,
            singleLine = singleLine
        )
    }

    @Composable
    fun SettingsSwitchItem(
        iconOn: ImageVector? = null,
        iconOff: ImageVector? = null,
        title: String,
        contentDescription: String = "",
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit = {},
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                val currentIcon = when {
                    checked && iconOn != null -> iconOn
                    !checked && iconOff != null -> iconOff
                    iconOn != null -> iconOn
                    else -> iconOff
                }

                currentIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(text = title, style = MaterialTheme.typography.titleMedium)
                    if (contentDescription.isNotEmpty()) {
                        Text(
                            text = contentDescription,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }

    @Composable
    fun ModernCheckBox(
        modifier: Modifier = Modifier,
        icon: ImageVector? = null,
        title: String,
        contentDescription: String,
        isChecked: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(text = title, style = MaterialTheme.typography.titleMedium)
                    if (contentDescription.isNotEmpty()) {
                        Text(
                            text = contentDescription,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    uncheckedColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }

    @Composable
    fun ActionButton(
        modifier: Modifier = Modifier,
        icon: ImageVector? = null,
        title: String,
        description: String? = null,
        onClick: () -> Unit
    ) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Column {
                    Text(text = title, style = MaterialTheme.typography.titleMedium)
                    if (!description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun PathInputWithFilePicker(
        title: String,
        path: String,
        onPathChange: (String) -> Unit,
        onFolderSelect: () -> Unit,
        modifier: Modifier
    ) {
        TODO("Not yet implemented")
    }

}