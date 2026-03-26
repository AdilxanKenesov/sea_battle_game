package uz.gita.seabattle.presenter.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.gita.seabattle.data.model.KatakData
import uz.gita.seabattle.data.model.GameData
import uz.gita.seabattle.domain.repository.AuthRepository

class HomeViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _katak = MutableStateFlow(katakList())
    val katak = _katak.asStateFlow()

    private val _gameData = MutableStateFlow<GameData?>(null)
    val gameData = _gameData.asStateFlow()

    private var selectedShipSize = 0
    private val shipCord = mutableListOf<String>()

    val shipsCord = MutableStateFlow(0)


    private fun katakList(): List<KatakData>{
        val list = mutableListOf<KatakData>()
        for (i in 0 until 100){
            list.add(KatakData(i,i/10, i%10))
        }
        return list
    }


    fun startListening(gameId: String) {
        viewModelScope.launch {
            repo.getGameData(gameId).collect { result ->
                result.onSuccess { data ->
                    _gameData.value = data
                }
            }
        }
    }

    fun selectShipSize(size: Int) {
        selectedShipSize = size
    }


    fun placeShip(viewId: Int): Boolean {
        if (selectedShipSize == 0) return false

        val currentList = _katak.value.toMutableList()
        val row = viewId / 10
        val col = viewId % 10

        val c = selectedShipSize / 2
        val a = row - c
        val b = row + (selectedShipSize - c - 1)

        if (a < 0 || b > 9)
            return false

        val emptyCell = mutableListOf<Int>()

        for (i in a..b) {
            val idx = i * 10 + col
            if (currentList[idx].hasShip) return false
            emptyCell.add(idx)
        }

        emptyCell.forEach { idx ->
            currentList[idx] = currentList[idx].copy(hasShip = true)
            shipCord.add("${idx / 10},${idx % 10}")
        }

        _katak.value = currentList
        selectedShipSize = 0
        shipsCord.value += 1
        return true
    }

    fun finishSetup(gameId: String, isPlayer1: Boolean) {
        viewModelScope.launch {
            repo.sendShips(gameId, isPlayer1, shipCord)
        }
    }
}