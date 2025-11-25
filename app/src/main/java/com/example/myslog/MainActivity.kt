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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.myslog.db.MysDAO
import com.example.myslog.ui.AppNavHost
import dagger.hilt.android.AndroidEntryPoint
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject
import androidx.core.content.edit

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
                val context = LocalContext.current
                val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

                // Estados para el flujo
                var showTermsDialog by remember {
                    mutableStateOf(!prefs.getBoolean("terms_accepted", false))
                }
                var showNameDialog by remember {
                    mutableStateOf(
                        prefs.getBoolean("terms_accepted", false) &&
                                !prefs.getBoolean("name_dialog_shown", false)
                    )
                }

                // Leer Markdown desde res/raw/terms.md
                var terms by remember { mutableStateOf("Loading...") }

                LaunchedEffect(Unit) {
                    terms = withContext(Dispatchers.IO) {
                        context.resources.openRawResource(R.raw.terms)
                            .bufferedReader().use { it.readText() }
                    }
                }

                // Flujo: Términos -> Nombre -> Home
                if (showTermsDialog) {
                    AlertDialog(
                        onDismissRequest = {     prefs.edit {
                            putString("user_name", "")
                                .putBoolean("name_dialog_shown", true)
                        }
                            showNameDialog = false },
                        title = { Text(stringResource(R.string.code_conduct)) },
                        text = {
                            Column(
                                Modifier
                                    .fillMaxHeight(0.6f)
                                    .verticalScroll(rememberScrollState())
                                    .padding(8.dp)
                            ) {
                                MarkdownText(markdown = terms)
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                prefs.edit { putBoolean("terms_accepted", true) }
                                showTermsDialog = false
                                showNameDialog = true
                            }) {
                                Text(stringResource(R.string.accept))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { finish() }) {
                                Text(stringResource(R.string.cancel))
                            }
                        }
                    )
                } else if (showNameDialog) {
                    var name by remember { mutableStateOf("") }

                    AlertDialog(
                        onDismissRequest = {
                            prefs.edit { putString("user_name", "") }
                            showNameDialog = false
                        },
                        title = { Text("¡Bienvenido!") },
                        text = {
                            Column {
                                Text(stringResource(R.string.ask_name))
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    label = { Text(stringResource(R.string.name_placeholder)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    prefs.edit {
                                        putString("user_name", name)
                                            .putBoolean("name_dialog_shown", true)
                                    }
                                    showNameDialog = false
                                }
                            ) {
                                Text(stringResource(R.string.continue_))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    prefs.edit {
                                        putString("user_name", "")
                                            .putBoolean("name_dialog_shown", true)
                                    }
                                    showNameDialog = false
                                }
                            ) {
                                Text(stringResource(R.string.skip))
                            }
                        }
                    )
                } else {
                    val navController = rememberNavController()
                    val userName = prefs.getString("user_name", "") ?: ""

                    Surface {
                        AppNavHost(
                            navController = navController,
                            userName = userName
                        )
                    }
                }
            }
        }
    }
}