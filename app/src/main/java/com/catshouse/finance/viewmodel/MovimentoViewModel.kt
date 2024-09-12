package com.catshouse.finance.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catshouse.finance.model.Repositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimentoViewModel @Inject constructor(
    private val repositorio: Repositorio
) : ViewModel() {

    private val mutableLiveDataQuantia = MutableLiveData<String>()
    private val mutableLiveDatadataDoMovimento = MutableLiveData<String>()
    private val mutableLiveDatacategoria = MutableLiveData<String>()
    private val mutableLiveDatadescricao = MutableLiveData<String>()

    fun getMovimentacao(
        ano: String,
        mes: String,
        posicaoRecyclerView: Int,
    ) {
        viewModelScope.launch {
            val quantia =
                repositorio.getListaMovimentacoes(ano, mes)[posicaoRecyclerView].quantia.toString()
            val dataDoMovimento =
                repositorio.getListaMovimentacoes(ano, mes)[posicaoRecyclerView].data
            val categoria =
                repositorio.getListaMovimentacoes(ano, mes)[posicaoRecyclerView].categoria
            val descricao =
                repositorio.getListaMovimentacoes(ano, mes)[posicaoRecyclerView].descricao

            mutableLiveDataQuantia.value = quantia
            mutableLiveDatadataDoMovimento.value = dataDoMovimento
            mutableLiveDatacategoria.value = categoria
            mutableLiveDatadescricao.value = descricao

        }
    }

    fun getLiveData(): List<MutableLiveData<String>> {
        return listOf(mutableLiveDataQuantia,mutableLiveDatadataDoMovimento,mutableLiveDatacategoria,mutableLiveDatadescricao)
    }
}