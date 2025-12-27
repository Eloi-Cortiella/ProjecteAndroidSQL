package com.app.projecteandroidsql.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.projecteandroidsql.data.room.entity.EntradaBibliotecaEntity
import com.app.projecteandroidsql.data.room.entity.EstatLectura
import kotlinx.coroutines.flow.Flow

data class FilaStatsBiblioteca(
    val total: Int,
    val llegits: Int,
    val enCurs: Int,
    val perLlegir: Int
)

@Dao
interface EntradaBibliotecaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirOAactualitzar(entrada: EntradaBibliotecaEntity): Long

    @Query("SELECT * FROM entrades_biblioteca WHERE id_usuari = :idUsuari")
    fun observarEntrades(idUsuari: Long): Flow<List<EntradaBibliotecaEntity>>

    @Query("""
        UPDATE entrades_biblioteca
        SET estat = :estat,
            progres_pagines = :progresPagines,
            actualitzat_a_epoch_ms = :actualitzatA
        WHERE id_usuari = :idUsuari AND id_llibre = :idLlibre
    """)
    suspend fun actualitzarEstat(
        idUsuari: Long,
        idLlibre: Long,
        estat: EstatLectura,
        progresPagines: Int,
        actualitzatA: Long = System.currentTimeMillis()
    ): Int

    @Query("""
        SELECT
          COUNT(*) AS total,
          SUM(CASE WHEN estat = 'LLEGIT' THEN 1 ELSE 0 END) AS llegits,
          SUM(CASE WHEN estat = 'EN_CURS' THEN 1 ELSE 0 END) AS enCurs,
          SUM(CASE WHEN estat = 'PER_LLEGIR' THEN 1 ELSE 0 END) AS perLlegir
        FROM entrades_biblioteca
        WHERE id_usuari = :idUsuari
    """)
    fun observarStats(idUsuari: Long): Flow<FilaStatsBiblioteca>
}
