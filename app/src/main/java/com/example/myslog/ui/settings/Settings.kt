    package com.example.myslog.ui.settings

    import androidx.activity.compose.rememberLauncherForActivityResult
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Checkbox
    import androidx.compose.material3.FilledTonalButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.stringResource
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

        val exportLauncher = rememberLauncherForActivityResult(
            contract = CreateDocument("application/json"),
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

        Scaffold { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineMedium)

                Spacer(Modifier.height(16.dp))

                // 🔲 Nuevo checkbox para mantener la pantalla encendida
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = keepScreenOn,
                        onCheckedChange = { checked ->
                            viewModel.onEvent(SettingsEvent.ToggleKeepScreenOn(checked))
                        }
                    )
                    Text(stringResource(R.string.keep_screen))
                }

                Spacer(Modifier.height(16.dp))

                FilledTonalButton(onClick = {
                    viewModel.onEvent(SettingsEvent.CreateFile)
                }) {
                    Text(stringResource(R.string.export_database))
                }

                FilledTonalButton(onClick = {
                    importLauncher.launch("application/json")
                }) {
                    Text(stringResource(R.string.import_database))
                }

                FilledTonalButton(onClick = {
                    viewModel.onEvent(SettingsEvent.ClearDatabase)
                }) {
                    Text(stringResource(R.string.delete_database))
                }
            }
        }
    }
