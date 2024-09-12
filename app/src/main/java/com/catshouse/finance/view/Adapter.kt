package com.catshouse.finance.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.catshouse.finance.R
import com.catshouse.finance.databinding.ListaExibicaoTelaPrincipalBinding

class Adapter(
    private val listaTitulos: List<String>,
    private val listaQuantia: List<String>,
) :
    RecyclerView.Adapter<Adapter.MyViewHolder>() {

    private val mutableSwiperLiveData = MutableLiveData<Int>()
    private val mutableItemClicadoLiveData = MutableLiveData<Int>()


    class MyViewHolder(val binding: ListaExibicaoTelaPrincipalBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListaExibicaoTelaPrincipalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listaTitulos.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.textViewDespesa.text = listaTitulos[position]
        holder.binding.textViewDespesa.isClickable=true
        holder.binding.textViewDespesa.elevation=200f


        holder.binding.textViewQuantia.text = listaQuantia[position]
        holder.binding.textViewQuantia.isClickable=true
        holder.binding.textViewQuantia.elevation=200f



        val context = holder.itemView.context

        if (listaQuantia[position].toDouble() < 0) {
            val colorDespesa = ContextCompat.getColor(context, R.color.despesacor)
            holder.binding.textViewQuantia.setBackgroundColor(colorDespesa)
        } else {
            val colorReceita = ContextCompat.getColor(context, R.color.receitacor)
            holder.binding.textViewQuantia.setBackgroundColor(colorReceita)
        }


        listenerCliqueNormal(holder, position)
    }


    fun listenerCliqueNormal(holder: MyViewHolder, position: Int) {
        holder.binding.textViewDespesa.setOnClickListener {
            mutableItemClicadoLiveData.value = holder.layoutPosition
        }
    }

    fun listenerCliqueLongo(holder: MyViewHolder, position: Int) {
        holder.binding.textViewDespesa.setOnLongClickListener {
            true
        }
    }

    fun getSwiperLiveData(): MutableLiveData<Int> {
        return mutableSwiperLiveData
    }

    fun getMutableLiveDataItemClicado(): MutableLiveData<Int> {
        return mutableItemClicadoLiveData
    }

    fun attachSwipeToRecyclerView(recyclerView: RecyclerView) {
        val callback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mutableSwiperLiveData.value = viewHolder.adapterPosition
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}
