package com.app.projecteandroidsql.ui.perfil

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.projecteandroidsql.ui.theme.ProjecteAndroidSQLTheme
import kotlinx.coroutines.launch

class PerfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjecteAndroidSQLTheme {
                PerfilScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PerfilScreen(viewModel: PerfilViewModel = PerfilViewModel()) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as? Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = {
                        //activity?.onBackPressedDispatcher?.onBackPressed()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Enrere"
                        )
                    }
                }
            )
        },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Capçalera amb avatar i info principal
            ProfileHeader(
                name = uiState.name,
                email = uiState.email,
                onChangeAvatarClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Funció de canviar foto pendent d'implementar")
                    }
                }
            )

            // Informació bàsica (editable)
            ProfileInfoCard(
                uiState = uiState,
                onNameChange = viewModel::onNameChange,
                onUsernameChange = viewModel::onUsernameChange,
                onPhoneChange = viewModel::onPhoneChange,
                onBioChange = viewModel::onBioChange,
                onEditToggle = { viewModel.startEditing() },
                onSave = {
                    viewModel.saveChanges()
                    scope.launch {
                        snackbarHostState.showSnackbar("Perfil actualitzat correctament")
                    }
                },
                onCancel = {
                    viewModel.cancelEditing()
                    scope.launch {
                        snackbarHostState.showSnackbar("Canvis descartats")
                    }
                }
            )

            // Preferències
            PreferencesCard(
                notificationsEnabled = uiState.notificationsEnabled,
                darkModeEnabled = uiState.darkModeEnabled,
                onNotificationsChange = viewModel::onNotificationsChange,
                onDarkModeChange = viewModel::onDarkModeChange
            )

            // Sessió / compte
            SessionCard(
                onLogout = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Sessió tancada")
                    }
                    // Ací podries netejar credencials i anar a LoginActivity
                    activity?.finish()
                },
                onDeleteAccount = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Funció d'eliminar compte pendent d'implementar")
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    name: String,
    email: String,
    onChangeAvatarClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initialsFromName(name),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        OutlinedButton(onClick = onChangeAvatarClick) {
            Text("Canviar foto de perfil")
        }
    }
}

@Composable
private fun ProfileInfoCard(
    uiState: PerfilUiState,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onEditToggle: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Informació bàsica",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (!uiState.isEditing) {
                    OutlinedButton(onClick = onEditToggle) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar perfil"
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("Editar")
                    }
                }
            }

            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = { Text("Nom complet") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = uiState.isEditing
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { /* correu generalment no editable */ },
                label = { Text("Correu electrònic") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false
            )

            OutlinedTextField(
                value = uiState.username,
                onValueChange = onUsernameChange,
                label = { Text("Nom d'usuari") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = uiState.isEditing
            )

            OutlinedTextField(
                value = uiState.phone,
                onValueChange = onPhoneChange,
                label = { Text("Telèfon") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = uiState.isEditing
            )

            OutlinedTextField(
                value = uiState.bio,
                onValueChange = onBioChange,
                label = { Text("Descripció / Bio") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                enabled = uiState.isEditing,
                maxLines = 4
            )

            if (uiState.isEditing) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onSave,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar")
                    }
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel·lar")
                    }
                }
            }
        }
    }
}

@Composable
private fun PreferencesCard(
    notificationsEnabled: Boolean,
    darkModeEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    onDarkModeChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Preferències",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            PreferenceRow(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificacions"
                    )
                },
                title = "Notificacions",
                subtitle = "Rebre avisos importants i actualitzacions",
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsChange
            )

            PreferenceRow(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.DarkMode,
                        contentDescription = "Mode fosc"
                    )
                },
                title = "Mode fosc",
                subtitle = "Ajust visual per a entorns amb poca llum",
                checked = darkModeEnabled,
                onCheckedChange = onDarkModeChange
            )
        }
    }
}

@Composable
private fun PreferenceRow(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SessionCard(
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Compte i sessió",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Tancar sessió"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text("Tancar sessió")
            }

            OutlinedButton(
                onClick = onDeleteAccount,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar compte"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text("Eliminar compte")
            }
        }
    }
}

private fun initialsFromName(name: String): String {
    return name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first().uppercaseChar() }
        .joinToString("")
}
