package com.app.projecteandroidsql.data.room.dao

data class LlibreBibliotecaItem(
    val idEntrada: Long,
    val idLlibre: Long,
    val titol: String,
    val autor: String,
    val estatLectura: String
)