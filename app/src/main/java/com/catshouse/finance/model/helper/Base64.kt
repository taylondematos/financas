package com.catshouse.finance.model.helper

/*
* Responsável por codificar em base64 os dados do usuário
* */
class Base64 {
    companion object {
        fun codificarBase64(string: String): String {
            return android.util.Base64.encodeToString(
                string.encodeToByteArray(),
                android.util.Base64.DEFAULT
            ).replace("\n", "")
        }
    }
}