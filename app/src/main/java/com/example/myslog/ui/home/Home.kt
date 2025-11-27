// Home.kt (modificado)
package com.example.myslog.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myslog.R
import com.example.myslog.ui.home.components.HomeBottomBar
import com.example.myslog.ui.home.components.SessionCard
import com.example.myslog.ui.tutorial.TutorialDialog
import com.example.myslog.ui.tutorial.TutorialViewModel
import com.example.myslog.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    userName: String = "",
    viewModel: HomeViewModel = hiltViewModel(),
    tutorialViewModel: TutorialViewModel = hiltViewModel()
) {
    val sessions by viewModel.sessions.collectAsState()
    val sessionToDelete = viewModel.sessionToDelete
    val snackbarHostState = remember { SnackbarHostState() }

    // Estado del tutorial
    val tutorialState by tutorialViewModel.tutorialState.collectAsStateWithLifecycle()

    // Mostrar diálogo de tutorial si está activo
    if (tutorialState.showTutorial) {
        TutorialDialog(
            currentStep = tutorialState.currentStep,
            totalSteps = TutorialViewModel.TOTAL_TUTORIAL_STEPS,
            onNext = { tutorialViewModel.nextStep() },
            onSkip = { tutorialViewModel.skipTutorial() }
        )
    }

    // Diálogo de eliminación de sesión (existente)
    if (sessionToDelete != null) {
        AlertDialog(
            onDismissRequest = { viewModel.sessionToDelete = null },
            title = { Text(stringResource(R.string.delete_session)) },
            text = { Text(stringResource(R.string.sure_about_that)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(
                        HomeEvent.ConfirmDeleteSession(
                            sessionToDelete.session.sessionId
                        )
                    )
                }) { Text(stringResource(R.string.delete)) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.sessionToDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.Navigate) onNavigate(event)
        }
    }

    // Iniciar tutorial automáticamente si no se ha completado y es el primer acceso
    LaunchedEffect(Unit) {
        if (!tutorialState.isTutorialCompleted && sessions.isEmpty()) {
            tutorialViewModel.startTutorial()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            HomeBottomBar(onEvent = { event ->
                when (event) {
                    else -> viewModel.onEvent(event)
                }
            })
        },
        floatingActionButton = {
            // Botón para reiniciar tutorial (solo para testing)
            if (!tutorialState.showTutorial) {
                FloatingActionButton(
                    onClick = { tutorialViewModel.startTutorial() },
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Icon(
                        // Usa un ícono temporal - puedes cambiar después
                        imageVector = Icons.AutoMirrored.Filled.Help,
                        contentDescription = "Mostrar tutorial"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Saludo de bienvenida (existente)
            if (userName.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.welcome, userName),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.ready),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(sessions, key = { it.session.sessionId }) { sessionWrapper ->
                    SessionCard(
                        sessionWrapper = sessionWrapper,
                        onClick = { viewModel.onEvent(HomeEvent.SessionClicked(sessionWrapper)) },
                        onLongClick = {
                            viewModel.onEvent(
                                HomeEvent.DeleteSessionRequested(sessionWrapper)
                            )
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}