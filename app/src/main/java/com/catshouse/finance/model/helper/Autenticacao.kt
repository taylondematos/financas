package com.catshouse.finance.model.helper

import com.google.firebase.auth.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/*
* Classe que trata da autenticaçao : do cadastro, login, logout e retorno do usuário logado
*/

class Autenticacao @Inject constructor() {

    private val autenticador: FirebaseAuth = FirebaseAuth.getInstance()

    //Faz o cadastro do usuário com o firebase auth
    suspend fun cadastrarUsuario(email: String, senha: String): String {
        var mensagem: Deferred<String> =
            CoroutineScope(Dispatchers.IO).async {
                try {
                    autenticador.createUserWithEmailAndPassword(email, senha).await()
                    "Success"
                } catch (e: FirebaseAuthWeakPasswordException) {
                    "Use a stronger password"
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    "Use a real e-mail address"
                } catch (e: FirebaseAuthUserCollisionException) {
                    "This account already exists"
                } catch (e: FirebaseAuthEmailException) {
                    "Something went wrong"
                }
            }

        return mensagem.await()
    }

    //faz o login do usuário. A mensagem de retorno abaixo atualmente não está sendo mostrada ao usuário
    suspend fun logarUsuario(email: String, senha: String): String {
        return try {
            CoroutineScope(Dispatchers.IO).async {
                autenticador.signInWithEmailAndPassword(email, senha).await()
                "Success"
            }.await()
        } catch (e: FirebaseAuthInvalidUserException) {
            "E-mail e senha não correspondem a um usuário cadastrado"
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            "Digite um e-mail real"
        } catch (e: java.lang.Exception) {
            "Erro ao logar usuário"
        }
    }

    //Retorna o usuário logado ao firebase. Caso não logado, null
    suspend fun getCurrentUser(): FirebaseUser? {
        return withContext(Dispatchers.IO) {
            autenticador.currentUser
        }
    }

    //faz o logout do usuário
    fun deslogar() {
        autenticador.signOut()
    }


}