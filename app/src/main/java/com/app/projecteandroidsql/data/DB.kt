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
        
            ${Constants.COLUMN_TITOL}INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Constants.COLUMN_AUTOR} TEXT NOT NULL,
            ${Constants.COLUMN_DATA_PUBLICACIO} INTEGER NOT NULL,
            ${Constants.COLUMN_EDITORIAL} TEXT NOT NULL,
            ${Constants.COLUMN_NUM_PAGINES} FLOAT NOT NULL
            ${Constants.COLUMN_ESTAT_LLIBRE} FLOAT NOT NULL
            ${Constants.COLUMN_SINOPSIS} FLOAT NOT NULL
            ${Constants.COLUMN_PORTADA} FLOAT NOT NULL
        )
    """
db?.execSQL(taulaLlibres)

val taulaUsuaris = """
        CREATE TABLE ${Constants.TABLE_NAME_USUARIS} (
            ${Constants.COLUMN_USERNAME} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Constants.COLUMN_CONTRASENYA} TEXT NOT NULL,
            ${Constants.COLUMN_CORREU} INTEGER NOT NULL,
        )
    """
db?.execSQL(taulaUsuaris)

val taulaBiblioteca = """
        CREATE TABLE ${Constants.TABLE_NAME_BIBLIOTECA} (
            // IMPORTANT ${Constants.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        )
    """
db?.execSQL(taulaBiblioteca)

val taulaAutor = """
        CREATE TABLE ${Constants.TABLE_NAME_AUTOR} (
            ${Constants.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Constants.COLUMN_NOM} INTEGER NOT NULL,
            ${Constants.COLUMN_COGNOM} TEXT NOT NULL,
            ${Constants.COLUMN_DATA_NAIXEMENT} FLOAT NOT NULL
        )
    """
db?.execSQL(createTable)
}

override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {


// ==== S'HA DE MODIFICAR ====
// db?.execSQL("DROP TABLE IF EXISTS ${Constants}")
// onCreate(db)
}
}