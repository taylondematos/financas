package com.catshouse.finance.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.catshouse.finance.R
import com.catshouse.finance.databinding.ActivityLoginBinding
import com.catshouse.finance.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var editTextEmail: EditText
    private lateinit var editTextSenha: EditText
    private lateinit var botaoLogin: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setagens()
        setContentView(binding.root)
        listenerBotaoLogin()
        aberturaTelaPrincipalObserver()
    }

    fun setagens() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        editTextEmail = binding.editTextEmail
        editTextSenha = binding.editTextSenha
        botaoLogin = binding.buttonLogin
    }

    fun listenerBotaoLogin() {
        botaoLogin.setOnClickListener {
            checagemDadosLogin()
        }
    }

    fun checagemDadosLogin() {
        if (editTextEmail.text.isBlank() || editTextSenha.text.isBlank()) {
            Toast.makeText(this, R.string.texto_prencha_todos_os_cwmpos, Toast.LENGTH_LONG).show()
        } else {
            viewModel.pedidoDeLogin(
                editTextEmail.text.toString(),
                editTextSenha.text.toString()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun aberturaTelaPrincipalObserver() {
        viewModel.getLoginStatusLiveData().observe(this) {
            if (it == true) {
                startActivity(Intent(this, TelaPrincipalActivity::class.java))
                finish()
            } else Toast.makeText(this, "Wrong e-mail or password ", Toast.LENGTH_LONG).show()

        }
    }


}
