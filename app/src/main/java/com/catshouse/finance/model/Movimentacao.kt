package com.catshouse.finance.model
/*
* Classe que define o modelo de movimentaçao
* O atributo tipo: despesa ou receita
*
* */
class Movimentacao(
    val id: String,
    val data: String,
    val tipo: String,
    val categoria: String,
    val descricao: String,
    val quantia: Double
) {

    //Construtor vazio para o Hilt e para outras funçoes
    constructor() : this("", "", "", "", "", 0.00)

}