    package com.example.myslog.ui.session.actions

    import android.content.Context
    import androidx.core.content.edit
    import com.example.myslog.R
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
        val sessionHardSets: Int,
        val markdownSummary: String
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
                    prefs.edit {
                        putInt(key, hardSets)
                        putString("${key}_last_reset", today.toString())
                    }
                    hardSets
                } else {
                    val updated = storedCount + hardSets
                    prefs.edit {
                        putInt(key, updated)
                        putString("${key}_last_reset", today.toString())
                    }
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

            // Generar resumen en Markdown usando plantillas
            val markdownSummary = generateMarkdownSummary(summaries, sessionHardSets)

            FinishResult(summaries, sessionHardSets, markdownSummary)
        }

        private fun generateMarkdownSummary(
            summaries: List<ExerciseSummary>,
            sessionHardSets: Int
        ): String {
            val resources = context.resources

            return buildString {
                // Total de sets duros
                appendLine(resources.getString(R.string.total_hard_sets, sessionHardSets))
                appendLine()

                if (summaries.isNotEmpty()) {
                    // Encabezado de ejercicios
                    appendLine(resources.getString(R.string.exercises_done))
                    appendLine()

                    // Lista de ejercicios
                    summaries.forEach { summary ->
                        appendLine(
                            resources.getString(
                                R.string.exercise_summary_item,
                                summary.exerciseName,
                                summary.totalSets,
                                summary.hardSets,
                                summary.weeklyHardSets
                            )
                        )
                        appendLine()
                    }

                    // Tabla resumen
                    appendLine(resources.getString(R.string.summary_table))
                    appendLine(resources.getString(R.string.table_header))

                    // Filas de la tabla
                    summaries.forEach { summary ->
                        appendLine(
                            resources.getString(
                                R.string.table_row,
                                summary.exerciseName,
                                summary.totalSets,
                                summary.hardSets,
                                summary.weeklyHardSets
                            )
                        )
                    }
                } else {
                    // Mensaje cuando no hay ejercicios
                    appendLine(resources.getString(R.string.no_exercises))
                }

                appendLine()
                appendLine("---")
                appendLine(resources.getString(R.string.good_job))
            }
        }
    }