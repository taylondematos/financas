/*
* Classe que molda o usuário.
* A despesa do usuário deve ser passada em número negativo
* */

class Usuario(
    private var id: String,
    private var nome: String,
    private var email: String,
    private var senha: String,
    private var receitaTotal: Double = 0.00,
    private var despesaTotal: Double = 0.00,
    private var saldo: Double = 0.00
) {

    //construtor vazio para o HILT
    constructor() : this("","","","",0.0,0.0,0.0)

    //A despesa tem q ser passada de forma negativa pelas outras classes
    fun setDespesaTotal(quantia: Double) {
        despesaTotal = despesaTotal + quantia
        atualizarSaldo()
    }

    fun setReceitaTotal(quantia: Double) {
        receitaTotal = receitaTotal + quantia
        atualizarSaldo()
    }

    fun getId(): String {
        return id
    }

    fun getEmail(): String {
        return email
    }

    fun getSenha(): String {
        return senha
    }

    fun getNome(): String {
        return nome
    }

    fun getReceitaTotal(): Double {
        return receitaTotal
    }

    fun getDespesaTotal(): Double {
        return despesaTotal
    }

    fun getSaldo(): Double {
        atualizarSaldo()
        return saldo
    }

    fun atualizarSaldo() {
        saldo = receitaTotal + despesaTotal
    }

}
