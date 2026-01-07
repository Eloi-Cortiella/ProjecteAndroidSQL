package com.app.projecteandroidsql.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "llibre")
data class LlibreEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,

    @ColumnInfo(name = "titol") val titol: String,
    @ColumnInfo(name = "autor") val autor: String,
    @ColumnInfo(name = "data_publicacio") val data_publicacio: Int? = null, // o Int si és any
    @ColumnInfo(name = "num_pagines") val num_pagines: Int? = null,
    @ColumnInfo(name = "sinopsi") val sinopsi: String? = null,
    @ColumnInfo(name = "portada") val portada: String? = null          // url o path local
)
