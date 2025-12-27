package com.app.projecteandroidsql.utils

import com.app.projecteandroidsql.model.Llibres


//Les constants serveixen per definir les columnes de les taules SQL, no les dades
/**
 * Classe object per definir les constants de la base de dades
 */
object Constants {
    const val DATABASE_NAME = "BiblioDAMDB"
    const val DATABASE_VERSION = 2

    //Columnes taula llibres
    const val TABLE_NAME_LLIBRES = "Llibres"
    const val COLUMN_LLIBRE_ID = "id"
    const val COLUMN_TITOL = "titol"
    const val COLUMN_AUTOR = "autor"
    const val COLUMN_DATA_PUBLICACIO = "data_publicacio"
    //const val COLUMN_EDITORIAL = ""
    const val COLUMN_NUM_PAGINES = "num_pagines"
    const val COLUMN_ESTAT_LLIBRE = "estat"
    const val COLUMN_SINOPSIS = "sinopsis"
    const val COLUMN_PORTADA = "portada"


    //Columnes taula usuaris
    const val TABLE_NAME_USUARIS = "Usuaris"
    const val COLUMN_USUARI_ID = "id"
    const val COLUMN_USERNAME = "username"
    const val COLUMN_PASSWORD = "password"
    const val COLUMN_EMAIL = "email"


    //Columnes taula biblioteca
    const val TABLE_NAME_BIBLIOTECA = "Biblioteca"
    //Importem la classe Llibres per poder-la utilitzar en la taula Biblioteca
    val LLISTA_LLIBRES = Llibres::class

    //Columnes taula autor
    const val TABLE_NAME_AUTOR = "Autor"
    const val COLUMN_AUTOR_ID = "id"
    const val COLUMN_NOM = "nom"
    const val COLUMN_COGNOM = "cognom"
    const val COLUMN_NOM_ARTISTIC = "nom_artistic"
    const val COLUMN_DATA_NAIXEMENT = "data_naixement"


}