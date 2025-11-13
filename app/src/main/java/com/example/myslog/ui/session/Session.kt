package com.example.myslog.ui.session

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myslog.core.Routes
import com.example.myslog.db.entities.GymSet
import com.example.myslog.db.entities.Session
import com.example.myslog.ui.SessionWrapper
import com.example.myslog.ui.TimerState
import com.example.myslog.ui.session.actions.FinishResult
import com.example.myslog.ui.session.actions.ExerciseSummary
import com.example.myslog.ui.session.components.DeletionAlertDialog
import com.example.myslog.ui.session.components.KeepScreenOnEffect
import com.example.myslog.ui.session.components.SessionPreview
import com.example.myslog.ui.settings.SettingsViewModel
import com.example.myslog.utils.TimerService
import com.example.myslog.utils.UiEvent
import com.example.myslog.utils.sendTimerAction
import timber.log.Timber

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
        timerState = timerState.value
    )

    if (deleteExerciseDialog.value) {
        DeletionAlertDialog(
            onDismiss = { deleteExerciseDialog.value = false },
            onDelete = {
                viewModel.onEvent(SessionEvent.RemoveSelectedExercises)
                deleteExerciseDialog.value = false
            },
            title = { Text("Eliminar ejercicio${if (selectedExercises.size > 1) "s" else ""}?") },
            text = { Text("¿Seguro que deseas eliminar los ejercicios seleccionados de esta sesión?") }
        )
    }

    if (deleteSessionDialog.value) {
        DeletionAlertDialog(
            onDismiss = { deleteSessionDialog.value = false },
            onDelete = {
                viewModel.onEvent(SessionEvent.RemoveSession)
                deleteSessionDialog.value = false
            },
            title = { Text("¿Eliminar sesión?") },
            text = { Text("¿Seguro que deseas eliminar esta sesión y todo su contenido?") }
        )
    }

    if (deleteSetDialog.value != null) {
        deleteSetDialog.value?.let { viewModel.onEvent(SessionEvent.SetDeleted(it)) }
        deleteSetDialog.value = null
    }

    if (finishResult.value != null) {
        AlertDialog(
            onDismissRequest = { finishResult.value = null },
            title = { Text("Sesión terminada 🎉") },
            text = {
                Column {
                    finishResult.value!!.exerciseSummaries.forEach { summary ->
                        Text("${summary.exerciseName}: ${summary.totalSets} series, ${summary.hardSets} al fallo (📅 ${summary.weeklyHardSets} esta semana)")
                    }
                    Spacer(Modifier.height(8.dp))
                    Divider()
                    Spacer(Modifier.height(8.dp))
                    Text("🔥 En total: ${finishResult.value!!.sessionHardSets} sets al fallo en esta sesión.")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    finishResult.value = null
                    onNavigate(UiEvent.Navigate(Routes.HOME, popBackStack = true))
                }) {
                    Text("OK")
                }
            }
        )
    }
}
