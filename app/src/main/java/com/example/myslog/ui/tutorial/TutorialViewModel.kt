package com.example.myslog.ui.tutorial

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TutorialViewModel : ViewModel() {
    private val _tutorialState = MutableStateFlow(TutorialState())
    val tutorialState: StateFlow<TutorialState> = _tutorialState.asStateFlow()

    fun startTutorial(tutorialType: TutorialType = TutorialType.HOME) {
        _tutorialState.value = TutorialState(
            currentStep = 1,
            isTutorialCompleted = false,
            showTutorial = true,
            tutorialType = tutorialType
        )
    }

    fun nextStep() {
        val current = _tutorialState.value.currentStep
        val tutorialType = _tutorialState.value.tutorialType
        val totalSteps = when (tutorialType) {
            TutorialType.HOME -> HOME_TUTORIAL_STEPS
            TutorialType.SESSION -> SESSION_TUTORIAL_STEPS
            TutorialType.EXERCISE_PICKER -> EXERCISE_PICKER_TUTORIAL_STEPS
        }

        if (current < totalSteps) {
            _tutorialState.value = _tutorialState.value.copy(
                currentStep = current + 1
            )
        } else {
            completeTutorial()
        }
    }

    fun skipTutorial() {
        completeTutorial()
    }

    private fun completeTutorial() {
        _tutorialState.value = TutorialState(
            currentStep = 0,
            isTutorialCompleted = true,
            showTutorial = false,
            tutorialType = _tutorialState.value.tutorialType
        )
    }

    companion object {
        const val HOME_TUTORIAL_STEPS = 3
        const val SESSION_TUTORIAL_STEPS = 4
        const val EXERCISE_PICKER_TUTORIAL_STEPS = 3
    }
}

