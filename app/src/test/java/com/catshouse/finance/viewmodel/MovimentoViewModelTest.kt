package com.catshouse.finance.viewmodel

import android.content.Context
import android.os.Build
import com.catshouse.finance.model.Movimentacao
import com.catshouse.finance.model.Repositorio
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class MovimentoViewModelTest {

    lateinit var repositorio: Repositorio

    lateinit var viewModel: MovimentoViewModel

    lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        Firebase.initialize(context)
        repositorio = Mockito.mock(Repositorio::class.java)
        viewModel = MovimentoViewModel(repositorio)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getMovimentacao() = runBlocking {
        val lista = ArrayList<Movimentacao>()
        val movimentacaoTeste = Movimentacao()
        lista.add(movimentacaoTeste)
        `when`(repositorio.getListaMovimentacoes("2023", "11")).thenReturn(lista)
        viewModel.getMovimentacao("2023", "11", 0)
        assertEquals(movimentacaoTeste.quantia.toString(), viewModel.getLiveData()[0].value)
    }

    @Test
    fun getLiveData() {
    }
}