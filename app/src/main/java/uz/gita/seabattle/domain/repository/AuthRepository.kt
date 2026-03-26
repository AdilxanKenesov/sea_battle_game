package uz.gita.seabattle.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.seabattle.data.model.GameData
import uz.gita.seabattle.data.model.UserData

interface AuthRepository {
    suspend fun signUser(): Result<Unit>
    suspend fun createGame(user: UserData): Result<String>
    suspend fun joinGame(gameId: String, user: UserData): Result<Unit>
    suspend fun sendShips(gameId: String, player1: Boolean, ships: List<String>): Result<Unit>
    fun getGameData(gameId: String): Flow<Result<GameData>>
}