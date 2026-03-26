package uz.gita.seabattle.presenter.ui.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.gita.seabattle.data.repositoryImpl.AuthRepositoryImpl

class FirstViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FirstViewModel(AuthRepositoryImpl.getInstance()) as T
    }
}