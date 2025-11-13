package com.example.myslog

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.myslog.db.MysDAO
import com.example.myslog.ui.AppNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

//TODO: capitalizar json y traducir con googleSheets
// Anotación para habilitar la inyección de dependencias con Hilt en esta actividad
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dao: MysDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )

        if (BuildConfig.DEBUG) Timber.plant(DebugTree())

        setContent {
            AppTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
                    )
                }

                val context = LocalContext.current
                val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

                // Flag si ya aceptó los términos
                var showDialog by remember { mutableStateOf(!prefs.getBoolean("terms_accepted", false)) }

                // Texto plano cargado desde assets
                var termsText by remember { mutableStateOf("Cargando términos...") }

                LaunchedEffect(Unit) {
                    termsText = withContext(Dispatchers.IO) {
                        context.assets.open("terms.txt").bufferedReader().use { it.readText() }
                    }
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            // Forzamos que la app se cierre si intenta salir del diálogo
                            finish()
                        },
                        title = { Text("Términos y condiciones") },
                        text = {
                            Column(
                                Modifier
                                    .fillMaxHeight(0.6f)
                                    .verticalScroll(rememberScrollState())
                                    .padding(8.dp)
                            ) {
                                Text(text = termsText)
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                prefs.edit().putBoolean("terms_accepted", true).apply()
                                showDialog = false
                            }) {
                                Text("Aceptar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                finish() // Cierra la app si no acepta
                            }) {
                                Text("Cerrar")
                            }
                        }
                    )
                } else {
                    val navController = rememberNavController()
                    Surface {
                        AppNavHost(navController = navController)
                    }
                }
            }
        }
    }
}
