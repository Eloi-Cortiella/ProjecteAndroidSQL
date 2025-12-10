package com.app.projecteandroidsql.ui.perfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class PerfilUiState(
    val name: String = "Nom Exemple",
    val email: String = "correu@example.com",
    val username: String = "usuari123",
    val phone: String = "+34 600 000 000",
    val bio: String = "Aquí pots escriure una breu descripció sobre tu.",
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val isEditing: Boolean = false
)

class PerfilViewModel {

    // Estat "guardat" real (el que estaria a BD/servidor)
    private var savedState = PerfilUiState()

    // Estat visible actualment a la UI (editable)
    var uiState by mutableStateOf(savedState)
        private set

    fun onNameChange(newName: String) {
        uiState = uiState.copy(name = newName)
    }

    fun onUsernameChange(newUsername: String) {
        uiState = uiState.copy(username = newUsername)
    }

    fun onPhoneChange(newPhone: String) {
        uiState = uiState.copy(phone = newPhone)
    }

    fun onBioChange(newBio: String) {
        uiState = uiState.copy(bio = newBio)
    }

    fun onNotificationsChange(enabled: Boolean) {
        uiState = uiState.copy(notificationsEnabled = enabled)
    }

    fun onDarkModeChange(enabled: Boolean) {
        uiState = uiState.copy(darkModeEnabled = enabled)
    }

    fun startEditing() {
        uiState = uiState.copy(isEditing = true)
    }

    fun cancelEditing() {
        // Tornem a l'estat guardat i llevem el mode edició
        uiState = savedState.copy(isEditing = false)
    }

    fun saveChanges() {
        // Ací en un futur podries fer crida a API / BD
        savedState = uiState.copy(isEditing = false)
        uiState = savedState
    }
}
