package com.app.projecteandroidsql.model

import java.util.Date
data class Llibres(val id : Int, val titol: String, val autor: String, val data_publicacio : Date,
                   val editorial: String, val num_pag : Int, val estat: String, val sinopsis : String,
                   val portada : String) {
    constructor(titol: String, autor: String, data_publicacio: Date, editorial: String, num_pag: Int,
                estat: String, sinopsis: String, portada: String) : this(0, titol, autor, data_publicacio, editorial, num_pag, estat, sinopsis, portada)

}