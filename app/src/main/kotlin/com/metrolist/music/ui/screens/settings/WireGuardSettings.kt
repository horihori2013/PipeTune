package com.metrolist.music.ui.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.metrolist.music.R
import com.metrolist.music.constants.WireGuardAutoConnectKey
import com.metrolist.music.constants.WireGuardConfigContentKey
import com.metrolist.music.constants.WireGuardConfigNameKey
import com.metrolist.music.constants.WireGuardEnabledKey
import com.metrolist.music.ui.component.Material3SettingsGroup
import com.metrolist.music.ui.component.Material3SettingsItem
import com.metrolist.music.ui.component.Material3SettingsToggle
import com.metrolist.music.ui.component.liquidglass.LiquidSwitch
import androidx.compose.foundation.layout.Spacer
import com.metrolist.music.utils.WireGuardConfig
import com.metrolist.music.utils.WireGuardManager
import com.metrolist.music.utils.rememberPreference
import timber.log.Timber

@Composable
fun WireGuardSettings(
    navController: NavController,
) {
    val context = LocalContext.current
    val (wireGuardEnabled, onWireGuardEnabledChange) = rememberPreference(WireGuardEnabledKey, false)
    val (wireGuardConfigName, onWireGuardConfigNameChange) = rememberPreference(WireGuardConfigNameKey, "")
    val (wireGuardConfigContent, onWireGuardConfigContentChange) = rememberPreference(WireGuardConfigContentKey, "")
    val (wireGuardAutoConnect, onWireGuardAutoConnectChange) = rememberPreference(WireGuardAutoConnectKey, false)

    var showImportDialog by rememberSaveable { mutableStateOf(false) }
    var showRemoveConfigDialog by rememberSaveable { mutableStateOf(false) }
    var importError by rememberSaveable { mutableStateOf<String?>(null) }

    val hasConfig = wireGuardConfigContent.isNotBlank()

    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val content = inputStream?.bufferedReader()?.readText() ?: ""
                inputStream?.close()

                val fileName = uri.lastPathSegment?.substringAfterLast("/") ?: "wireguard.conf"
                val config = WireGuardConfig.parse(fileName, content)

                if (config != null) {
                    showImportDialog = true
                    importError = null
                    onWireGuardConfigNameChange(config.name.ifBlank { fileName.removeSuffix(".conf") })
                    onWireGuardConfigContentChange(content)
                } else {
                    importError = context.getString(R.string.wireguard_config_error)
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to read WireGuard config")
                importError = context.getString(R.string.wireguard_config_error)
            }
        }
    }

    if (showImportDialog) {
        var tempName by rememberSaveable { mutableStateOf(wireGuardConfigName) }

        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text(stringResource(R.string.wireguard_config_name)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.wireguard_config_imported),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text(stringResource(R.string.wireguard_config_name)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onWireGuardConfigNameChange(tempName)
                    showImportDialog = false
                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImportDialog = false
                    onWireGuardConfigContentChange("")
                    onWireGuardConfigNameChange("")
                }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }

    if (showRemoveConfigDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveConfigDialog = false },
            title = { Text(stringResource(R.string.wireguard_remove_config)) },
            text = {
                Text(text = "Remove the WireGuard configuration \"$wireGuardConfigName\"?")
            },
            confirmButton = {
                TextButton(onClick = {
                    if (wireGuardEnabled) {
                        WireGuardManager.stop()
                        onWireGuardEnabledChange(false)
                    }
                    onWireGuardConfigContentChange("")
                    onWireGuardConfigNameChange("")
                    showRemoveConfigDialog = false
                }) {
                    Text(stringResource(R.string.wireguard_remove_config))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveConfigDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }

    Column(
        Modifier
            .windowInsetsPadding(com.metrolist.music.LocalPlayerAwareWindowInsets.current)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Material3SettingsGroup(
            title = stringResource(R.string.wireguard),
            items = listOf(
                Material3SettingsItem(
                    icon = painterResource(R.drawable.wifi_proxy),
                    title = { Text(stringResource(R.string.enable_wireguard)) },
                    description = { Text(stringResource(R.string.wireguard_desc)) },
                    trailingContent = {
                        LiquidSwitch(
                            checked = wireGuardEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled && !hasConfig) {
                                    filePickerLauncher.launch(arrayOf("text/*", "*/*"))
                                } else if (enabled) {
                                    val started = WireGuardManager.start(
                                        context,
                                        wireGuardConfigName.ifBlank { "WireGuard" },
                                        wireGuardConfigContent,
                                    )
                                    onWireGuardEnabledChange(started)
                                } else {
                                    WireGuardManager.stop()
                                    onWireGuardEnabledChange(false)
                                }
                            },
                        )
                    },
                    onClick = {
                        if (!hasConfig) {
                            filePickerLauncher.launch(arrayOf("text/*", "*/*"))
                        }
                    }
                ),
            ),
        )

        if (hasConfig) {
            Spacer(modifier = Modifier.height(16.dp))

            Material3SettingsGroup(
                title = stringResource(R.string.wireguard_config_name),
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.info),
                        title = { Text(wireGuardConfigName.ifBlank { "WireGuard" }) },
                        description = {
                            val config = WireGuardConfig.parse(wireGuardConfigName, wireGuardConfigContent)
                            Text(config?.endpoint ?: "No endpoint")
                        },
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.settings),
                        title = { Text(stringResource(R.string.import_wireguard_config)) },
                        onClick = {
                            filePickerLauncher.launch(arrayOf("text/*", "*/*"))
                        },
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.delete),
                        title = { Text(stringResource(R.string.wireguard_remove_config)) },
                        onClick = { showRemoveConfigDialog = true },
                    ),
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Material3SettingsGroup(
                title = stringResource(R.string.settings),
                items = listOf(
                    Material3SettingsToggle(
                        icon = painterResource(R.drawable.wifi_proxy),
                        title = stringResource(R.string.wireguard_auto_connect),
                        checked = wireGuardAutoConnect,
                        onCheckedChange = onWireGuardAutoConnectChange,
                    ),
                ),
            )
        }
    }
}
