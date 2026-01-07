package com.app.projecteandroidsql.data.room

import androidx.room.TypeConverter
import com.app.projecteandroidsql.data.room.model.EstatLectura

class RoomConverters {

    @TypeConverter
    fun fromEstatLectura(value: EstatLectura?): String? = value?.name

    @TypeConverter
    fun toEstatLectura(value: String?): EstatLectura? =
        value?.let { EstatLectura.valueOf(it) }
}
