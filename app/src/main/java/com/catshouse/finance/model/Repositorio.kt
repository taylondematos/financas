package com.catshouse.finance.model

import Usuario
import android.os.Build
import androidx.annotation.RequiresApi
import com.catshouse.finance.model.helper.Autenticacao
import com.catshouse.finance.model.helper.Base64
import com.catshouse.finance.model.helper.DataDoSistema
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/*
* Responsável por fornecer os dados para as viewmodels
* */
class Repositorio @Inject constructor(
    private val autenticacao: Autenticacao,
    private val usuarioDAO: UsuarioDAO,
    private val movimentacaoDAO: MovimentacaoDAO
) {

    private lateinit var usuario: Usuario
    private lateinit var movimentacao: Movimentacao
    private var listaMovimentacao = ArrayList<Movimentacao>()


    @RequiresApi(Build.VERSION_CODES.O)
    fun dataDoSistema(): String {
        return DataDoSistema.dataDoSistema()
    }

    //Cria o usuário no firebase mediante retorno da funçao na autenticação. A mensagem da autenticação é mostrada ao usuário
    suspend fun criarUsuario(nome: String, email: String, senha: String): String {
        usuario = Usuario(Base64.codificarBase64(email), nome, email, senha)
        val mensagem = autenticacao.cadastrarUsuario(email, senha)
        if (mensagem == "Success") {
            return usuarioDAO.gravarDadosDoUsuarioNoDB( usuario.getId(), usuario)
        } else return mensagem
    }

    suspend fun logarUsuario(email: String, senha: String): String {
        return autenticacao.logarUsuario(email, senha)
    }

    suspend fun verificarUsuarioLogado(): FirebaseUser? {
        return autenticacao.getCurrentUser()
    }

    //Cria o movimento e e depois retorna o salvamento do movimento. Retorna a mensagem do salvamento da Movimentação.
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun criarMovimento(
        data: String, categoria: String, descricao: String, quantia: Double, tipo: String
    ): String {
        movimentacao = Movimentacao(
            Base64.codificarBase64(DataDoSistema.dataDoSistema()),
            data,
            tipo,
            categoria,
            descricao,
            quantia
        )
        return salvarMovimentacao(movimentacao)
    }

    //Salva a movimentançao e retorna a mensagem de retorno da operação
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun salvarMovimentacao(movimentacao: Movimentacao): String {
        return movimentacaoDAO.gravarMovimentacaoDoUsuarioNoFirebase(
            movimentacao, Base64.codificarBase64(autenticacao.getCurrentUser()?.email.toString())
        )
    }

    /*Retorna o usuário recuperado. Caso seja nulo, significa que algo de errado aconteceu com o database.
    Por segurança, o usuário será deslogado. Um exemplo é se o usuário não ter os dados gravados no banco de dados.
    */
    suspend fun recuperarUsuario(): Usuario {
        val email = withContext(Dispatchers.IO) {
            autenticacao.getCurrentUser()?.email
        }
        if (email != null) {
            usuarioDAO.recuperarDados(Base64.codificarBase64(email))
                .getValue(Usuario::class.java)?.let {
                    usuario = it
                } ?: deslogar()
        } else {
            deslogar()
        }
        return usuario
    }


    //Recupera o user e atualiza as finaças mediante os dados que recebeu
    suspend fun atualizarFinancasUsuario(quantia: Double, categoria: String) {
        var currentUser = recuperarUsuario()
        if (currentUser != null) {
            when (categoria) {
                "despesa" -> usuario.setDespesaTotal(quantia)
                "receita" -> usuario.setReceitaTotal(quantia)
                "saldo" -> usuario.atualizarSaldo()
            }
            usuarioDAO.atualizarDados(usuario, usuario.getId())
        }
    }

    //Limpa a lista para construir uma nova lista e retorná-la
    suspend fun getListaMovimentacoes(ano: String, mes: String): java.util.ArrayList<Movimentacao> {
        listaMovimentacao.clear()
        listaMovimentacao = movimentacaoDAO.listarMovimentos(recuperarUsuario().getId(), ano, mes)
        return listaMovimentacao
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dataDoSistemaSeparada(tipo: String): String? {
        val data = DataDoSistema.dataDoSistemaSeparada()[tipo]
        return data
    }

    fun deslogar() {
        autenticacao.deslogar()
    }

    //Deleta o movimento e já atualiza os dados do usuário
    suspend fun deletarMovimento(ano: String?, mes: String?, posicaoNaLista: Int) {
        movimentacaoDAO.deletarDado(
            ano, mes, listaMovimentacao[posicaoNaLista].id, recuperarUsuario().getId()
        )
        if (listaMovimentacao[posicaoNaLista].tipo == "receita") {
            usuario.setReceitaTotal(
                (listaMovimentacao[posicaoNaLista].quantia) * (-1)
            )
        } else usuario.setDespesaTotal((listaMovimentacao[posicaoNaLista].quantia) * (-1))

        usuarioDAO.atualizarDados(usuario, usuario.getId())
    }
}
