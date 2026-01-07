package com.app.projecteandroidsql.data.room.repository

import androidx.room.withTransaction
import com.app.projecteandroidsql.data.room.AppDatabase
import com.app.projecteandroidsql.data.room.entity.EntradaBibliotecaEntity
import com.app.projecteandroidsql.data.room.entity.LlibreEntity
import com.app.projecteandroidsql.data.room.model.EstatLectura

class BibliotecaRepository(
    private val db: AppDatabase
) {
    /**
     * Crea un llibre i, automàticament, crea la seua entrada a la biblioteca
     * per a l’usuari amb estat PER_LLEGIR.
     */
    suspend fun afegirLlibreIAssignarAUsuari(
        idUsuari: Long,
        llibre: LlibreEntity
    ): Long {
        return db.withTransaction {
            val idLlibre = db.llibreDao().inserirOAactualitzar(llibre)

            db.entradaBibliotecaDao().inserirOAactualitzar(
                EntradaBibliotecaEntity(
                    idUsuari = idUsuari,
                    idLlibre = idLlibre,
                    estatLectura = EstatLectura.PER_LLEGIR.name
                )
            )

            idLlibre
        }
    }
}
