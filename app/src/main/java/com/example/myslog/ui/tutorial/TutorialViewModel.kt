// TutorialViewModel.kt
package com.example.myslog.ui.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TutorialViewModel : ViewModel() {
    private val _tutorialState = MutableStateFlow(TutorialState())
    val tutorialState: StateFlow<TutorialState> = _tutorialState.asStateFlow()

    fun startTutorial() {
        _tutorialState.value = TutorialState(
            currentStep = 1,
            isTutorialCompleted = false,
            showTutorial = true
        )
    }

    fun nextStep() {
        val current = _tutorialState.value.currentStep
        if (current < TOTAL_TUTORIAL_STEPS) {
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
            showTutorial = false
        )
    }

    companion object {
        const val TOTAL_TUTORIAL_STEPS = 3
    }
}