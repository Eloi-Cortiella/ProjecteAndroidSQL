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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment


import com.app.projecteandroidsql.ui.home.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = remember { HomeViewModel() }) {
    var mostrarDialogo by rememberSaveable { mutableStateOf(false) }

    if (mostrarDialogo) {
        afegirLlibre(viewModel) { mostrarDialogo = false }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { mostrarDialogo = true }) {
                Text("Afegeix Llibre")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun afegirLlibre(viewModel: HomeViewModel, onDismiss: () -> Unit) {
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
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Títol del llibre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    val raw = title.text
                    if (!viewModel.validarTitol(raw)) {
                        scope.launch {
                            snackbarHostState.showSnackbar("El títol ha de tenir almenys 3 caràcters.")
                        }
                        return@Button
                    }

                    val formatted = viewModel.formatTitol(raw)
                    tasks.add(TodoItem(title = formatted))
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
