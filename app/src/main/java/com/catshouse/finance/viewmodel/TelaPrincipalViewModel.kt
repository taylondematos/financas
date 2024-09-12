package com.catshouse.finance.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catshouse.finance.model.Repositorio
import com.catshouse.finance.view.Adapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelaPrincipalViewModel @Inject constructor(
    private val repositorio: Repositorio
) : ViewModel() {

    private val saldoLiveData = MutableLiveData<Double>()
    private val nomeUsuarioLiveData = MutableLiveData<String>()
    private val listaTitulosLiveData = MutableLiveData<List<String>>()
    private val listaQuantiasLiveData = MutableLiveData<List<String>>()


    fun atualizarSaldoUsuario() {
        val categoria = "saldo"
        viewModelScope.launch {
            repositorio.atualizarFinancasUsuario(0.00, categoria)
        }
    }

    fun getSaldoLiveData(): MutableLiveData<Double> {
        viewModelScope.launch {
            repositorio.recuperarUsuario()?.getSaldo()?.let {
                saldoLiveData.value = it
            }
        }
        return saldoLiveData
    }

    fun getNomeDoUsuarioLiveData(): MutableLiveData<String> {
        viewModelScope.launch {
            repositorio.recuperarUsuario()?.getNome()?.let {
                nomeUsuarioLiveData.value = it
            }
        }
        return nomeUsuarioLiveData
    }

    fun updateLista(ano: String, mes: String) {
        viewModelScope.launch {
            val listaTitulos = mutableListOf<String>()
            val listaQuantias = mutableListOf<String>()
            for (item in repositorio.getListaMovimentacoes(ano, mes)) {
                listaTitulos.add(item.categoria)
                listaQuantias.add(item.quantia.toString())
            }
            listaTitulosLiveData.value = listaTitulos
            listaQuantiasLiveData.value = listaQuantias
        }
    }

    fun getListTitulosLiveData(): MutableLiveData<List<String>> {
        return listaTitulosLiveData
    }

    fun getListQuantiasLivedata(): MutableLiveData<List<String>> {
        return listaQuantiasLiveData
    }

    fun creatAdapter(listaTitulos: List<String>, listaQuantias: List<String>): Adapter {
        return Adapter(listaTitulos, listaQuantias)
    }

    fun deletarMovimento(ano: String?, mes: String?, posicaoNaLista: Int) {
        viewModelScope.launch {
            repositorio.deletarMovimento(ano, mes, posicaoNaLista)
            getSaldoLiveData()
            ano?.let {val ano = it
                mes?.let {val mes = it
                    updateLista(ano,mes)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dataDoSistemaSeparada(tipo: String): String? {
        val data = repositorio.dataDoSistemaSeparada(tipo)
        return data
    }

    fun deslogar() {
        repositorio.deslogar()
    }

}