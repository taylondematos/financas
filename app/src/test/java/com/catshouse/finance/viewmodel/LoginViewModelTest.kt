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
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class LoginViewModelTest {
    lateinit var repositorio: Repositorio

    lateinit var viewModel: LoginViewModel

    lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        Firebase.initialize(context)
        repositorio = Mockito.mock(Repositorio::class.java)
        viewModel = LoginViewModel(repositorio)

    }

    @After
    fun tearDown() {
    }

    @Test
    fun pedidoDeLogin() = runBlocking {
        val email = "teste@teste.com"
        val senha = "testeTeste"

        `when`(repositorio.logarUsuario("teste@teste.com", "testeTeste")).thenReturn("Success")
        viewModel.pedidoDeLogin(email, senha)
        assertEquals(true, viewModel.getLoginStatusLiveData().value)
    }

    @Test
    fun getLoginStatusLiveData() {
    }
}