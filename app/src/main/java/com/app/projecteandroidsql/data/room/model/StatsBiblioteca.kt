package com.app.projecteandroidsql.data.room.model

import androidx.room.ColumnInfo

data class StatsBiblioteca(
    @ColumnInfo(name = "llegits") val llegits: Int,
    @ColumnInfo(name = "enCurs") val enCurs: Int,
    @ColumnInfo(name = "perLlegir") val perLlegir: Int
)