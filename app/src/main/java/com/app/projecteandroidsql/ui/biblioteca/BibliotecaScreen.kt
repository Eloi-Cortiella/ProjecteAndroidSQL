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
                            }
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
    onCanviarEstat: (EstatLectura) -> Unit
) {
    var menu by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
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
                TextButton(onClick = { menu = true }) { Text("Canviar") }
                DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                    DropdownMenuItem(
                        text = { Text("Per llegir") },
                        onClick = { menu = false; onCanviarEstat(EstatLectura.PER_LLEGIR) }
                    )
                    DropdownMenuItem(
                        text = { Text("En curs") },
                        onClick = { menu = false; onCanviarEstat(EstatLectura.EN_CURS) }
                    )
                    DropdownMenuItem(
                        text = { Text("Llegit") },
                        onClick = { menu = false; onCanviarEstat(EstatLectura.LLEGIT) }
                    )
                }
            }
        }
    }
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