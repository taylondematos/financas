package com.catshouse.finance.model.helper

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/*
* Responsável por fornecer a data do sistema
* */
class DataDoSistema {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun dataDoSistema(): String {
            val dataDoSistema = LocalDateTime.now()
            val formater = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            val data = formater.format(dataDoSistema).toString()
            return data
        }

        //Retorna um hashmap para que se obtenha já tratada a data desejada
        @RequiresApi(Build.VERSION_CODES.O)
        fun dataDoSistemaSeparada(): HashMap<String, String> {
            var data = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now()).toString().split("/")
            val hashMap = HashMap<String, String>()
            hashMap.put("dia", data[0])
            hashMap.put("mes", data[1])
            hashMap.put("ano", data[2])

            return hashMap
        }
    }
}