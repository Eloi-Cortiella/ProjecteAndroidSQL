package com.app.projecteandroidsql.ui.biblioteca

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.projecteandroidsql.data.room.AppDatabase
import com.app.projecteandroidsql.data.room.dao.LlibreAmbEstat
import com.app.projecteandroidsql.data.room.entity.EntradaBibliotecaEntity
import com.app.projecteandroidsql.data.room.model.EstatLectura
import com.app.projecteandroidsql.data.session.SessioStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BibliotecaUiState(
    val idUsuari: Long? = null,
    val cercador: String = "",
    val filtre: FiltreEstat = FiltreEstat.TOTS,
    val llibres: List<LlibreAmbEstat> = emptyList(),
    val carregant: Boolean = true
)

class BibliotecaViewModel(app: Application) : AndroidViewModel(app) {

    private val context = getApplication<Application>().applicationContext
    private val db = AppDatabase.getInstance(context)
    private val sessioStore = SessioStore(context)

    private val _cercador = MutableStateFlow("")
    private val _filtre = MutableStateFlow(FiltreEstat.TOTS)
    private val _idUsuari = MutableStateFlow<Long?>(null)

    private val llibresFlow: Flow<List<LlibreAmbEstat>> =
        _idUsuari
            .filterNotNull()
            .flatMapLatest { id ->
                db.llibreDao().observarLlibresAmbEstat(id)
            }
            .catch { emit(emptyList()) }

    val uiState: StateFlow<BibliotecaUiState> =
        combine(
            _idUsuari,
            _cercador,
            _filtre,
            llibresFlow.onStart { emit(emptyList()) }
        ) { idUsuari, cercador, filtre, llibres ->
            val filtrats = llibres
                .filter { item ->
                    val q = cercador.trim()
                    if (q.isEmpty()) true
                    else item.llibre.titol.contains(q, ignoreCase = true) ||
                            item.llibre.autor.contains(q, ignoreCase = true)
                }
                .filter { item ->
                    when (filtre) {
                        FiltreEstat.TOTS -> true
                        FiltreEstat.PER_LLEGIR -> item.estatLectura == EstatLectura.PER_LLEGIR.name
                        FiltreEstat.EN_CURS -> item.estatLectura == EstatLectura.EN_CURS.name
                        FiltreEstat.LLEGIT -> item.estatLectura == EstatLectura.LLEGIT.name
                    }
                }

            BibliotecaUiState(
                idUsuari = idUsuari,
                cercador = cercador,
                filtre = filtre,
                llibres = filtrats,
                carregant = idUsuari == null
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BibliotecaUiState())

    init {
        // Escolta sessió i guarda idUsuari actual
        viewModelScope.launch {
            sessioStore.sessioFlow
                .map { it.idUsuariActual }
                .distinctUntilChanged()
                .collect { _idUsuari.value = it }
        }
    }

    fun onCercadorChange(nou: String) {
        _cercador.value = nou
    }

    fun onFiltreChange(nou: FiltreEstat) {
        _filtre.value = nou
    }

    fun canviarEstatLlibre(idLlibre: Long, nouEstat: EstatLectura) {
        val idUsuari = _idUsuari.value ?: return

        viewModelScope.launch(Dispatchers.IO) {
            db.entradaBibliotecaDao().inserirOAactualitzar(
                EntradaBibliotecaEntity(
                    idUsuari = idUsuari,
                    idLlibre = idLlibre,
                    estatLectura = nouEstat.name
                )
            )
        }
    }
}
