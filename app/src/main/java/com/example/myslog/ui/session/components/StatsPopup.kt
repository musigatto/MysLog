package com.example.myslog.ui.session.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import com.example.myslog.R

data class StatEntry(
    val date: java.time.LocalDate,
    val pesoMax: Float,
    val volumeTotal: Float = 0f,
    val totalSets: Int = 0
)

@Composable
fun StatsPopup(
    stats: List<StatEntry>,
    exerciseName: String = "",
    onDismiss: () -> Unit
) {
    var selectedMetric by remember { mutableStateOf(0) }
    val metrics = listOf(stringResource(R.string.max_weight),
        stringResource(R.string.total_volumen)
    )
    val formatter = DateTimeFormatter.ofPattern("dd/MM")
    val lineColor = MaterialTheme.colorScheme.primary
    val axisColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val textColor = MaterialTheme.colorScheme.onSurface

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(stringResource(R.string.stats))
                if (exerciseName.isNotEmpty()) {
                    Text(
                        text = exerciseName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(8.dp)
            ) {
                if (stats.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_data))
                    }
                } else {
                    // Selector de métrica
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        metrics.forEachIndexed { index, metric ->
                            TextButton(
                                onClick = { selectedMetric = index }
                            ) {
                                Text(
                                    metric,
                                    color = if (selectedMetric == index)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Gráfica
                    Canvas(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp)) {

                        val padding = 60f
                        val graphWidth = size.width - padding * 2
                        val graphHeight = size.height - padding * 2

                        // Seleccionar datos según métrica
                        val dataValues = stats.map {
                            if (selectedMetric == 0) it.pesoMax else it.volumeTotal
                        }
                        val maxValue = dataValues.maxOrNull() ?: 1f
                        val minValue = dataValues.minOrNull() ?: 0f
                        val valueRange = (maxValue - minValue).coerceAtLeast(1f)

                        // Dibujar ejes
                        drawLine(
                            color = axisColor,
                            start = Offset(padding, padding),
                            end = Offset(padding, padding + graphHeight),
                            strokeWidth = 2f
                        )
                        drawLine(
                            color = axisColor,
                            start = Offset(padding, padding + graphHeight),
                            end = Offset(padding + graphWidth, padding + graphHeight),
                            strokeWidth = 2f
                        )

                        val xLabelPadding = 20f
                        val points = stats.mapIndexed { index, stat ->
                            val x = padding + xLabelPadding + index * ((graphWidth - xLabelPadding) / (stats.size - 1).coerceAtLeast(1))
                            val value = if (selectedMetric == 0) stat.pesoMax else stat.volumeTotal
                            val y = padding + (1 - (value - minValue) / valueRange) * graphHeight
                            Offset(x, y)
                        }

                        // Dibujar líneas y puntos
                        for (i in 0 until points.size - 1) {
                            drawLine(color = lineColor, start = points[i], end = points[i + 1], strokeWidth = 4f)
                        }
                        points.forEach { point ->
                            drawCircle(color = lineColor, radius = 6f, center = point)
                        }

                        // Etiquetas Y (valores)
                        val steps = 5
                        drawContext.canvas.nativeCanvas.apply {
                            val paint = android.graphics.Paint().apply {
                                color = textColor.toArgb()
                                textSize = 32f
                                textAlign = android.graphics.Paint.Align.RIGHT
                            }
                            for (i in 0..steps) {
                                val value = minValue + i * (maxValue - minValue) / steps
                                val y = padding + (1 - i.toFloat() / steps) * graphHeight
                                drawText("%.0f".format(value), padding - 10, y + 10, paint)
                            }
                        }

                        // Etiquetas X (fechas)
                        drawContext.canvas.nativeCanvas.apply {
                            val paint = android.graphics.Paint().apply {
                                color = textColor.toArgb()
                                textSize = 30f
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                            points.forEachIndexed { index, point ->
                                if (index % 2 == 0) { // Mostrar solo algunas fechas para evitar saturación
                                    val dateLabel = stats[index].date.format(formatter)
                                    drawText(dateLabel, point.x, padding + graphHeight + 30, paint)
                                }
                            }
                        }
                    }

                    // Información resumida
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(stringResource(R.string.best_weight), style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${stats.maxOf { it.pesoMax }.toInt()}kg",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(stringResource(R.string.best_total_volumen), style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${stats.maxOf { it.volumeTotal }.toInt()}kg",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(stringResource(R.string.sessions), style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${stats.size}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}