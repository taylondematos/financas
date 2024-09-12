package com.catshouse.finance.model

import Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsuarioDAO @Inject constructor() {

    private lateinit var firebaseDatabase: DatabaseReference

    //
    suspend fun gravarDadosDoUsuarioNoDB(idDoUsuario: String, valor: Usuario): String {
        val scope = CoroutineScope(Dispatchers.IO)
        firebaseDatabase = FirebaseDatabase.getInstance().getReference()
        val resultado = scope.async {
            try {
                firebaseDatabase.child("usuarios").child(idDoUsuario).setValue(valor).await()
                "Success"
            } catch (e: Exception) {
                "Error"
            }
        }
        return resultado.await()
    }

    suspend fun recuperarDados(segundoNo: String): DataSnapshot {
        val scope = CoroutineScope(Dispatchers.IO)
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference()
        val dataSnapshot = scope.async {
            firebaseDatabase.child("usuarios").child(segundoNo).get().await()
        }
        return dataSnapshot.await()
    }

    fun atualizarDados(any: Any?, segundoNo: String) {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference()
        any?.let { firebaseDatabase.child("usuarios").child(segundoNo).setValue(any) }
    }


}