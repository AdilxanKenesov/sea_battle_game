package uz.gita.seabattle.presenter.ui.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.gita.seabattle.data.repositoryImpl.AuthRepositoryImpl

class SecondViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SecondViewModel(AuthRepositoryImpl.getInstance()) as T
    }
}