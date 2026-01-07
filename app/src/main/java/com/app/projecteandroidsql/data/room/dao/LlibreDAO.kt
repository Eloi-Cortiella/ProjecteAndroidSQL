package com.app.projecteandroidsql.data.room.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.projecteandroidsql.data.room.entity.LlibreEntity
import kotlinx.coroutines.flow.Flow

data class LlibreAmbEstat(
    @Embedded val llibre: LlibreEntity,
    @ColumnInfo(name = "estat_lectura") val estatLectura: String
)

@Dao
interface LlibreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirOAactualitzar(llibre: LlibreEntity): Long

    @Query("SELECT * FROM llibre ORDER BY titol ASC")
    fun observarTots(): Flow<List<LlibreEntity>>

    @Query("SELECT * FROM llibre WHERE id = :id LIMIT 1")
    suspend fun buscarPerId(id: Long): LlibreEntity?

    @Query(
        """
        SELECT 
            l.*,
            COALESCE(e.estat_lectura, 'PER_LLEGIR') AS estat_lectura
        FROM llibre l
        LEFT JOIN entrada_biblioteca e
            ON e.id_llibre = l.id AND e.id_usuari = :idUsuari
        ORDER BY l.titol ASC
        """
    )
    fun observarLlibresAmbEstat(idUsuari: Long): Flow<List<LlibreAmbEstat>>

    @Query("""
        UPDATE llibre SET
            titol = :titol,
            autor = :autor,
            data_publicacio = :dataPublicacio,
            num_pagines = :numPagines,
            sinopsi = :sinopsi,
            portada = :portada
        WHERE id = :idLlibre
    """)
    suspend fun actualitzarLlibre(
        idLlibre: Long,
        titol: String,
        autor: String,
        dataPublicacio: Int?,
        numPagines: Int?,
        sinopsi: String?,
        portada: String?
    ): Int

    @Query("UPDATE llibre SET titol = :titol, autor = :autor WHERE id = :idLlibre")
    suspend fun actualitzarTitolAutor(idLlibre: Long, titol: String, autor: String): Int
}
