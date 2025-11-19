package com.app.projecteandroidsql.model

data class Usuari(val id: Int, val username : String, val password: String, val email: String) {
    constructor(username: String, password: String, email: String) : this(0, username, password, email)
}