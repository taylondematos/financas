package com.catshouse.finance.view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.catshouse.finance.databinding.ActivityMovimentoBinding
import com.catshouse.finance.viewmodel.MovimentoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovimentoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovimentoBinding
    private lateinit var textViewQuantiaMovimento: TextView
    private lateinit var textViewDataDoMovimento: TextView
    private lateinit var textViewCategoria: TextView
    private lateinit var textViewDescricao: TextView
    lateinit var ano: String
    lateinit var mes: String
    var posicaoRecycler = 0

    private val viewModel: MovimentoViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setagens()
        setContentView(binding.root)
    }

    fun intent() {
        ano = intent.getStringExtra("ano")!!
        mes = intent.getStringExtra("mes")!!
        posicaoRecycler = intent.getIntExtra("posicaoRecycler", 0)
    }

    private fun setagens() {
        binding = ActivityMovimentoBinding.inflate(layoutInflater)
        textViewQuantiaMovimento = binding.textViewQuantiaMovimento
        textViewDataDoMovimento = binding.textViewDataDoMovimento
        textViewCategoria = binding.textViewCategoria
        textViewDescricao = binding.textViewDescricao
        intent()
        observers()
    }

    fun observers() {
        viewModel.getMovimentacao(ano!!, mes!!, posicaoRecycler)

        viewModel.getLiveData()[0].observe(this) {
            textViewQuantiaMovimento.text = "$ $it"
        }
        viewModel.getLiveData()[1].observe(this) {
            textViewDataDoMovimento.text = it
        }
        viewModel.getLiveData()[2].observe(this) {
            textViewCategoria.text = it
        }
        viewModel.getLiveData()[3].observe(this) {
            textViewDescricao.text = it
        }
    }
}