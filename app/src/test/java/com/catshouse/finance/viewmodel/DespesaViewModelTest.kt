package com.catshouse.finance.viewmodel

import android.content.Context
import android.os.Build
import com.catshouse.finance.model.Movimentacao
import com.catshouse.finance.model.MovimentacaoDAO
import com.catshouse.finance.model.Repositorio
import com.catshouse.finance.model.helper.DataDoSistema
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))

class DespesaViewModelTest {

    lateinit var repositorio: Repositorio
    lateinit var viewModel: DespesaViewModel
    lateinit var movimentacaoDAO: MovimentacaoDAO
    lateinit var movimentacao: Movimentacao
    lateinit var context: Context


    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        Firebase.initialize(context)
        repositorio = mock(Repositorio::class.java)
        viewModel = DespesaViewModel(repositorio)
        movimentacaoDAO = MovimentacaoDAO()
        movimentacao = Movimentacao(
            "kkjklkk",
            "01/01/2023 01:01:01",
            "despesa",
            "despesa",
            "despesa",
            1000.00
        )
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addDespesa() = runBlocking {
        val data = DataDoSistema.dataDoSistema()
        `when`(
            repositorio.criarMovimento(
                data,
                "despesa",
                "despesa",
                -1000.00,
                "despesa"
            )
        ).thenReturn("Success")

        viewModel.addDespesa(data, "despesa", "despesa", 1000.00)

        assertEquals("Success", viewModel.getAddDespesaResultadoLiveData().value)
    }

}