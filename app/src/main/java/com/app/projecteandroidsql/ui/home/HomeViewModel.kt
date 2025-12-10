package com.app.projecteandroidsql.ui.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    fun validarTitol(title: String): Boolean {
        return title.trim().length >= 3
    }

    fun formatTitol(title: String): String {
        return title.trim().replaceFirstChar { it.uppercase() }
    }
}