package com.app.projecteandroidsql.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.app.projecteandroidsql.utils.Constants

class DB(context: Context) : SQLiteOpenHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
/* ==== S'HAN DE MODIFICAR EL TIPUS DE DADES I ELS NOMS DELS CAMPOS SEGONS LES NECESSITATS ==== */
val taulaLlibres = """
        CREATE TABLE ${Constants.TABLE_NAME_LLIBRES} (
        
            ${Constants.COLUMN_LLIBRE_ID}INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Constants.COLUMN_TITOL} TEXT NOT NULL,
            ${Constants.COLUMN_AUTOR} TEXT NOT NULL,
            ${Constants.COLUMN_DATA_PUBLICACIO} TEXT NOT NULL,
            ${Constants.COLUMN_NUM_PAGINES} TEXT NOT NULL,
            ${Constants.COLUMN_ESTAT_LLIBRE} TEXT NOT NULL,
            ${Constants.COLUMN_SINOPSIS} TEXT NOT NULL,
            ${Constants.COLUMN_PORTADA} TEXT NOT NULL
        )
    """
db?.execSQL(taulaLlibres)

val taulaUsuaris = """
        CREATE TABLE ${Constants.TABLE_NAME_USUARIS} (
            ${Constants.COLUMN_USUARI_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Constants.COLUMN_USERNAME} TEXT NOT NULL,
            ${Constants.COLUMN_PASSWORD} TEXT NOT NULL,
            ${Constants.COLUMN_EMAIL} TEXT NOT NULL
        )
    """
db?.execSQL(taulaUsuaris)

val taulaBiblioteca = """
        CREATE TABLE ${Constants.TABLE_NAME_BIBLIOTECA} (
            // IMPORTANT ${Constants.LLISTA_LLIBRES} 
        )
    """
db?.execSQL(taulaBiblioteca)

val taulaAutor = """
        CREATE TABLE ${Constants.TABLE_NAME_AUTOR} (
            ${Constants.COLUMN_AUTOR_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Constants.COLUMN_NOM} TEXT NOT NULL,
            ${Constants.COLUMN_COGNOM} TEXT NOT NULL,
            ${Constants.COLUMN_DATA_NAIXEMENT} INTEGER NOT NULL
        )
    """
db?.execSQL(taulaAutor)
}

override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {


// ==== S'HA DE MODIFICAR ====
// db?.execSQL("DROP TABLE IF EXISTS ${Constants}")
// onCreate(db)
}
}