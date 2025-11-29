// TutorialDialog.kt
package com.example.myslog.ui.tutorial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myslog.R

@Composable
fun TutorialDialog(
    currentStep: Int,
    totalSteps: Int,
    tutorialType: TutorialType,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    Dialog(onDismissRequest = onSkip) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicador de progreso
                Text(
                    text = "$currentStep de $totalSteps",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Contenido según el tipo de tutorial y paso actual
                when (tutorialType) {
                    TutorialType.HOME -> HomeTutorialContent(currentStep)
                    TutorialType.SESSION -> SessionTutorialContent(currentStep)
                    TutorialType.EXERCISE_PICKER -> ExercisePickerTutorialContent(currentStep)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onSkip) {
                        Text(text = stringResource(R.string.skip))
                    }

                    Button(onClick = onNext) {
                        Text(
                            text = if (currentStep == totalSteps) {
                                stringResource(R.string.finish)
                            } else {
                                stringResource(R.string.accept)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeTutorialContent(currentStep: Int) {
    when (currentStep) {
        1 -> TutorialStep1()
        2 -> TutorialStep2()
        3 -> TutorialStep3()
    }
}

@Composable
private fun SessionTutorialContent(currentStep: Int) {
    when (currentStep) {
        1 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Pantalla de Sesión",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Aquí puedes registrar todos los ejercicios de tu sesión de entrenamiento.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        // Cambia el orden y contenido:
        2 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Temporizador",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Usa el botón ⏱️ para activar el temporizador de descanso entre series.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        3 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Registrar Series",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Toca un ejercicio para expandirlo y registrar series, peso y repeticiones.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        4 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Gestión de Sesión",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Usa el botón \"Finalizar\" para finalizar o 🗑️ para borrar la sesión y volver al inicio.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ExercisePickerTutorialContent(currentStep: Int) {
    when (currentStep) {
        1 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Selector de Ejercicios",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Aquí puedes buscar y seleccionar ejercicios para agregar a tu sesión.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        2 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Filtros Avanzados",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Usa 🏋️ por equipo, 💪 por grupo muscular y 📚 para ejercicios usados anteriormente.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        3 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Workouts Personalizados",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Crea 📋 workouts personalizados seleccionando ejercicios y guardándolos como rutina.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
// Mantenemos los pasos originales del home sin cambios
@Composable
private fun TutorialStep1() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "¡Bienvenido a MySlog!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "MySlog te ayuda a registrar y seguir tus sesiones de entrenamiento de forma sencilla.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TutorialStep2() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Crear Sesiones",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Usa el botón '+' para crear nuevas sesiones de entrenamiento y registrar tus ejercicios.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TutorialStep3() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Gestionar Sesiones",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Toca una sesión para ver detalles o mantén presionada para eliminarla.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
