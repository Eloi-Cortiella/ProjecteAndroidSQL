package com.app.projecteandroidsql.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class EstatLectura { PER_LLEGIR, EN_CURS, LLEGIT }

@Entity(
    tableName = "entrades_biblioteca",
    foreignKeys = [
        ForeignKey(
            entity = UsuariEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_usuari"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LlibreEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_llibre"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_usuari"]),
        Index(value = ["id_llibre"]),
        Index(value = ["id_usuari", "id_llibre"], unique = true)
    ]
)
data class EntradaBibliotecaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val id_usuari: Long,
    val id_llibre: Long,
    val estat: EstatLectura = EstatLectura.PER_LLEGIR,
    val progres_pagines: Int = 0,
    val actualitzat_a_epoch_ms: Long = System.currentTimeMillis()
)
