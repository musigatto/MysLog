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
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    app: Application,
    private val repo: MysRepository,
) : AndroidViewModel(app) {

    private val prefs: SharedPreferences =
        app.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) // CAMBIO: usar user_prefs

    private val _keepScreenOn = MutableStateFlow(prefs.getBoolean("keep_screen_on", false))
    val keepScreenOn: StateFlow<Boolean> = _keepScreenOn

    // NUEVO: Estado para el nombre de usuario
    private val _userName = MutableStateFlow(prefs.getString("user_name", "") ?: "")
    val userName: StateFlow<String> = _userName

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: Event) {
        when (event) {
            is SettingsEvent.ImportDatabase -> {
                viewModelScope.launch(Dispatchers.IO) {
                    // importDatabase(event.uri, event.context)
                }
            }

            is SettingsEvent.ExportDatabase -> {
                viewModelScope.launch(Dispatchers.IO) {
                    // exportDatabase(event.uri, event.context)
                }
            }

            is SettingsEvent.CreateFile -> {
                // sendUiEvent(UiEvent.FileCreated("workout_db.json"))
            }

            is SettingsEvent.ClearDatabase -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repo.clearDatabase()
                    Timber.d("Database cleared")
                    _uiEvent.send(UiEvent.Navigate(Routes.HOME))
                }
            }

            is SettingsEvent.ToggleKeepScreenOn -> {
                _keepScreenOn.value = event.enabled
                prefs.edit { putBoolean("keep_screen_on", event.enabled) }
                Timber.d("KeepScreenOn set to ${event.enabled}")
            }

            // NUEVO: Manejar actualización de nombre
            is SettingsEvent.UpdateUserName -> {
                _userName.value = event.name
                prefs.edit { putString("user_name", event.name) }
                Timber.d("UserName updated to: ${event.name}")
            }
        }
    }
}