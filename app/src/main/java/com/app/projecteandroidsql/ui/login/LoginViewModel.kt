package com.app.projecteandroidsql.ui.login

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.projecteandroidsql.data.room.AppDatabase
import com.app.projecteandroidsql.data.room.entity.UsuariEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class LoginViewModel : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
        _errorMessage.value = null
    }

    private fun isValidPassword(password: String): Boolean = password.length >= 6

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /**
     * Retorna:
     *  - Long (id_usuari) si login OK
     *  - null si falla (i deixa missatge a errorMessage)
     */
    suspend fun onLoginSelected(context: Context): Long? {
        val emailActual = _email.value.orEmpty().trim()
        val passActual = _password.value.orEmpty()

        if (!isValidEmail(emailActual) || !isValidPassword(passActual)) {
            _errorMessage.value = "Revisa l'email i la contrasenya (mínim 6 caràcters)."
            return null
        }

        _isLoading.value = true
        _errorMessage.value = null

        return try {
            // Operacions de BD fora del fil principal
            val idUsuari = withContext(Dispatchers.IO) {
                val db = AppDatabase.getInstance(context)
                val dao = db.usuariDao()

                val existent = dao.buscarPerEmail(emailActual)

                if (existent == null) {
                    // ✅ MODE DEV (sense pantalla de registre):
                    // si no existeix, el creem automàticament.
                    val nomUsuari = emailActual.substringBefore("@").ifBlank { "usuari" }
                    val nou = UsuariEntity(
                        nom_usuari = nomUsuari,
                        hash_contrasenya = sha256(passActual),
                        email = emailActual
                    )
                    dao.inserir(nou)
                } else {
                    // comprova contrasenya (hash)
                    val hashEntrada = sha256(passActual)
                    val ok = existent.hash_contrasenya == hashEntrada || existent.hash_contrasenya == passActual
                    if (!ok) return@withContext -1L
                    existent.id
                }
            }

            _isLoading.value = false

            if (idUsuari == -1L) {
                _errorMessage.value = "Contrasenya incorrecta."
                null
            } else {
                idUsuari
            }
        } catch (e: Exception) {
            _isLoading.value = false
            _errorMessage.value = "Error fent login: ${e.message ?: "desconegut"}"
            null
        }
    }

    fun consumirError() {
        _errorMessage.value = null
    }

    private fun sha256(text: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(text.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}