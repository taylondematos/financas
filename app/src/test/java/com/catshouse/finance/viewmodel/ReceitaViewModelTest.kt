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
class ReceitaViewModelTest {
    lateinit var repositorio: Repositorio

    lateinit var viewModel: ReceitaViewModel

    lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        Firebase.initialize(context)
        repositorio = Mockito.mock(Repositorio::class.java)
        viewModel = ReceitaViewModel(repositorio)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addReceita() = runBlocking {
        `when`(
            repositorio.criarMovimento(
                "0",
                "receita",
                "receita",
                0.0,
                "receita"
            )
        ).thenReturn("Success")
        viewModel.addReceita("0", "receita", "receita", 0.0)
        assertEquals("Success",viewModel.getAddReceitaResultadoLiveData().value)
    }

    @Test
    fun getAddReceitaResultadoLiveData() {
    }
}