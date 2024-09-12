package com.catshouse.finance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catshouse.finance.model.Repositorio
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class MainActivityViewMoel @Inject constructor(private val repositorio: Repositorio) : ViewModel() {

    suspend fun verificarUsuarioLogado(): FirebaseUser? {
        return viewModelScope.async {
            repositorio.verificarUsuarioLogado()
        }.await()
    }
}