package uz.gita.seabattle.presenter.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.gita.seabattle.data.repositoryImpl.AuthRepositoryImpl

class HomeViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(AuthRepositoryImpl.getInstance()) as T
    }
}