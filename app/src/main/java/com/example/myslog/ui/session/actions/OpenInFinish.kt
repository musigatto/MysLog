package com.example.myslog.ui.session.actions

import com.example.myslog.ui.ExerciseWrapper
import android.content.Context
import com.example.myslog.core.TipoSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

data class ExerciseVolume(val exerciseName: String, val volume: Int)
data class FinishResult(
    val exerciseVolumes: List<ExerciseVolume>,
    val totalVolume: Int,
    val weeklyHardSets: Int // 👈 contador semanal de hard sets
)


class OpenInFinish(private val context: Context) {

    private val prefs = context.getSharedPreferences("weekly_stats", Context.MODE_PRIVATE)

    suspend fun calculateAndCountHardSets(exercises: List<ExerciseWrapper>): FinishResult = withContext(Dispatchers.IO) {
        val volumes = exercises.map { ex ->
            val vol = ex.sets.sumOf { (it.reps ?: 0) * (it.weight ?: 0f).toInt() }
            ExerciseVolume(ex.exercise.name, vol)
        }
        val total = volumes.sumOf { it.volume }

        // Calcular cuántos hard sets hay en esta sesión
        val hardSetsThisSession = exercises.sumOf { ex ->
            ex.sets.count { it.tipoSet == TipoSet.HARD }
        }

        // Cargar contador actual
        val lastReset = prefs.getString("last_reset_date", null)
        val storedCount = prefs.getInt("hard_sets_this_week", 0)
        val today = LocalDate.now()

        val shouldReset = lastReset == null || LocalDate.parse(lastReset).dayOfWeek.value > today.dayOfWeek.value

        val newTotal = if (shouldReset) {
            prefs.edit()
                .putInt("hard_sets_this_week", hardSetsThisSession)
                .putString("last_reset_date", today.toString())
                .apply()
            hardSetsThisSession
        } else {
            val updated = storedCount + hardSetsThisSession
            prefs.edit()
                .putInt("hard_sets_this_week", updated)
                .putString("last_reset_date", today.toString())
                .apply()
            updated
        }

        FinishResult(volumes, total, newTotal)
    }
}
