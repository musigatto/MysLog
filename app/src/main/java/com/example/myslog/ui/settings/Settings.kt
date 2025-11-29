package com.example.myslog.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.myslog.R
import com.example.myslog.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val mContext = LocalContext.current
    val keepScreenOn by viewModel.keepScreenOn.collectAsState()
    val userName by viewModel.userName.collectAsState()

    // Estados para diálogos
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showClearDatabaseDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf("") }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = CreateDocument("application/zip"),
        onResult = { uri ->
            uri?.let { viewModel.onEvent(SettingsEvent.ExportDatabase(mContext, it)) }
        }
    )
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { viewModel.onEvent(SettingsEvent.ImportDatabase(mContext, it)) }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.FileCreated -> exportLauncher.launch(event.fileName)
                is UiEvent.Navigate -> navController.navigate(event.route)
                else -> Unit
            }
        }
    }

    // Diálogo para editar nombre
    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text(stringResource(R.string.edit_name)) },
            text = {
                Column {
                    Text(stringResource(R.string.change_username))
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text(stringResource(R.string.your_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(SettingsEvent.UpdateUserName(tempName))
                        showEditNameDialog = false
                    }
                ) {
                    Text(stringResource(R.string.accept))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEditNameDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // NUEVO: Diálogo de confirmación para borrar base de datos
    if (showClearDatabaseDialog) {
        AlertDialog(
            onDismissRequest = { showClearDatabaseDialog = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null) },
            title = { Text(stringResource(R.string.delete_alldb)) },
            text = { Text(stringResource(R.string.delete_db_really)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(SettingsEvent.ClearDatabase)
                        showClearDatabaseDialog = false
                    }
                ) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearDatabaseDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.prefs_app),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Sección: Perfil de usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.profile),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.username),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = userName.ifEmpty { stringResource(R.string.not_stablished) },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        FilledTonalButton(
                            onClick = {
                                tempName = userName
                                showEditNameDialog = true
                            }
                        ) {
                            Text(stringResource(R.string.edit))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Sección: Preferencias de pantalla
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.screen),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.keep_screen_on),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = stringResource(R.string.screen_on_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Switch(
                            checked = keepScreenOn,
                            onCheckedChange = { checked ->
                                viewModel.onEvent(SettingsEvent.ToggleKeepScreenOn(checked))
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Sección: Copias de seguridad
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.back_ups),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(12.dp))

                    // Exportar base de datos
                    FilledTonalButton(
                        onClick = { viewModel.onEvent(SettingsEvent.CreateFile) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.export_db))
                    }

                    Spacer(Modifier.height(8.dp))

                    // Importar base de datos
                    OutlinedButton(
                        onClick = { importLauncher.launch("application/zip") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Upload,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.import_db))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Sección: Peligro
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.danger),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }

                    Text(
                        text = stringResource(R.string.ask_delete),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Borrar base de datos
                    OutlinedButton(
                        onClick = { showClearDatabaseDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.delete_db_subtitle))
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}