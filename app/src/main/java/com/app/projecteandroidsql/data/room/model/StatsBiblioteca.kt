package com.app.projecteandroidsql.data.room.model

import androidx.room.ColumnInfo

data class StatsBiblioteca(
    @ColumnInfo(name = "total") val total: Int,
    @ColumnInfo(name = "llegits") val llegits: Int,
    @ColumnInfo(name = "per_llegir") val perLlegir: Int
)
