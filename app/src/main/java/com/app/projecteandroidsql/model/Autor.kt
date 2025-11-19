package com.app.projecteandroidsql.model

data class Autor(val id: Int, val nom : String, val cognom : String, val nom_artistic: String, val data_naixement : String) {
    constructor(nom: String, cognom: String, nom_artistic: String, data_naixement: String) : this(0, nom, cognom, nom_artistic, data_naixement)
}