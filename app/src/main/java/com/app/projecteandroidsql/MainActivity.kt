package com.app.projecteandroidsql

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.app.projecteandroidsql.ui.theme.ProjecteAndroidSQLTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    @PreviewScreenSizes
    @Composable
    fun IniciarApp() {
        var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                it.icon,
                                contentDescription = it.label
                            )
                        },
                        label = { Text(it.label) },
                        selected = it == currentDestination,
                        onClick = { currentDestination = it }
                    )
                }
            }
        )
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