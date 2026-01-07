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
    @ColumnInfo(name = "data_publicacio") val data_publicacio: String, // o Int si és any
    @ColumnInfo(name = "num_pagines") val num_pagines: Int,
    @ColumnInfo(name = "sinopsi") val sinopsi: String,
    @ColumnInfo(name = "portada") val portada: String          // url o path local
)
