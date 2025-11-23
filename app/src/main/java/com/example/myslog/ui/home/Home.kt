    package com.example.myslog.ui.home

    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.WindowInsets
    import androidx.compose.foundation.layout.asPaddingValues
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.statusBars
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.AlertDialog
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.SnackbarHost
    import androidx.compose.material3.SnackbarHostState
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextButton
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.rememberCoroutineScope
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.unit.dp
    import androidx.hilt.navigation.compose.hiltViewModel
    import com.example.myslog.R
    import com.example.myslog.ui.home.components.HomeBottomBar
    import com.example.myslog.ui.home.components.SessionCard
    import com.example.myslog.utils.UiEvent
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import timber.log.Timber
    import java.util.Locale

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(
        onNavigate: (UiEvent.Navigate) -> Unit,
        viewModel: HomeViewModel = hiltViewModel()
    ) {
        val sessions by viewModel.sessions.collectAsState()
        val sessionToDelete = viewModel.sessionToDelete
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

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
        val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                HomeBottomBar(onEvent = { event ->
                    when (event) {
                        HomeEvent.CheckUpdates -> {
                            coroutineScope.launch(Dispatchers.IO) {
                                val lang = Locale.getDefault().language
                                Timber.d("Idioma actual del sistema: $lang")


                            }
                        }
                        else -> viewModel.onEvent(event)
                    }
                })
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = topPadding),
                contentPadding = PaddingValues(bottom = 0.dp)
            ) {
                items(sessions, key = { it.session.sessionId }) { sessionWrapper ->
                    SessionCard(
                        sessionWrapper = sessionWrapper,
                        onClick = { viewModel.onEvent(HomeEvent.SessionClicked(sessionWrapper)) },
                        onLongClick = {
                            viewModel.onEvent(
                                HomeEvent.DeleteSessionRequested(
                                    sessionWrapper
                                )
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
