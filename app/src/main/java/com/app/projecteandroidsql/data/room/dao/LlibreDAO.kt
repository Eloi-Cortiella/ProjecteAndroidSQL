package com.app.projecteandroidsql.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.projecteandroidsql.data.room.entity.LlibreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LlibreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirOAactualitzar(llibre: LlibreEntity): Long

    @Query("SELECT * FROM llibres ORDER BY titol ASC")
    fun observarTots(): Flow<List<LlibreEntity>>

    @Query("SELECT * FROM llibres WHERE id = :id LIMIT 1")
    suspend fun buscarPerId(id: Long): LlibreEntity?
}
