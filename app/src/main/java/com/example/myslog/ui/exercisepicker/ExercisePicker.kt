package com.example.myslog.ui.exercisepicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myslog.db.entities.Exercise
import com.example.myslog.ui.exercisepicker.components.EquipmentSheet
import com.example.myslog.ui.exercisepicker.components.ExercisePickerPreview
import com.example.myslog.ui.exercisepicker.components.ImagePopup
import com.example.myslog.ui.exercisepicker.components.MuscleSheet
import com.example.myslog.ui.session.components.StatEntry
import com.example.myslog.ui.session.components.StatsPopup
import com.example.myslog.ui.tutorial.TutorialDialog
import com.example.myslog.ui.tutorial.TutorialType
import com.example.myslog.ui.tutorial.TutorialViewModel
import com.example.myslog.utils.UiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisePicker(
    navController: NavController,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    // Estados de UI
    val exercises by viewModel.filteredExercises.collectAsState(initial = emptyList())
    val selectedExercises by viewModel.selectedExercises.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val equipmentFilter by viewModel.equipmentFilter.collectAsState()
    val muscleFilter by viewModel.muscleFilter.collectAsState()
    val allEquipment by viewModel._allEquipment.collectAsState()
    val allMusclesList by viewModel._allMuscles.collectAsState()
    val filterSelected by viewModel.filterSelected.collectAsState()
    val filterUsed by viewModel.filterUsed.collectAsState()
    val workoutFilter by viewModel.workoutFilter.collectAsState()
    val allWorkouts by viewModel.allWorkouts.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var equipmentBottomsheet by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    var showPopupExerciseId by remember { mutableStateOf<String?>(null) }
    var statsPopupSets by remember { mutableStateOf<List<StatEntry>?>(null) }
    var statsPopupData by remember {
        mutableStateOf<Pair<List<StatEntry>, String>?>(null)
    }

    val tutorialViewModel: TutorialViewModel = hiltViewModel()
    val tutorialState by tutorialViewModel.tutorialState.collectAsStateWithLifecycle()

    // Mostrar diálogo de tutorial si está activo
    if (tutorialState.showTutorial && tutorialState.tutorialType == TutorialType.EXERCISE_PICKER) {
        TutorialDialog(
            currentStep = tutorialState.currentStep,
            totalSteps = TutorialViewModel.EXERCISE_PICKER_TUTORIAL_STEPS,
            tutorialType = tutorialState.tutorialType,
            onNext = { tutorialViewModel.nextStep() },
            onSkip = { tutorialViewModel.skipTutorial() }
        )
    }

    statsPopupData?.let { (stats, exerciseName) ->
        StatsPopup(
            stats = stats,
            exerciseName = exerciseName,
            onDismiss = { statsPopupData = null }
        )
    }

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowImagePopup -> {
                    showPopupExerciseId = event.exerciseId
                    keyboardController?.hide()
                }

                is UiEvent.ShowStatsPopup -> {
                    statsPopupData = Pair(event.stats, event.exerciseName)
                    keyboardController?.hide()
                }

                else -> Unit
            }
        }
    }
        // Contenido principal
        ExercisePickerPreview(
            exercises = exercises,
            selectedExercises = selectedExercises,
            onAddClick = {
                viewModel.onEvent(ExerciseEvent.AddExercises)
                navController.popBackStack()
            },
            onExerciseClick = { exercise ->
                viewModel.onEvent(ExerciseEvent.ExerciseSelected(exercise))
            },
            onSearchChanged = { text -> viewModel.onEvent(ExerciseEvent.SearchChanged(text)) },
            searchText = searchText,
            onEvent = viewModel::onEvent,
            onFilterSelectedClick = { viewModel.onEvent(ExerciseEvent.FilterSelected) },
            onFilterUsedClick = { viewModel.onEvent(ExerciseEvent.FilterUsed) },
            onMuscleFilterClick = {
                equipmentBottomsheet = false
                coroutineScope.launch {
                    keyboardController?.hide()
                    sheetState.show()
                }
            },
            onEquipmentFilterClick = {
                equipmentBottomsheet = true
                coroutineScope.launch {
                    keyboardController?.hide()
                    sheetState.show()
                }
            },
            filterSelected = filterSelected,
            filterUsed = filterUsed,
            muscleFilterActive = muscleFilter.isNotEmpty(),
            equipmentFilterActive = equipmentFilter.isNotEmpty(),
            workoutFilter = workoutFilter,
            allWorkouts = allWorkouts,
            onTutorialClick = { tutorialViewModel.startTutorial(TutorialType.EXERCISE_PICKER) }
        )


    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { coroutineScope.launch { sheetState.hide() } },
            sheetState = sheetState
        ) {
            if (equipmentBottomsheet) {
                EquipmentSheet(
                    selectedEquipment = equipmentFilter,
                    allEquipment = allEquipment,
                    onEvent = viewModel::onEvent
                )
            } else {
                MuscleSheet(
                    selectedMusclegroups = muscleFilter,
                    allMuscleGroups = allMusclesList,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }

    // Popup de imagen del ejercicio
    if (showPopupExerciseId != null) {
        val exercise: Exercise? = exercises.find { it.id == showPopupExerciseId }
        if (exercise != null) {
            ImagePopup(
                exercise = exercise,
                onDismiss = { showPopupExerciseId = null }
            )
        }
    }
}