// TutorialState.kt
package com.example.myslog.ui.tutorial

data class TutorialState(
    val currentStep: Int = 0,
    val isTutorialCompleted: Boolean = false,
    val showTutorial: Boolean = false
)