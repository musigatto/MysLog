// TutorialDialog.kt
package com.example.myslog.ui.tutorial

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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

                // Contenido según el paso actual
                when (currentStep) {
                    1 -> TutorialStep1()
                    2 -> TutorialStep2()
                    3 -> TutorialStep3()
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