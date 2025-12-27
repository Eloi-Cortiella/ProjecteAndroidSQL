package com.app.projecteandroidsql.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class StatsBiblioteca(
    val llegits: Int,
    val enCurs: Int,
    val perLlegir: Int
) {
    val total: Int get() = llegits + enCurs + perLlegir
}

@Dao
interface EntradaBibliotecaDao {

    @Query(
        """
        SELECT
            SUM(CASE WHEN estat_lectura = 'LLEGIT' THEN 1 ELSE 0 END) AS llegits,
            SUM(CASE WHEN estat_lectura = 'EN_CURS' THEN 1 ELSE 0 END) AS enCurs,
            SUM(CASE WHEN estat_lectura = 'PER_LLEGIR' THEN 1 ELSE 0 END) AS perLlegir
        FROM entrada_biblioteca
        WHERE id_usuari = :idUsuari
        """
    )
    fun observarStats(idUsuari: Long): Flow<StatsBiblioteca?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(entrada: com.app.projecteandroidsql.data.room.entity.EntradaBibliotecaEntity)
}
