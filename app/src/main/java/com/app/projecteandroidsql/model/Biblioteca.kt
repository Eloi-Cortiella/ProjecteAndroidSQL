package com.app.projecteandroidsql.model

data class Biblioteca(val id : Int, val constructor_llibre : Array<Llibres>) {
    constructor() : this(0, emptyArray())
}