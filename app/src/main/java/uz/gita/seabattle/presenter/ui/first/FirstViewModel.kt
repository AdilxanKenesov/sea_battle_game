package uz.gita.seabattle.presenter.ui.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.gita.seabattle.data.model.UserData
import uz.gita.seabattle.domain.repository.AuthRepository

class FirstViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _createGameSuccess = MutableSharedFlow<String>()
    val createGameSuccess = _createGameSuccess.asSharedFlow()

    private val _loginOnlySuccess = MutableSharedFlow<Unit>()
    val loginOnlySuccess = _loginOnlySuccess.asSharedFlow()

    fun createNewGame() {
        viewModelScope.launch {
            _loading.value = true

            val loginResult = repo.signUser()

            if (loginResult.isSuccess) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val userData = UserData(
                    uid = currentUser?.uid ?: "",
                    isReady = false
                )

                val gameResult = repo.createGame(userData)
                _loading.value = false

                if (gameResult.isSuccess) {
                    _createGameSuccess.emit(gameResult.getOrNull() ?: "")
                }
            }
        }
    }

    fun signInOnly() {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.signUser()
            _loading.value = false
            if (result.isSuccess) {
                _loginOnlySuccess.emit(Unit)
            }
        }
    }
}