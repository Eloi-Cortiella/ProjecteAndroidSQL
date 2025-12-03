package com.app.projecteandroidsql.utils

import com.app.projecteandroidsql.model.Llibres


//Les constants serveixen per definir les columnes de les taules SQL, no les dades
/**
 * Classe object per definir les constants de la base de dades
 */
object Constants {
    const val DATABASE_NAME = "BiblioDAMDB"
    const val DATABASE_VERSION = 1

    //Columnes taula llibres
    const val TABLE_NAME_LLIBRES = "Llibres"
    const val COLUMN_LLIBRE_ID = ""
    const val COLUMN_TITOL = ""
    const val COLUMN_AUTOR = ""
    const val COLUMN_DATA_PUBLICACIO = ""
    const val COLUMN_EDITORIAL = ""
    const val COLUMN_NUM_PAGINES = ""
    const val COLUMN_ESTAT_LLIBRE = ""
    const val COLUMN_SINOPSIS = ""
    const val COLUMN_PORTADA = ""


    //Columnes taula usuaris
    const val TABLE_NAME_USUARIS = "Usuaris"
    const val COLUMN_USUARI_ID = ""
    const val COLUMN_USERNAME = ""
    const val COLUMN_PASSWORD = ""
    const val COLUMN_EMAIL = ""


    //Columnes taula biblioteca
    const val TABLE_NAME_BIBLIOTECA = "Biblioteca"
    //Importem la classe Llibres per poder-la utilitzar en la taula Biblioteca
    val LLISTA_LLIBRES = Llibres::class

    //Columnes taula autor
    const val TABLE_NAME_AUTOR = "Autor"
    const val COLUMN_AUTOR_ID = ""
    const val COLUMN_NOM = ""
    const val COLUMN_COGNOM = ""
    const val COLUMN_NOM_ARTISTIC = ""
    const val COLUMN_DATA_NAIXEMENT = ""


}