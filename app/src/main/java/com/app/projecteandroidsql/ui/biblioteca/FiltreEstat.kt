package com.app.projecteandroidsql.ui.biblioteca

import com.app.projecteandroidsql.data.room.model.EstatLectura

enum class FiltreEstat(val etiqueta: String, val estat: String?) {
    TOTS("Tots", null),
    PER_LLEGIR("Per llegir", EstatLectura.PER_LLEGIR.name),
    EN_CURS("En curs", EstatLectura.EN_CURS.name),
    LLEGIT("Llegit", EstatLectura.LLEGIT.name),
}
