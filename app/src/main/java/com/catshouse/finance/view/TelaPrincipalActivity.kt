package com.catshouse.finance.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.catshouse.finance.R
import com.catshouse.finance.databinding.ActivityTelaPrincipalBinding
import com.catshouse.finance.viewmodel.TelaPrincipalViewModel
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class TelaPrincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTelaPrincipalBinding
    private lateinit var fabMenu: FloatingActionMenu
    private lateinit var fabReceita: FloatingActionButton
    private lateinit var fabDespesa: FloatingActionButton
    private lateinit var textViewSaudacao: TextView
    private lateinit var textViewSaldo: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var buttonLogout: ImageButton
    private val viewModel: TelaPrincipalViewModel by viewModels()

    private var mesSendoMostrado: String? = null
    private var anoSendoMostrado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setagens()
    }

    override fun onPause() {
        super.onPause()
        fabMenu.close(true)
    }

    override fun onResume() {
        super.onResume()
        observerSaldoLiveData()
        observerSaudacaoLiveData()
        observableTitulosLiveData()
        atualizarLista()

    }

    fun setagens() {
        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        fabMenu = binding.fabMenu
        fabReceita = binding.fabReceita
        fabDespesa = binding.fabDespesa
        textViewSaldo = binding.textViewSaldo
        textViewSaudacao = binding.textViewSaudacao
        calendarView = binding.calendarView
        recyclerView = binding.recyclerView
        buttonLogout = binding.buttonLogout
        anoSendoMostrado = viewModel.dataDoSistemaSeparada("ano")
        mesSendoMostrado = viewModel.dataDoSistemaSeparada("mes")
        setagemRecyclerView()
        setContentView(binding.root)
        listenerDoFab()
        listenerCalendario()
        listenerButtonLogout()
    }

    fun popUpMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_principal, popupMenu.menu)
        popupMenu.gravity = Gravity.END
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    viewModel.deslogar()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    fun listenerButtonLogout() {
        buttonLogout.setOnClickListener {
            popUpMenu(buttonLogout)
        }
    }

    fun listenerDoFab() {
        fabReceita.setOnClickListener { startActivity(Intent(this, ReceitaActivity::class.java)) }
        fabDespesa.setOnClickListener { startActivity(Intent(this, DespesaActivity::class.java)) }
    }

    fun listenerCalendario() {
        calendarView.setOnMonthChangedListener { widget, date ->
            mesSendoMostrado = date.month.toString()
            atualizarLista()
        }
    }

    fun observerSaldoLiveData() {
        viewModel.atualizarSaldoUsuario()
        viewModel.getSaldoLiveData().observe(this) {
            textViewSaldo.text = "$ ${it.toString()}"
        }
    }

    fun observerSaudacaoLiveData() {
        viewModel.getNomeDoUsuarioLiveData().observe(this) {
            textViewSaudacao.text = "Hello ${it}"
        }
    }

    fun setagemRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        observableTitulosLiveData()
    }

    fun atualizarLista() {
        anoSendoMostrado?.let { mesSendoMostrado?.let { it1 -> viewModel.updateLista(it, it1) } }
    }

    fun observableTitulosLiveData() {
        viewModel.getListTitulosLiveData().observe(this) { listaDeTitulos ->

            viewModel.getListQuantiasLivedata().observe(this) { listaDeQuantias ->

                recyclerView.setItemViewCacheSize(listaDeTitulos.size)
                val adapter = viewModel.creatAdapter(listaDeTitulos, listaDeQuantias)
                recyclerView.adapter = adapter
                adapter.attachSwipeToRecyclerView(binding.recyclerView)

                adapter.getMutableLiveDataItemClicado().observe(this) {
                    var intent = Intent(this, MovimentoActivity::class.java)
                    intent.putExtra("mes", mesSendoMostrado)
                    intent.putExtra("ano", anoSendoMostrado)
                    intent.putExtra("posicaoRecycler", it)

                    startActivity(intent)
                }

                adapter.getSwiperLiveData().observe(this) {
                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setTitle("Delete")
                    alertDialog.setMessage("Do you want to delete this item?")
                    alertDialog.setCancelable(false)
                    alertDialog.setPositiveButton(
                        "Yes",
                        DialogInterface.OnClickListener { dialog, which ->
                            viewModel.deletarMovimento(anoSendoMostrado, mesSendoMostrado, it)
                            adapter.notifyItemRemoved(it)
                        })
                    alertDialog.setNegativeButton(
                        "No",
                        DialogInterface.OnClickListener { dialog, which ->
                            observableTitulosLiveData()
                            dialog.dismiss()
                        })
                    alertDialog.create().show()
                }
            }
        }
    }


}



