package com.catshouse.finance.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catshouse.finance.model.Repositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceitaViewModel @Inject constructor(private val repositorio: Repositorio) : ViewModel() {
    private val addReceitaResultadoLivedata: MutableLiveData<String> = MutableLiveData<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun dataDoSistema(): String {
        return repositorio.dataDoSistema()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addReceita(dataDoSistema: String, categoria: String, descricao: String, quantia: Double) {
        val tipo = "receita"
        viewModelScope.launch {
            val result =
                repositorio.criarMovimento(dataDoSistema, categoria, descricao, quantia, tipo)
            if (result == "Success") {
                repositorio.atualizarFinancasUsuario(quantia, tipo)
                addReceitaResultadoLivedata.value = result

            } else {
                addReceitaResultadoLivedata.value = result
            }
        }

    }

    fun getAddReceitaResultadoLiveData(): MutableLiveData<String> {
        return addReceitaResultadoLivedata
    }
}