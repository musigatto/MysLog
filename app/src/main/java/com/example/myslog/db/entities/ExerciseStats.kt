package com.example.myslog.db.entities

import androidx.room.ColumnInfo

// Para la consulta getExerciseStats
data class ExerciseStats(
    @ColumnInfo(name = "date") val date: java.time.LocalDate,
    @ColumnInfo(name = "maxWeight") val maxWeight: Float,
    @ColumnInfo(name = "totalVolume") val totalVolume: Float,
    @ColumnInfo(name = "totalSets") val totalSets: Int
)

// Para la consulta getExerciseHistory
data class ExerciseHistory(
    @ColumnInfo(name = "sessionExerciseId") val sessionExerciseId: Long,
    @ColumnInfo(name = "parentSessionId") val parentSessionId: Long,
    @ColumnInfo(name = "parentExerciseId") val parentExerciseId: String,
    @ColumnInfo(name = "comment") val comment: String?,
    @ColumnInfo(name = "sessionStart") val sessionStart: java.time.LocalDateTime,
    @ColumnInfo(name = "sessionEnd") val sessionEnd: java.time.LocalDateTime?,
    @ColumnInfo(name = "setId") val setId: Long,
    @ColumnInfo(name = "reps") val reps: Int?,
    @ColumnInfo(name = "weight") val weight: Float?,
    @ColumnInfo(name = "tipoSet") val tipoSet: Int
)