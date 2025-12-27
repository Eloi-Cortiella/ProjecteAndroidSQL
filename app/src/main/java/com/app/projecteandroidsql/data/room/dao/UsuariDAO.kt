package com.app.projecteandroidsql.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.projecteandroidsql.data.room.entity.UsuariEntity

@Dao
interface UsuariDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun inserir(usuari: UsuariEntity): Long

    @Query("SELECT * FROM usuaris WHERE nom_usuari = :nomUsuari LIMIT 1")
    suspend fun buscarPerNomUsuari(nomUsuari: String): UsuariEntity?

    @Query("SELECT * FROM usuaris WHERE id = :id LIMIT 1")
    suspend fun buscarPerId(id: Long): UsuariEntity?
}
