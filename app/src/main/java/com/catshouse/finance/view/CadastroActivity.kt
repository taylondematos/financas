package com.catshouse.finance.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.catshouse.finance.R
import com.catshouse.finance.databinding.ActivityCadastroBinding
import com.catshouse.finance.viewmodel.CadastroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var editTextNome: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextSenha: EditText
    private lateinit var botaoCadastrar: Button
    private val viewModel: CadastroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setagens()
        setContentView(binding.root)
        listenerBotaoCadastrar()
        mensagemCadastroResultadoObserver()
    }

    private fun setagens() {
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        editTextNome = binding.editTextNome
        editTextEmail = binding.editTextEmail
        editTextSenha = binding.editTextSenha
        botaoCadastrar = binding.buttonCadastrar
    }

    private fun listenerBotaoCadastrar() {
        botaoCadastrar.setOnClickListener {
            if (editTextNome.text.isBlank() || editTextEmail.text
                    .isBlank() || editTextSenha.text.isBlank()
            ) {
                Toast.makeText(this, R.string.texto_prencha_todos_os_cwmpos, Toast.LENGTH_LONG).show()
            } else {
                viewModel.pedidoDeCadastro(
                    editTextNome.text.toString(),
                    editTextEmail.text.toString(),
                    editTextSenha.text.toString(),
                )
            }
        }
    }

    fun mensagemCadastroResultadoObserver() {
        viewModel.getCadastroResultadoLiveData().observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            if (it == "Success") {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }
    }
}