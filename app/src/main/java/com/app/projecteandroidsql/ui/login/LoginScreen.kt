package com.app.projecteandroidsql.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.liveData
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (LoggedInUserView) -> Unit
) {
    // Estat del formulari i resultat del login (provinent del ViewModel)
    val loginFormState by viewModel.loginFormState.observeAsState(LoginFormState())
    val loginResult by viewModel.loginResult.observeAsState()

    // Estat local dels camps de text
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    // Quan hi haja èxit al login, avisa el MainActivity
    LaunchedEffect(loginResult?.success) {
        loginResult?.success?.let { user ->
            onLoginSuccess(user)
        }
    }

    // UI del login
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Inicia sessió",
                style = MaterialTheme.typography.headlineMedium
            )

            // Usuari
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    viewModel.loginDataChanged(username, password)
                },
                label = { Text("Usuari o correu") },
                isError = loginFormState.usernameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (loginFormState.usernameError != null) {
                Text(
                    text = stringResource(id = loginFormState.usernameError!!),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Contrasenya
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.loginDataChanged(username, password)
                },
                label = { Text("Contrasenya") },
                visualTransformation = PasswordVisualTransformation(),
                isError = loginFormState.passwordError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (loginFormState.passwordError != null) {
                Text(
                    text = stringResource(id = loginFormState.passwordError!!),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Botó de login
            Button(
                onClick = { viewModel.login(username, password) },
                enabled = loginFormState.isDataValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }

            // Missatge d’error general (login_failed)
            if (loginResult?.error != null) {
                Text(
                    text = stringResource(id = loginResult.error!!),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
