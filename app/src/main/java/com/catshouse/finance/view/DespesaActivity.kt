package com.catshouse.finance.view

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import com.catshouse.finance.R
import com.catshouse.finance.databinding.ActivityDespesaBinding
import com.catshouse.finance.viewmodel.DespesaViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DespesaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDespesaBinding
    private lateinit var editTextDespesa: EditText
    private lateinit var editTextData: EditText
    private lateinit var editTextTextCategoria: EditText
    private lateinit var editTextTextDescricao: EditText
    private lateinit var floatingActionButtonAdd: FloatingActionButton
    private lateinit var cardView: CardView
    private val viewModel: DespesaViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setagens()
        setContentView(binding.root)
        listenerBotaoAddDespesa()
        mensagemObserverResultadoAddDespesa()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setagens() {
        binding = ActivityDespesaBinding.inflate(layoutInflater)
        editTextData = binding.editTextData
        editTextDespesa = binding.editTextDespesa
        editTextTextCategoria = binding.editTextCategoria
        editTextTextDescricao = binding.editTextDescricao
        floatingActionButtonAdd = binding.floatingActionButtonAdd
        editTextData.text = Editable.Factory.getInstance().newEditable(viewModel.dataDoSistema())
        cardView = binding.cardView


        var shapeAppearanceModel: ShapeAppearanceModel.Builder = ShapeAppearanceModel().toBuilder()
        shapeAppearanceModel.setTopLeftCorner(
            CornerFamily.ROUNDED,
            CornerSize { return@CornerSize 200F }).setTopRightCorner(CornerFamily.ROUNDED,
            CornerSize { return@CornerSize 200F })

        binding.cardView.shapeAppearanceModel = shapeAppearanceModel.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun listenerBotaoAddDespesa() {
        val mes = intent.extras
        floatingActionButtonAdd.setOnClickListener {
            if (
                !(editTextData.text.toString().isBlank()
                        || editTextTextCategoria.text.toString().isBlank()
                        || editTextTextDescricao.text.toString().isBlank()
                        || editTextDespesa.text.toString().isBlank()
                        )
            ) {
                viewModel.addDespesa(
                    editTextData.text.toString(),
                    editTextTextCategoria.text.toString(),
                    editTextTextDescricao.text.toString(),
                    editTextDespesa.text.toString().toDouble()
                )

            } else Toast.makeText(this, R.string.texto_prencha_todos_os_cwmpos, Toast.LENGTH_LONG).show()
        }
    }

    fun mensagemObserverResultadoAddDespesa() {
        viewModel.getAddDespesaResultadoLiveData().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            if (it == "Success") finish()
        })
    }

}