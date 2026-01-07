package com.app.projecteandroidsql

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.lifecycleScope
import com.app.projecteandroidsql.data.session.SessioStore
import com.app.projecteandroidsql.ui.home.HomeScreen
import com.app.projecteandroidsql.ui.login.LoginActivity
import com.app.projecteandroidsql.ui.perfil.PerfilScreen
import com.app.projecteandroidsql.ui.biblioteca.BibliotecaScreen
import com.app.projecteandroidsql.ui.theme.ProjecteAndroidSQLTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val sessioStore = SessioStore(this@MainActivity)
            val sessio = sessioStore.sessioFlow.first()

            if (!sessio.estaLogejat) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                return@launch
            }
        }
        enableEdgeToEdge()
        setContent {
            ProjecteAndroidSQLTheme {
                IniciarApp()
            }
        }
    }

    /**
     * Classe per a la vista principal de l'aplicació afegida amb jetpack compose
     */

    // MainActivity.kt
    @PreviewScreenSizes
    @Composable
    fun IniciarApp() {
        var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach { destination ->
                    item(
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label
                            )
                        },
                        label = { Text(destination.label) },
                        selected = destination == currentDestination,
                        onClick = { currentDestination = destination }
                    )
                }
            }
        ) { // Conté el contingut de la pantalla
            when (currentDestination) {
                AppDestinations.HOME -> HomeScreen()
                AppDestinations.BIBLIOTECA -> BibliotecaScreen()
                AppDestinations.PERFIL -> PerfilScreen()
            }
        }
    }


    enum class AppDestinations(
        val label: String,
        val icon: ImageVector,
    ) {
        HOME("Home", Icons.Default.Home),
        BIBLIOTECA("Biblioteca", Icons.Default.CheckCircle),
        PERFIL("Perfil", Icons.Default.AccountBox),
    }
}