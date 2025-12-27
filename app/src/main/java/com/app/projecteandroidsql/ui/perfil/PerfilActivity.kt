package com.app.projecteandroidsql.ui.perfil

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.projecteandroidsql.data.session.SessioStore
import com.app.projecteandroidsql.ui.login.LoginActivity
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material.icons.filled.MenuBook
import com.app.projecteandroidsql.data.session.Sessio
import com.app.projecteandroidsql.data.room.AppDatabase
import com.app.projecteandroidsql.data.room.dao.StatsBiblioteca

class PerfilActivity : ComponentActivity() {

    private val viewModel: PerfilViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            ProjecteAndroidSQLTheme(
                darkTheme = uiState.darkModeEnabled,
                dynamicColor = true
            ) {
                PerfilScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(viewModel: PerfilViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = LocalContext.current as? Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = {
                        // activity?.onBackPressedDispatcher?.onBackPressed()
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

        PerfilContent(
            uiState = uiState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            // FALTA IMPLEMENTAR
            onChangeAvatarClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("Funció de canviar foto pendent d'implementar")
                }
            },
            onNameChange = viewModel::onNameChange,
            onUsernameChange = viewModel::onUsernameChange,
            onPhoneChange = viewModel::onPhoneChange,
            onBioChange = viewModel::onBioChange,
            onEditToggle = viewModel::startEditing,
            // FALTA IMPLEMENTAR
            onSave = {
                viewModel.saveChanges()
                scope.launch { snackbarHostState.showSnackbar("Perfil actualitzat correctament") }
            },
            // FALTA IMPLEMENTAR
            onCancel = {
                viewModel.cancelEditing()
                scope.launch { snackbarHostState.showSnackbar("Canvis descartats") }
            },
            onNotificationsChange = viewModel::onNotificationsChange,
            onDarkModeChange = viewModel::onDarkModeChange,
            onLogout = {
                scope.launch {
                    SessioStore(context).tancarSessio()

                    val intent = Intent(context, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                }
            },
            // FALTA IMPLEMENTAR
            onDeleteAccount = {
                scope.launch {
                    snackbarHostState.showSnackbar("Funció d'eliminar compte pendent d'implementar")
                }
            }
        )
    }
}

/**
 * ✅ Composable “puro” (sense ViewModel).
 * Això és el que usem per al Preview i també per la pantalla real.
 */
@Composable
private fun PerfilContent(
    uiState: PerfilUiState,
    modifier: Modifier = Modifier,
    onChangeAvatarClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onEditToggle: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
    onDarkModeChange: (Boolean) -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        ProfileHeader(
            name = uiState.name,
            email = uiState.email,
            onChangeAvatarClick = onChangeAvatarClick
        )

        ProfileInfoCard(
            uiState = uiState,
            onNameChange = onNameChange,
            onUsernameChange = onUsernameChange,
            onPhoneChange = onPhoneChange,
            onBioChange = onBioChange,
            onEditToggle = onEditToggle,
            onSave = onSave,
            onCancel = onCancel
        )
        // NO FUNCIONA FALTA IMPLEMENTAR
        PreferencesCard(
            notificationsEnabled = uiState.notificationsEnabled,
            darkModeEnabled = uiState.darkModeEnabled,
            onNotificationsChange = onNotificationsChange,
            onDarkModeChange = onDarkModeChange
        )

        BibliotecaStatsCard()

        SessionCard(
            onLogout = onLogout,
            onDeleteAccount = onDeleteAccount
        )
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
                onValueChange = { },
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
                    ) { Text("Guardar") }

                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f)
                    ) { Text("Cancel·lar") }
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
        ) { icon() }

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

private fun initialsFromName(name: String): String =
    name.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first().uppercaseChar() }
        .joinToString("")

// ✅ Preview sense ViewModel
@Preview(showBackground = true)
@Composable
private fun PerfilContentPreview() {
    ProjecteAndroidSQLTheme(darkTheme = false, dynamicColor = true) {
        PerfilContent(
            uiState = PerfilUiState(
                name = "Eloi Cortiella",
                email = "eloi@example.com",
                username = "eloi123",
                phone = "+34 600 111 222",
                bio = "Lector i col·leccionista de llibres.",
                notificationsEnabled = true,
                darkModeEnabled = false,
                isEditing = false,
                isLoading = false
            ),
            onChangeAvatarClick = {},
            onNameChange = {},
            onUsernameChange = {},
            onPhoneChange = {},
            onBioChange = {},
            onEditToggle = {},
            onSave = {},
            onCancel = {},
            onNotificationsChange = {},
            onDarkModeChange = {},
            onLogout = {},
            onDeleteAccount = {}
        )
    }
}

@Composable
private fun BibliotecaStatsCard() {
    val context = LocalContext.current

    // 1) Sessió -> quin usuari està actiu
    val sessioStore = remember { SessioStore(context) }
    val sessio by sessioStore.sessioFlow.collectAsState(initial = Sessio())

    // 2) Si no hi ha sessió, no té sentit mostrar stats
    val idUsuari = sessio.idUsuariActual
    if (idUsuari == null) return

    // 3) Room -> stats en temps real
    val db = remember { AppDatabase.getInstance(context) }
    val statsNullable by db.entradaBibliotecaDao().observarStats(idUsuari)
        .collectAsState(initial = null)

    val stats = statsNullable ?: StatsBiblioteca(llegits = 0, enCurs = 0, perLlegir = 0)

    val total = stats.total
    val progress = if (total == 0) 0f else (stats.llegits.toFloat() / total.toFloat())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.MenuBook,
                    contentDescription = "Biblioteca",
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = "Biblioteca",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = if (total == 0) "Encara no tens llibres a la biblioteca."
                else "Progrés de lectura: ${stats.llegits} / $total llegits",
                style = MaterialTheme.typography.bodyMedium
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatChip("Llegits", stats.llegits)
                StatChip("En curs", stats.enCurs)
                StatChip("Per llegir", stats.perLlegir)
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: Int) {
    Card(
        shape = RoundedCornerShape(999.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$label: ", fontWeight = FontWeight.SemiBold)
            Text(value.toString())
        }
    }
}