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
class DespesaViewModel @Inject constructor(private val repositorio: Repositorio) : ViewModel() {

    private val addDespesaLivedata: MutableLiveData<String> = MutableLiveData<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun dataDoSistema(): String {
        return repositorio.dataDoSistema()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addDespesa(dataDoSistema: String, categoria: String, descricao: String, quantia: Double) {
        var despesa = quantia * (-1)
        val tipo = "despesa"
        viewModelScope.launch {
            val result =
                repositorio.criarMovimento(dataDoSistema, categoria, descricao, despesa, tipo)

            if (result == "Success") {
                repositorio.atualizarFinancasUsuario(despesa, tipo)
                addDespesaLivedata.value = result

            } else {
                addDespesaLivedata.value = result
            }
        }

    }

    fun getAddDespesaResultadoLiveData(): MutableLiveData<String> {
        return addDespesaLivedata
    }

}