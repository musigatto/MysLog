package com.example.myslog.ui.tutorial

data class TutorialState(
    val currentStep: Int = 0,
    val isTutorialCompleted: Boolean = false,
    val showTutorial: Boolean = false,
    val tutorialType: TutorialType = TutorialType.HOME
)

enum class TutorialType {
    HOME, SESSION, EXERCISE_PICKER
}