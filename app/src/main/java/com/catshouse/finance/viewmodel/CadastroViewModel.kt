package com.catshouse.finance.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catshouse.finance.model.Repositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CadastroViewModel @Inject constructor(
    private val repositorio: Repositorio
) : ViewModel() {

    private var cadastrResultadoLiveData = MutableLiveData<String>()


    fun pedidoDeCadastro(nome: String, email: String, senha: String) {
        viewModelScope.launch {
            cadastrResultadoLiveData.value = repositorio.criarUsuario(nome, email, senha)
        }
    }

    fun getCadastroResultadoLiveData(): MutableLiveData<String> {

        return cadastrResultadoLiveData
    }

}