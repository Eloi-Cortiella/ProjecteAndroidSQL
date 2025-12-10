package com.app.projecteandroidsql.utils

import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.activity.ComponentActivity
import com.app.projecteandroidsql.data.DB

/**
 * Classe per registrar les taules de la base de dades
 */
class CrearBDD : ComponentActivity() {
    var db: DB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            // Inicialitzem la base de dades. Millor dit, del package data, importem la classe DB amb les seves propietats
            this.db = DB(this)
            Log.d(tag, "Base de dades inicialitzada")

            // Load and display movies in Logcat
            carregarTaules()
        } catch (e: Exception) {
            print("Error al importar la configuració de data/DB")
        }
    }

    private fun carregarTaules() {
        Log.d(tag, "Començant la càrrega de les taules de Classe DB")

        //Carreguem la taula autor
        try {
            this.db!!.getReadableDatabase().query(
                Constants.TABLE_NAME_AUTOR,
                null,
                null,
                null,
                null,
                null,
                null
            ).use { cursor ->
                Log.d(tag, "Consulta SQL executada. Nombre de resultats: " + cursor.getCount())
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_AUTOR_ID))
                    val nom = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_NOM))
                    val cognom = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_COGNOM))
                    val nom_artistic = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_NOM_ARTISTIC))
                    val data = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_DATA_NAIXEMENT))

                    Log.i(
                        tag, String.format(
                            "Llibre creat: ID=%d, Nom=%s, Cognom=%s, Nom_Artistic=%s, Data=%d",
                            id, nom, cognom, nom_artistic, data
                        )
                    )
                }
            }
        } catch (e: Exception) {
            print("Hi ha algun error al carregar la taula auto")
        }

        //Carreguem la taula llibres
        try {
            this.db!!.getReadableDatabase().query(
                Constants.TABLE_NAME_LLIBRES,
                null,
                null,
                null,
                null,
                null,
                null
            ).use { cursor ->
                Log.d(tag, "Consulta SQL executada. Nombre de resultats: " + cursor.getCount())
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_LLIBRE_ID))
                    val titol = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_TITOL))
                    val autor = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_AUTOR))
                    val data_publicacio = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_NOM_ARTISTIC))
                    val editorial = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_DATA_NAIXEMENT))
                    val num_pagines = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_NUM_PAGINES))
                    val estat = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_ESTAT_LLIBRE))
                    val sinopsis = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_SINOPSIS))
                    val portada = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_PORTADA))

                    Log.i(
                        tag, String.format(
                            "Llibre creat: ID=%d, Titol=%s, Autor=%s, Data publicació=%d, Editorial=%s, Num pagines=%d, Estat=%s, Sinopsis=%s, Portada=%s",
                            id, titol, autor, data_publicacio, editorial, num_pagines, estat, sinopsis, portada
                        )
                    )
                }
            }
        } catch (e: Exception) {
            print("Hi ha hagut algun error al intentar importar la taula llibres")
        }

        //Carreguem la taula usuaris
        try {
            this.db!!.getReadableDatabase().query(
                Constants.TABLE_NAME_USUARIS,
                null,
                null,
                null,
                null,
                null,
                null
            ).use { cursor ->
                Log.d(tag, "Consulta SQL executada. Nombre de resultats: " + cursor.getCount())
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_LLIBRE_ID))
                    val username = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_TITOL))
                    val password = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_AUTOR))
                    val email = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_NOM_ARTISTIC))

                    Log.i(
                        tag, String.format(
                            "Llibre creat: ID=%d, Username=%s, Password=%s, Email=%s",
                            id, username, password, email
                        )
                    )
                }
            }
        } catch (e: Exception) {
            print("Hi ha hagut algun error al intentar importar la taula usuaris")
        }


        //Carreguem la taula usuaris
        this.db!!.getReadableDatabase().query(
            Constants.TABLE_NAME_BIBLIOTECA,
            null,
            null,
            null,
            null,
            null,
            null
        )
        Log.d(tag, "Càrrega de les taules finalitzada")
    }


    companion object {
        const val tag: String = "CrearBDD"
    }
}