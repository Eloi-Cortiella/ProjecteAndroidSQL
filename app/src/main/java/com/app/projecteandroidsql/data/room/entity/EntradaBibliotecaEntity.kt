package com.app.projecteandroidsql.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.app.projecteandroidsql.data.room.model.EstatLectura

@Entity(
    tableName = "entrada_biblioteca",
    indices = [
        Index(value = ["id_usuari"]),
        Index(value = ["id_llibre"]),
        Index(value = ["id_usuari", "id_llibre"], unique = true)
    ]
)
data class EntradaBibliotecaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,

    @ColumnInfo(name = "id_usuari") val idUsuari: Long,
    @ColumnInfo(name = "id_llibre") val idLlibre: Long,

    // Guardem l’enum com a String
    @ColumnInfo(name = "estat_lectura") val estatLectura: String = EstatLectura.PER_LLEGIR.name,
)
