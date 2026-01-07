package com.app.projecteandroidsql.data.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.sessioDataStore by preferencesDataStore(name = "sessio")

data class Sessio(
    val idUsuariActual: Long? = null
) {
    val estaLogejat: Boolean get() = idUsuariActual != null
}

class SessioStore(private val context: Context) {

    private object Keys {
        val ID_USUARI_ACTUAL = longPreferencesKey("id_usuari_actual")
    }

    val sessioFlow: Flow<Sessio> =
        context.sessioDataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs ->
                Sessio(idUsuariActual = prefs[Keys.ID_USUARI_ACTUAL])
            }

    suspend fun establirUsuariActual(idUsuari: Long) {
        context.sessioDataStore.edit { prefs ->
            prefs[Keys.ID_USUARI_ACTUAL] = idUsuari
        }
    }

    suspend fun tancarSessio() {
        context.sessioDataStore.edit { prefs ->
            prefs.remove(Keys.ID_USUARI_ACTUAL)
        }
    }
}
