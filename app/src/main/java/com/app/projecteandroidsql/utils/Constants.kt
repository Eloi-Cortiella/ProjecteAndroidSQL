package com.app.projecteandroidsql.utils

import com.app.projecteandroidsql.model.Llibres


//Les constants serveixen per definir les columnes de les taules SQL, no les dades
object Constants {
    const val DATABASE_NAME = "BiblioDAMDB"
    const val DATABASE_VERSION = 1

    //Columnes taula llibres
    const val TABLE_NAME_LLIBRES = "Llibres"
    const val COLUMN_LLIBRE_ID = ""
    const val COLUM_TITOL = ""
    const val COLUM_AUTOR = ""
    const val COLUM_DATA_PUBLICACIO = ""
    const val COLUM_EDITORIAL = ""
    const val COLUM_NUM_PAGINES = ""
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
    val CONTRUCTOR_LLIBRE = arrayOf(Llibres::class)

    //Columnes taula autor
    const val TABLE_NAME_AUTOR = "Autor"
    const val COLUMN_NOM = ""
    const val COLUMN_COGNOM = ""
    const val COLUMN_NOM_ARTISTIC = ""
    const val COLUMN_DATA_NAIXEMENT = ""


}