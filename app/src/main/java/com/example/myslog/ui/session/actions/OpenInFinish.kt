package com.example.myslog.ui.session.actions

import com.example.myslog.ui.ExerciseWrapper

data class ExerciseVolume(val exerciseName: String, val volume: Int)
data class FinishResult(
    val exerciseVolumes: List<ExerciseVolume>,
    val totalVolume: Int,
    val funFact: String
)

class OpenInFinish {

    suspend fun calculateAndFetchFact(exercises: List<ExerciseWrapper>): FinishResult {
        val volumes = exercises.map { ex ->
            val vol = ex.sets.sumOf { (it.reps ?: 0) * (it.weight ?: 0f).toInt() }
            ExerciseVolume(ex.exercise.name, vol)
        }
        val total = volumes.sumOf { it.volume }


        return FinishResult(volumes, total, "")
    }
}
