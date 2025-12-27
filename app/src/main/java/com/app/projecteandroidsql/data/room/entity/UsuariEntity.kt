package com.app.projecteandroidsql.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuaris",
    indices = [
        Index(value = ["nom_usuari"], unique = true),
        Index(value = ["email"], unique = true)
    ]
)
data class UsuariEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nom_usuari: String,
    val hash_contrasenya: String, // millor hash que password pla
    val email: String
)
