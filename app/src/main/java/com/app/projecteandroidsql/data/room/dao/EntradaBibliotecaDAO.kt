package com.app.projecteandroidsql.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.projecteandroidsql.data.room.entity.EntradaBibliotecaEntity
import kotlinx.coroutines.flow.Flow
import com.app.projecteandroidsql.data.room.model.StatsBiblioteca

@Dao
interface EntradaBibliotecaDao {

    @Query(
        """
        SELECT
            COALESCE(SUM(CASE WHEN estat_lectura = :estatLlegit THEN 1 ELSE 0 END), 0) AS llegits,
            COALESCE(SUM(CASE WHEN estat_lectura = :estatEnCurs THEN 1 ELSE 0 END), 0) AS enCurs,
            COALESCE(SUM(CASE WHEN estat_lectura = :estatPerLlegir THEN 1 ELSE 0 END), 0) AS perLlegir
        FROM entrada_biblioteca
        WHERE id_usuari = :idUsuari
        """
    )

    fun observarStats(
        idUsuari: Long,
        estatLlegit: String,
        estatEnCurs: String,
        estatPerLlegir: String
    ): Flow<StatsBiblioteca>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirOAactualitzar(entrada: EntradaBibliotecaEntity): Long

    @Query(
        """
    SELECT 
        eb.id AS idEntrada,
        l.id AS idLlibre,
        l.titol AS titol,
        l.autor AS autor,
        eb.estat_lectura AS estatLectura
    FROM entrada_biblioteca eb
    JOIN llibre l ON l.id = eb.id_llibre
    WHERE eb.id_usuari = :idUsuari
      AND (:estat IS NULL OR eb.estat_lectura = :estat)
    ORDER BY l.titol COLLATE NOCASE ASC
    """
    )
    fun observarBiblioteca(
        idUsuari: Long,
        estat: String? = null
    ): Flow<List<LlibreBibliotecaItem>>

    @Query("UPDATE entrada_biblioteca SET estat_lectura = :nouEstat WHERE id = :idEntrada")
    suspend fun actualitzarEstatLectura(idEntrada: Long, nouEstat: String)

    @Query("DELETE FROM entrada_biblioteca WHERE id = :idEntrada")
    suspend fun eliminarEntradaPerId(idEntrada: Long): Int
}
