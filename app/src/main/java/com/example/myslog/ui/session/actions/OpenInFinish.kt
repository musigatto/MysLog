package com.example.myslog.ui.session.actions

import android.content.Context
import com.example.myslog.core.TipoSet
import com.example.myslog.ui.ExerciseWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

data class ExerciseSummary(
    val exerciseName: String,
    val totalSets: Int,
    val hardSets: Int,
    val weeklyHardSets: Int
)

data class FinishResult(
    val exerciseSummaries: List<ExerciseSummary>,
    val sessionHardSets: Int
)

class OpenInFinish(private val context: Context) {

    private val prefs = context.getSharedPreferences("weekly_stats", Context.MODE_PRIVATE)

    suspend fun calculateAndCountHardSets(exercises: List<ExerciseWrapper>): FinishResult = withContext(Dispatchers.IO) {
        val today = LocalDate.now()
        val summaries = exercises.map { ex ->
            val totalSets = ex.sets.size
            val hardSets = ex.sets.count { it.tipoSet == TipoSet.HARD }


            val key = "weekly_hard_${ex.exercise.id}"
            val lastReset = prefs.getString("${key}_last_reset", null)
            val storedCount = prefs.getInt(key, 0)

            val shouldReset = lastReset == null || LocalDate.parse(lastReset).dayOfWeek.value > today.dayOfWeek.value

            val newWeekly = if (shouldReset) {
                prefs.edit()
                    .putInt(key, hardSets)
                    .putString("${key}_last_reset", today.toString())
                    .apply()
                hardSets
            } else {
                val updated = storedCount + hardSets
                prefs.edit()
                    .putInt(key, updated)
                    .putString("${key}_last_reset", today.toString())
                    .apply()
                updated
            }

            ExerciseSummary(
                exerciseName = ex.exercise.name,
                totalSets = totalSets,
                hardSets = hardSets,
                weeklyHardSets = newWeekly
            )
        }

        val sessionHardSets = summaries.sumOf { it.hardSets }

        FinishResult(summaries, sessionHardSets)
    }
}
