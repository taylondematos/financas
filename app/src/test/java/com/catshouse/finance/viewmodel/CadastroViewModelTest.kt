package com.catshouse.finance.viewmodel

import android.content.Context
import android.os.Build
import com.catshouse.finance.model.Repositorio
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

class CadastroViewModelTest {

    lateinit var repositorio: Repositorio

    lateinit var viewModel: CadastroViewModel

    lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        Firebase.initialize(context)
        repositorio = mock(Repositorio::class.java)
        viewModel = CadastroViewModel(repositorio)

    }

    @After
    fun tearDown() {
    }

    @Test
    fun pedidoDeCadastro() = runBlocking {
        // Configure o comportamento esperado do seu mock Repositorio
        `when`(repositorio.criarUsuario("teste", "teste@teste.com", "testeteste")).thenReturn("Sucesso")

        // Execute a função que você está testando
        viewModel.pedidoDeCadastro("teste", "teste@teste.com", "testeteste")

        // Obtenha o valor do LiveData
        val value = viewModel.getCadastroResultadoLiveData().value

        // Asserção do resultado
        assertEquals("Sucesso", value)
    }

}