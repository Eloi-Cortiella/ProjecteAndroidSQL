package com.app.projecteandroidsql.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "llibres",
    indices = [Index(value = ["titol", "autor"])]
)
data class LlibreEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titol: String,
    val autor: String,
    val data_publicacio: String, // o Int si és any
    val num_pagines: Int,
    val sinopsi: String,
    val portada: String          // url o path local
)
