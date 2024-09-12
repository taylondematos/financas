package com.catshouse.finance.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.catshouse.finance.R
import com.catshouse.finance.viewmodel.MainActivityViewMoel
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : IntroActivity() {



    private val viewModel: MainActivityViewMoel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TRECHO PARA SETAR O TEMA DA ACTIVITY APÓS O SPLASH SCREEN
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        setTheme(R.style.Theme_Financas)
        // setContentView(R.layout.activity_main)
        addSlidFragment()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            verificarUsuarioLogado()
        }
    }

    fun addSlidFragment() {
        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build()

        )
        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        )
        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        )
        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()
        )
        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.tela_intrologincadastro)
                .canGoForward(false)
                .build()
        )
    }

    fun addSlide() {
        isButtonBackVisible = true
        isButtonNextVisible = true
        addSlide(
            SimpleSlide.Builder()
                .title("Titulo")
                .description("Description")
                .image(R.drawable.finandjake)
                .background(android.R.color.white)
                .build()
        )
        addSlide(
            SimpleSlide.Builder()
                .title("Titulo2")
                .description("Description2")
                .background(android.R.color.white)
                .image(R.drawable.finandjake2)
                .build()

        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun verificarUsuarioLogado() {
        var curretUser = viewModel.verificarUsuarioLogado()
        if (curretUser != null) {
            startActivity(
                Intent(
                    this,
                    TelaPrincipalActivity::class.java
                )
            )
            finish()
        }
    }

    fun login(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun cadastrar(view: View) {
        startActivity(Intent(this, CadastroActivity::class.java))
    }
}