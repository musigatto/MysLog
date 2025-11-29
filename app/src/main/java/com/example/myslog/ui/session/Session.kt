package com.example.myslog.ui.session

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myslog.R
import com.example.myslog.core.Routes
import com.example.myslog.db.entities.GymSet
import com.example.myslog.db.entities.Session
import com.example.myslog.ui.SessionWrapper
import com.example.myslog.ui.TimerState
import com.example.myslog.ui.session.actions.FinishResult
import com.example.myslog.ui.session.components.DeletionAlertDialog
import com.example.myslog.ui.session.components.KeepScreenOnEffect
import com.example.myslog.ui.session.components.SessionPreview
import com.example.myslog.ui.settings.SettingsViewModel
import com.example.myslog.ui.tutorial.TutorialDialog
import com.example.myslog.ui.tutorial.TutorialType
import com.example.myslog.ui.tutorial.TutorialViewModel
import com.example.myslog.utils.TimerService
import com.example.myslog.utils.UiEvent
import com.example.myslog.utils.sendTimerAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: SessionViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val keepScreenOn by settingsViewModel.keepScreenOn.collectAsState()

    // Solicitud de permisos para notificaciones
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Manejar el resultado si es necesario
        if (!isGranted) {
            // Opcional: mostrar mensaje al usuario sobre la importancia del permiso
        }
    }

    KeepScreenOnEffect(keepScreenOn)

    val session by viewModel.session.collectAsState(SessionWrapper(Session(), emptyList()))
    val exercises by viewModel.exercises.collectAsState(initial = emptyList())
    val expandedExercise by viewModel.expandedExercise.collectAsState()
    val selectedExercises by viewModel.selectedExercises.collectAsState()
    val muscleGroups by viewModel.muscleGroups.collectAsState(emptyList())

    val deleteExerciseDialog = remember { mutableStateOf(false) }
    val deleteSessionDialog = remember { mutableStateOf(false) }
    val deleteSetDialog = remember { mutableStateOf<GymSet?>(null) }
    val timerVisible = remember { mutableStateOf(false) }
    val timerState = remember { mutableStateOf(TimerState(0L, false, 0L)) }
    val finishResult = remember { mutableStateOf<FinishResult?>(null) }
// En SessionScreen.kt, agregar después de las declaraciones de viewModel:
    val tutorialViewModel: TutorialViewModel = hiltViewModel()
    val tutorialState by tutorialViewModel.tutorialState.collectAsStateWithLifecycle()

// Mostrar diálogo de tutorial si está activo
    if (tutorialState.showTutorial && tutorialState.tutorialType == TutorialType.SESSION) {
        TutorialDialog(
            currentStep = tutorialState.currentStep,
            totalSteps = TutorialViewModel.SESSION_TUTORIAL_STEPS,
            tutorialType = tutorialState.tutorialType,
            onNext = { tutorialViewModel.nextStep() },
            onSkip = { tutorialViewModel.skipTutorial() }
        )
    }

    // Solicitar permisos cuando se entra a la pantalla de sesión
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    val timeReceived = it.getLongExtra(TimerService.Intents.Extras.TIME.toString(), -1L)
                    val runningReceived = it.getBooleanExtra(TimerService.Intents.Extras.IS_RUNNING.toString(), false)
                    val maxTimeReceived = it.getLongExtra(TimerService.Intents.Extras.MAX_TIME.toString(), -1L)
                    timerState.value = TimerState(
                        time = if (timeReceived == -1L) 0L else timeReceived,
                        running = runningReceived,
                        maxTime = if (maxTimeReceived == -1L) 0L else maxTimeReceived
                    )
                }
            }
        }

        val filter = IntentFilter(TimerService.Intents.STATUS.toString())
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            context.registerReceiver(receiver, filter)
        }

        context.sendTimerAction(TimerService.Actions.QUERY)
        onDispose { context.unregisterReceiver(receiver) }
    }

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.OpenWebsite -> uriHandler.openUri(event.url)
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ToggleTimer -> context.sendTimerAction(TimerService.Actions.TOGGLE)
                is UiEvent.ResetTimer -> context.sendTimerAction(TimerService.Actions.RESET)
                is UiEvent.IncrementTimer -> context.sendTimerAction(TimerService.Actions.INCREMENT)
                is UiEvent.DecrementTimer -> context.sendTimerAction(TimerService.Actions.DECREMENT)
                is UiEvent.ShowFinishResult -> finishResult.value = event.result
                else -> Unit
            }
        }
    }

        SessionPreview(
            session = session,
            exercises = exercises,
            expandedExercise = expandedExercise,
            selectedExercises = selectedExercises,
            muscleGroups = muscleGroups,
            onEvent = viewModel::onEvent,
            onNavigate = onNavigate,
            deleteExerciseDialog = deleteExerciseDialog,
            deleteSessionDialog = deleteSessionDialog,
            deleteSetDialog = deleteSetDialog,
            timerVisible = timerVisible,
            timerState = timerState.value,
            onTutorialClick = { tutorialViewModel.startTutorial(TutorialType.SESSION) }
        )

    if (deleteExerciseDialog.value) {
        DeletionAlertDialog(
            onDismiss = { deleteExerciseDialog.value = false },
            onDelete = {
                viewModel.onEvent(SessionEvent.RemoveSelectedExercises)
                deleteExerciseDialog.value = false
            },
            title = { Text(stringResource(R.string.delete_selected_ex)) },
            text = { Text(stringResource(R.string.sure_about_ex_del)) }
        )
    }

    if (deleteSessionDialog.value) {
        DeletionAlertDialog(
            onDismiss = { deleteSessionDialog.value = false },
            onDelete = {
                viewModel.onEvent(SessionEvent.RemoveSession)
                deleteSessionDialog.value = false
            },
            title = { Text(stringResource(R.string.delete_ses)) },
            text = { Text(stringResource(R.string.sure_bout_sess_del)) }
        )
    }

    if (deleteSetDialog.value != null) {
        deleteSetDialog.value?.let { viewModel.onEvent(SessionEvent.SetDeleted(it)) }
        deleteSetDialog.value = null
    }

    if (finishResult.value != null) {
        AlertDialog(
            onDismissRequest = { finishResult.value = null },
            title = { Text(stringResource(R.string.finished_sess)) },
            text = {
                Column {
                    finishResult.value!!.exerciseSummaries.forEach { summary ->
                        Text(
                            stringResource(
                                R.string.this_week,
                                summary.exerciseName,
                                summary.totalSets,
                                summary.hardSets,
                                summary.weeklyHardSets
                            ))
                    }
                    Spacer(Modifier.height(8.dp))
                    Divider()
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.total_sets, finishResult.value!!.sessionHardSets))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    finishResult.value = null
                    onNavigate(UiEvent.Navigate(Routes.HOME, popBackStack = true))
                }) {
                    Text(stringResource(R.string.accept))
                }
            }
        )
    }
}