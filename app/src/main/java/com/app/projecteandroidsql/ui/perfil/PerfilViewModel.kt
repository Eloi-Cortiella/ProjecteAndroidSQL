package com.app.projecteandroidsql.ui.perfil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiState(
    val name: String = "Nom Exemple",
    val email: String = "correu@example.com",
    val username: String = "usuari123",
    val phone: String = "+34 600 000 000",
    val bio: String = "Aquí pots escriure una breu descripció sobre tu.",
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val isEditing: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class PerfilViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = PerfilPreferences(app.applicationContext)

    private val _uiState = MutableStateFlow(PerfilUiState(isLoading = true))
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    // Snapshot de l’últim estat persistit (per Cancel·lar)
    private var persistedSnapshot: PerfilUiState = PerfilUiState(isLoading = true)

    init {
        // Observem DataStore i omplim la UI
        viewModelScope.launch {
            prefs.flow.collect { p ->
                val persisted = PerfilUiState(
                    name = p.name,
                    email = p.email,
                    username = p.username,
                    phone = p.phone,
                    bio = p.bio,
                    notificationsEnabled = p.notificationsEnabled,
                    darkModeEnabled = p.darkModeEnabled,
                    isEditing = false,
                    isLoading = false,
                    errorMessage = null
                )
                persistedSnapshot = persisted

                // Si no estem editant, refresquem-ho tot.
                // Si estem editant, no trepitgem el que l’usuari està escrivint.
                if (!_uiState.value.isEditing) {
                    _uiState.value = persisted
                } else {
                    _uiState.update {
                        it.copy(
                            notificationsEnabled = p.notificationsEnabled,
                            darkModeEnabled = p.darkModeEnabled,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            }
        }
    }

    // --- Camps editables ---
    fun onNameChange(v: String) = _uiState.update { it.copy(name = v) }
    fun onUsernameChange(v: String) = _uiState.update { it.copy(username = v) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v) }
    fun onBioChange(v: String) = _uiState.update { it.copy(bio = v) }

    // --- Preferències (es persisteixen al moment) ---
    fun onNotificationsChange(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
        viewModelScope.launch {
            runCatching { prefs.setNotifications(enabled) }
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
        }
    }

    fun onDarkModeChange(enabled: Boolean) {
        _uiState.update { it.copy(darkModeEnabled = enabled) }
        viewModelScope.launch {
            runCatching { prefs.setDarkMode(enabled) }
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
        }
    }

    // --- Mode edició ---
    fun startEditing() {
        _uiState.update { it.copy(isEditing = true, errorMessage = null) }
    }

    fun cancelEditing() {
        // Tornem al snapshot guardat (persistit)
        _uiState.value = persistedSnapshot
    }

    fun saveChanges() {
        val cur = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                prefs.saveProfile(
                    name = cur.name,
                    email = cur.email, // si no vols editable, això igualment ho manté
                    username = cur.username,
                    phone = cur.phone,
                    bio = cur.bio
                )
            }.onSuccess {
                // Quan DataStore emeti, ja ens posarà isEditing = false i isLoading=false,
                // però tanquem edició immediat per UX
                _uiState.update { it.copy(isEditing = false, isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
