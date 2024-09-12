package com.catshouse.finance.viewmodel

import Usuario
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
class TelaPrincipalViewModelTest {

    lateinit var repositorio: Repositorio

    lateinit var viewModel: TelaPrincipalViewModel

    lateinit var context: Context

    lateinit var usuario: Usuario


    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        Firebase.initialize(context)
        repositorio = Mockito.mock(Repositorio::class.java)
        viewModel = TelaPrincipalViewModel(repositorio)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun atualizarSaldoUsuario() = runBlocking {
        usuario = Usuario("0", "0", "0", "0", 2.0, -1.0, 0.0)
        `when`(repositorio.recuperarUsuario()).thenReturn(usuario)
        viewModel.atualizarSaldoUsuario()
        assertEquals(1.0, usuario.getSaldo(), 0.0)
    }

    @Test
    fun updateLista() = runBlocking {
        val movimentacao1 = Movimentacao("0", "0", "despesa", "teste1", "0", 1.0)
        val movimentacao2 = Movimentacao("0", "0", "despesa", "teste2", "0", 2.0)
        val lista = ArrayList<Movimentacao>()
        lista.add(movimentacao1)
        lista.add(movimentacao2)
        `when`(repositorio.getListaMovimentacoes("2023", "11")).thenReturn(lista)
        viewModel.updateLista("2023", "11")
        assertEquals(
            movimentacao1.quantia.toString(),
            viewModel.getListQuantiasLivedata().value?.get(0)
        )
        assertEquals(
            movimentacao2.quantia.toString(),
            viewModel.getListQuantiasLivedata().value?.get(1)
        )
    }

    @Test
    fun creatAdapter() = runBlocking {
        val movimentacao1 = Movimentacao("0", "0", "despesa", "teste1", "0", 1.0)
        val movimentacao2 = Movimentacao("0", "0", "despesa", "teste2", "0", 2.0)
        val lista = ArrayList<Movimentacao>()
        val listaQuantias = ArrayList<String>()
        val listaDeTitulos = ArrayList<String>()
        lista.add(movimentacao1)
        lista.add(movimentacao2)

        listaQuantias.add(movimentacao1.quantia.toString())
        listaQuantias.add(movimentacao1.quantia.toString())
        listaDeTitulos.add(movimentacao1.categoria.toString())
        listaDeTitulos.add(movimentacao2.categoria.toString())


        assertNotNull(viewModel.creatAdapter(listaQuantias, listaDeTitulos))
    }

    @Test
    fun deletarMovimento() = runBlocking {


        val movimentacao1 = Movimentacao("1", "0", "despesa", "teste1", "0", -1.0)
        val movimentacao2 = Movimentacao("2", "0", "receita", "teste2", "0", 2.0)
        val lista = ArrayList<Movimentacao>()
        lista.add(movimentacao1)
        lista.add(movimentacao2)
        `when`(repositorio.getListaMovimentacoes("2023", "11")).thenReturn(lista)

        usuario = Usuario("0", "0", "0", "0", 0.0, 0.0, 0.0)
        usuario.setReceitaTotal(movimentacao2.quantia)
        usuario.setDespesaTotal(movimentacao1.quantia)
        `when`(repositorio.recuperarUsuario()).thenReturn(usuario)

        viewModel.deletarMovimento("2023", "11", 0)

        assertEquals( 1.0, usuario.getSaldo(),0.0)
    }


}