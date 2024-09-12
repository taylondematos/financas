package com.catshouse.finance.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.catshouse.finance.model.helper.DataDoSistema
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/*
* Faz o uso da classe movimentaçao para trabalhar com o banco de dados
* */
class MovimentacaoDAO @Inject constructor() {

    private lateinit var firebaseDatabase: DatabaseReference

    //Faz a adição dos dados do usuário e retorna Success. Funções dependem dessa mensagem
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun gravarMovimentacaoDoUsuarioNoFirebase(movimentacao: Movimentacao, idDoUsuario: String): String {
        val scope = CoroutineScope(Dispatchers.IO)
        firebaseDatabase = FirebaseDatabase.getInstance().getReference()
        val resultado = scope.async {
            try {
                firebaseDatabase.child("movimentacoes")
                    .child(idDoUsuario)
                    .child(DataDoSistema.dataDoSistemaSeparada().get("ano").toString())
                    .child(DataDoSistema.dataDoSistemaSeparada().get("mes").toString())
                    .child(movimentacao.id)
                    .setValue(movimentacao)
                    .await()
                "Success"
            } catch (e: Exception) {
                "Error"
            }
        }
        return resultado.await()
    }

    //Lista os movimentos e retorna
    suspend fun listarMovimentos(
        usuarioID: String,
        ano: String,
        mes: String
    ): ArrayList<Movimentacao> {
        firebaseDatabase =
            FirebaseDatabase.getInstance().getReference().child("movimentacoes").child(usuarioID)
                .child(ano)
                .child(mes)

        val lista = ArrayList<Movimentacao>()

        suspendCoroutine { continuation ->
            firebaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children) {
                        val movimentacao = item.getValue(Movimentacao::class.java)
                        if (movimentacao != null) {
                            lista.add(movimentacao)
                        }
                    }
                    continuation.resume(Unit)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        }
        return lista
    }

    fun deletarDado(ano: String?, mes: String?, idMovimentacao: String, idDoUsuario: String) {
        val ano = ano?.let { ano } ?: "0"
        val mes = mes?.let { mes } ?: "0"
        firebaseDatabase = FirebaseDatabase.getInstance().getReference()
        try {
            firebaseDatabase.child("movimentacoes").child(idDoUsuario).child(ano).child(mes)
                .child(idMovimentacao)
                .removeValue()
        } catch (e: Exception) {
            "Error"
        }
    }


}