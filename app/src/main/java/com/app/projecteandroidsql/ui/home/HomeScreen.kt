package com.app.projecteandroidsql.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

import com.app.projecteandroidsql.ui.home.HomeViewModel

@Composable
fun afegirLlibre() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var title by rememberSaveable { mutableStateOf(TextFieldValue("")) }
    val tasks = remember { mutableStateListOf<TodoItem>() }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Afegir llibre") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // INPUT DE TÍTOL
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Títol del llibre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // BOTÓ AFEGIR LLIBRE
            Button(
                onClick = {
                    val raw = title.text
                    if (!HomeViewModel.validarTitol(raw)) {
                        scope.launch {
                            snackbarHostState.showSnackbar("El títol ha de tenir almenys 3 caràcters.")
                        }
                        return@Button
                    }

                    val formatted = HomeViewModel.formatTitol(raw)

                    tasks.add(
                        TodoItem(
                            title = formatted,
                        )
                    )

                    // Neteja i feedback
                    title = TextFieldValue("")

                    scope.launch {
                        snackbarHostState.showSnackbar("Afegida: \"$formatted\"")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Afegir tasca")
            }
        }
    }
}
data class TodoItem(val title: String)
