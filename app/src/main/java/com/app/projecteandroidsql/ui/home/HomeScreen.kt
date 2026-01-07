package com.app.projecteandroidsql.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.app.projecteandroidsql.data.room.AppDatabase
import com.app.projecteandroidsql.data.room.entity.LlibreEntity
import com.app.projecteandroidsql.data.room.repository.BibliotecaRepository
import com.app.projecteandroidsql.data.session.Sessio
import com.app.projecteandroidsql.data.session.SessioStore
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: HomeViewModel = remember { HomeViewModel() }) {
    var mostrarFormulari by rememberSaveable { mutableStateOf(false) }

    if (mostrarFormulari) {
        AfegirLlibreScreen(
            viewModel = viewModel,
            onDismiss = { mostrarFormulari = false }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { mostrarFormulari = true }) {
                Text("Afegeix Llibre")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AfegirLlibreScreen(
    viewModel: HomeViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Sessió
    val sessioStore = remember { SessioStore(context) }
    val sessio by sessioStore.sessioFlow.collectAsState(initial = Sessio())
    val idUsuari = sessio.idUsuariActual

    // DB
    val db = remember { AppDatabase.getInstance(context) }

    // --- Camps (Strings) ---
    var titol by rememberSaveable { mutableStateOf("") }
    var autor by rememberSaveable { mutableStateOf("") }
    var anyPublicacio by rememberSaveable { mutableStateOf("") }
    var numPagines by rememberSaveable { mutableStateOf("") }
    var sinopsi by rememberSaveable { mutableStateOf("") }
    var portadaUrl by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Afegir llibre") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Text("✕")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (idUsuari == null) {
                Text("No hi ha sessió iniciada. Torna a fer login.")
                return@Column
            }

            OutlinedTextField(
                value = titol,
                onValueChange = { titol = it },
                label = { Text("Títol *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = autor,
                onValueChange = { autor = it },
                label = { Text("Autor/a *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = anyPublicacio,
                onValueChange = { anyPublicacio = it.filter { c -> c.isDigit() } },
                label = { Text("Any de publicació") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = numPagines,
                onValueChange = { numPagines = it.filter { c -> c.isDigit() } },
                label = { Text("Nº pàgines") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = portadaUrl,
                onValueChange = { portadaUrl = it },
                label = { Text("Portada (URL o ruta)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = sinopsi,
                onValueChange = { sinopsi = it },
                label = { Text("Sinopsi") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(Modifier.height(8.dp))

            val repo = remember { BibliotecaRepository(db) }

            Button(
                onClick = {
                    val titolRaw = titol
                    if (!viewModel.validarTitol(titolRaw)) {
                        scope.launch {
                            snackbarHostState.showSnackbar("El títol ha de tenir almenys 3 caràcters.")
                        }
                        return@Button
                    }
                    if (autor.trim().length < 2) {
                        scope.launch { snackbarHostState.showSnackbar("L'autor/a és obligatori.") }
                        return@Button
                    }

                    val anyInt = anyPublicacio.toIntOrNull()
                    val pagInt = numPagines.toIntOrNull()

                    scope.launch {
                        try {
                            val llibreId = LlibreEntity(
                                    titol = viewModel.formatTitol(titolRaw),
                                    autor = autor.trim(),
                                    data_publicacio = anyInt,
                                    num_pagines = pagInt,
                                    sinopsi = sinopsi.trim().ifBlank { null },
                                    portada = portadaUrl.trim().ifBlank { null }
                                )


                            repo.afegirLlibreIAssignarAUsuari(
                                idUsuari = idUsuari,
                                llibre = llibreId
                            )

                            snackbarHostState.showSnackbar("Llibre afegit correctament ✅")
                            onDismiss()
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Error afegint llibre: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar llibre")
            }
        }
    }
}
