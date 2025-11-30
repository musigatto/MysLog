package com.example.myslog.ui.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myslog.core.Routes
import com.example.myslog.db.repository.MysRepository
import com.example.myslog.utils.Event
import com.example.myslog.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    app: Application,
    private val repo: MysRepository,
) : AndroidViewModel(app) {

    private val prefs: SharedPreferences =
        app.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val _keepScreenOn = MutableStateFlow(prefs.getBoolean("keep_screen_on", false))
    val keepScreenOn: StateFlow<Boolean> = _keepScreenOn

    private val _userName = MutableStateFlow(prefs.getString("user_name", "") ?: "")
    val userName: StateFlow<String> = _userName

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: Event) {
        when (event) {

            // ==========================================
            // EXPORTAR DB (no cerrar Room)
            // ==========================================
            is SettingsEvent.ExportDatabase -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val context = event.context
                    val uri = event.uri
                    try {
                        val dbDir = context.getDatabasePath("mys_db").parentFile!!
                        val dbFiles = listOf("mys_db", "mys_db-wal", "mys_db-shm")

                        context.contentResolver.openOutputStream(uri)?.use { output ->
                            ZipOutputStream(output).use { zip ->
                                dbFiles.forEach { fileName ->
                                    val file = File(dbDir, fileName)
                                    if (file.exists()) {
                                        zip.putNextEntry(ZipEntry(fileName))
                                        file.inputStream().use { input -> input.copyTo(zip) }
                                        zip.closeEntry()
                                    }
                                }
                            }
                        }
                        Timber.d("Database exported successfully")
                    } catch (e: Exception) {
                        Timber.e(e, "Error exporting DB")
                    }
                }
            }

            // ==========================================
            // IMPORTAR DB (sobreescribir archivos + navegar a HOME)
            // ==========================================
            is SettingsEvent.ImportDatabase -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val context = event.context
                    val uri = event.uri
                    try {
                        val dbDir = context.getDatabasePath("mys_db").parentFile!!
                        val dbFiles = listOf("mys_db", "mys_db-wal", "mys_db-shm")

                        context.contentResolver.openInputStream(uri)?.use { input ->
                            ZipInputStream(input).use { zip ->
                                var entry: ZipEntry? = zip.nextEntry
                                while (entry != null) {
                                    if (entry.name in dbFiles) {
                                        val outFile = File(dbDir, entry.name)
                                        FileOutputStream(outFile).use { out -> zip.copyTo(out) }
                                    }
                                    zip.closeEntry()
                                    entry = zip.nextEntry
                                }
                            }
                        }

                        Timber.d("Database imported successfully")

                        // Ir a HOME para reinyectar Room
                        _uiEvent.trySend(UiEvent.Navigate(Routes.HOME))

                    } catch (e: Exception) {
                        Timber.e(e, "Error importing DB")
                    }
                }
            }

            // ==========================================
            // CREAR FILE PARA EXPORTAR
            // ==========================================
            is SettingsEvent.CreateFile -> {
                _uiEvent.trySend(UiEvent.FileCreated("myslog_backup.zip"))
            }

            // ==========================================
            // BORRAR DB (sobreescribir con DB vacía)
            // ==========================================
            is SettingsEvent.ClearDatabase -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repo.clearDatabase()
                    Timber.d("Database cleared")
                    _uiEvent.trySend(UiEvent.Navigate(Routes.HOME))
                }
            }

            // ==========================================
            // TOGGLE PREFERENCIAS
            // ==========================================
            is SettingsEvent.ToggleKeepScreenOn -> {
                _keepScreenOn.value = event.enabled
                prefs.edit { putBoolean("keep_screen_on", event.enabled) }
                Timber.d("KeepScreenOn set to ${event.enabled}")
            }

            // ==========================================
            // ACTUALIZAR NOMBRE USUARIO
            // ==========================================
            is SettingsEvent.UpdateUserName -> {
                _userName.value = event.name
                prefs.edit { putString("user_name", event.name) }
                Timber.d("UserName updated to: ${event.name}")
            }
        }
    }
}
