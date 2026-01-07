package com.app.projecteandroidsql.ui.biblioteca

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.projecteandroidsql.data.room.AppDatabase
import com.app.projecteandroidsql.data.room.dao.LlibreBibliotecaItem
import com.app.projecteandroidsql.data.room.model.EstatLectura
import com.app.projecteandroidsql.data.session.Sessio
import com.app.projecteandroidsql.data.session.SessioStore
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibliotecaScreen() {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val scope = rememberCoroutineScope()

    // Sessió (mateix patró que al Perfil) :contentReference[oaicite:4]{index=4}
    val sessioStore = remember { SessioStore(context) }
    val sessio by sessioStore.sessioFlow.collectAsState(initial = Sessio())
    val idUsuari = sessio.idUsuariActual

    var query by remember { mutableStateOf("") }
    var filtre by remember { mutableStateOf(FiltreEstat.TOTS) }
    var idEntradaAEliminar by remember { mutableStateOf<Long?>(null) }
    var llibreAEditar by remember { mutableStateOf<LlibreBibliotecaItem?>(null) }

    val db = remember { AppDatabase.getInstance(context) }

    val llista: List<LlibreBibliotecaItem> = if (isPreview) {
        listOf(
            LlibreBibliotecaItem(1, 10, "El Quixot", "Cervantes", EstatLectura.PER_LLEGIR.name),
            LlibreBibliotecaItem(2, 11, "1984", "George Orwell", EstatLectura.EN_CURS.name),
            LlibreBibliotecaItem(3, 12, "Dune", "Frank Herbert", EstatLectura.LLEGIT.name),
        )
    } else {
        if (idUsuari == null) emptyList()
        else {
            val flow = remember(idUsuari, filtre) {
                db.entradaBibliotecaDao().observarBiblioteca(
                    idUsuari = idUsuari,
                    estat = filtre.estat
                )
            }
            val data by flow.collectAsState(initial = emptyList())
            data
        }
    }

    val filtrada = remember(llista, query) {
        val q = query.trim().lowercase()
        if (q.isBlank()) llista
        else llista.filter {
            it.titol.lowercase().contains(q) || it.autor.lowercase().contains(q)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Biblioteca") }) }
    ) { padding ->
        if (idEntradaAEliminar != null) {
            AlertDialog(
                onDismissRequest = { idEntradaAEliminar = null },
                title = { Text("Eliminar de la biblioteca") },
                text = { Text("Vols eliminar aquest llibre de la teua biblioteca?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val id = idEntradaAEliminar ?: return@TextButton
                            idEntradaAEliminar = null
                            scope.launch {
                                db.entradaBibliotecaDao().eliminarEntradaPerId(id)
                            }
                        }
                    ) { Text("Eliminar") }
                },
                dismissButton = {
                    TextButton(onClick = { idEntradaAEliminar = null }) { Text("Cancel·lar") }
                }
            )
        }
        if (llibreAEditar != null) {
            EditarLlibreDialog(
                item = llibreAEditar!!,
                onDismiss = { llibreAEditar = null },
                onGuardar = { nouTitol, nouAutor ->
                    val actual = llibreAEditar ?: return@EditarLlibreDialog
                    llibreAEditar = null

                    scope.launch {
                        db.llibreDao().actualitzarTitolAutor(
                            idLlibre = actual.idLlibre,
                            titol = nouTitol,
                            autor = nouAutor
                        )
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cerca
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Cercar llibre (títol o autor)") }
            )

            // Filtres
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FiltreEstat.entries.forEach { f ->
                    FilterChip(
                        selected = filtre == f,
                        onClick = { filtre = f },
                        label = { Text(f.etiqueta) }
                    )
                }
            }

            // Si no hi ha sessió
            if (!isPreview && idUsuari == null) {
                Text(
                    "No hi ha sessió iniciada. Torna a fer login.",
                    style = MaterialTheme.typography.bodyMedium
                )
                return@Column
            }

            // Llista
            if (filtrada.isEmpty()) {
                Text(
                    "No tens llibres en aquest filtre.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtrada, key = { it.idEntrada }) { item ->
                        LlibreCard(
                            item = item,
                            onCanviarEstat = { nou ->
                                if (isPreview) return@LlibreCard
                                scope.launch {
                                    db.entradaBibliotecaDao()
                                        .actualitzarEstatLectura(item.idEntrada, nou.name)
                                }
                            },
                            onEliminar = { idEntradaAEliminar = item.idEntrada },
                            onEditar = { llibreAEditar = item }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LlibreCard(
    item: LlibreBibliotecaItem,
    onCanviarEstat: (EstatLectura) -> Unit,
    onEliminar: () -> Unit,
    onEditar: () -> Unit
) {
    var menu by remember { mutableStateOf(false) }

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.titol, fontWeight = FontWeight.SemiBold)
                Text(
                    item.autor,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(6.dp))
                AssistChip(
                    onClick = { menu = true },
                    label = { Text(text = "Estat: ${item.estatLectura}") }
                )
            }

            Box {
                TextButton(onClick = { menu = true }) { Text("Canviar estat") }

                DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                    DropdownMenuItem(
                        text = { Text("Per llegir") },
                        onClick = {
                            menu = false
                            onCanviarEstat(EstatLectura.PER_LLEGIR)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("En curs") },
                        onClick = {
                            menu = false
                            onCanviarEstat(EstatLectura.EN_CURS)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Llegit") },
                        onClick = {
                            menu = false
                            onCanviarEstat(EstatLectura.LLEGIT)
                        }
                    )
                }
            }
            TextButton(onClick = onEliminar) {
                Text("Eliminar")
            }

            TextButton(onClick = onEditar) {
                Text("Editar")
            }
        }
    }
}

@Composable
private fun EditarLlibreDialog(
    item: LlibreBibliotecaItem,
    onDismiss: () -> Unit,
    onGuardar: (titol: String, autor: String) -> Unit
) {
    var titol by remember { mutableStateOf(item.titol) }
    var autor by remember { mutableStateOf(item.autor) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar llibre") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = titol,
                    onValueChange = { titol = it },
                    label = { Text("Títol") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onGuardar(titol.trim(), autor.trim()) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel·lar") }
        }
    )
}


//@Composable
//private fun EstatBadge(estat: EstatLectura) {
//    val text = when (estat) {
//        EstatLectura.PER_LLEGIR -> "Per llegir"
//        EstatLectura.EN_CURS -> "En curs"
//        EstatLectura.LLEGIT -> "Llegit"
//    }
//
//    Surface(
//        color = MaterialTheme.colorScheme.surface,
//        contentColor = MaterialTheme.colorScheme.onSurface,
//        shape = MaterialTheme.shapes.small
//    ) {
//        Text(
//            text = text,
//            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
//            style = MaterialTheme.typography.labelMedium
//        )
//    }
//}