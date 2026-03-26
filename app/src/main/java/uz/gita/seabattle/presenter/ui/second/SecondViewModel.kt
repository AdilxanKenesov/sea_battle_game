package uz.gita.seabattle.presenter.ui.second

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

class SecondViewModel(private val repo: AuthRepository): ViewModel() {
    private val _joinSuccess = MutableSharedFlow<Unit>()
    val joinSuccess = _joinSuccess.asSharedFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun joinGame(gameId: String) {

        viewModelScope.launch {
            _loading.value = true
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userData = UserData(uid = currentUser?.uid ?: "", isReady = false)

            val result = repo.joinGame(gameId, userData)
            _loading.value = false

            if (result.isSuccess) {
                _joinSuccess.emit(Unit)
            }
        }
    }
}