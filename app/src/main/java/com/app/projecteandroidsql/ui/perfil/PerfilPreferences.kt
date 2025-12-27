package com.app.projecteandroidsql.ui.perfil

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.profileDataStore by preferencesDataStore(name = "profile_prefs")

data class ProfilePrefs(
    val name: String,
    val email: String,
    val username: String,
    val phone: String,
    val bio: String,
    val notificationsEnabled: Boolean,
    val darkModeEnabled: Boolean
)

class PerfilPreferences(private val context: Context) {

    private object Keys {
        val NAME = stringPreferencesKey("profile_name")
        val EMAIL = stringPreferencesKey("profile_email")
        val USERNAME = stringPreferencesKey("profile_username")
        val PHONE = stringPreferencesKey("profile_phone")
        val BIO = stringPreferencesKey("profile_bio")
        val NOTIFICATIONS = booleanPreferencesKey("prefs_notifications")
        val DARK_MODE = booleanPreferencesKey("prefs_dark_mode")
    }

    val flow: Flow<ProfilePrefs> =
        context.profileDataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { p ->
                ProfilePrefs(
                    name = p[Keys.NAME] ?: "Nom Exemple",
                    email = p[Keys.EMAIL] ?: "correu@example.com",
                    username = p[Keys.USERNAME] ?: "usuari123",
                    phone = p[Keys.PHONE] ?: "+34 600 000 000",
                    bio = p[Keys.BIO] ?: "Aquí pots escriure una breu descripció sobre tu.",
                    notificationsEnabled = p[Keys.NOTIFICATIONS] ?: true,
                    darkModeEnabled = p[Keys.DARK_MODE] ?: false
                )
            }

    suspend fun saveProfile(name: String, email: String, username: String, phone: String, bio: String) {
        context.profileDataStore.edit { p ->
            p[Keys.NAME] = name
            p[Keys.EMAIL] = email
            p[Keys.USERNAME] = username
            p[Keys.PHONE] = phone
            p[Keys.BIO] = bio
        }
    }

    suspend fun setNotifications(enabled: Boolean) {
        context.profileDataStore.edit { p -> p[Keys.NOTIFICATIONS] = enabled }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.profileDataStore.edit { p -> p[Keys.DARK_MODE] = enabled }
    }
}
