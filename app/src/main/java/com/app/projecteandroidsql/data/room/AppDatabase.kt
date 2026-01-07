package com.app.projecteandroidsql.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.projecteandroidsql.data.room.dao.EntradaBibliotecaDao
import com.app.projecteandroidsql.data.room.dao.LlibreDao
import com.app.projecteandroidsql.data.room.dao.UsuariDao
import com.app.projecteandroidsql.data.room.entity.EntradaBibliotecaEntity
import com.app.projecteandroidsql.data.room.entity.LlibreEntity
import com.app.projecteandroidsql.data.room.entity.UsuariEntity

@Database(
    entities = [UsuariEntity::class, LlibreEntity::class, EntradaBibliotecaEntity::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuariDao(): UsuariDao
    abstract fun llibreDao(): LlibreDao
    abstract fun entradaBibliotecaDao(): EntradaBibliotecaDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "biblio_room.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
