package com.catshouse.finance.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catshouse.finance.model.Repositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repositorio: Repositorio
) : ViewModel() {

    private val loginStatusliveData = MutableLiveData<Boolean>()

    fun pedidoDeLogin(email: String, senha: String) {
        viewModelScope.launch {
            if (repositorio.logarUsuario(email, senha) == "Success")
                loginStatusliveData.value = true
            else loginStatusliveData.value = false
        }
    }

    fun getLoginStatusLiveData(): MutableLiveData<Boolean> {
        return loginStatusliveData
    }


}