package uz.gita.seabattle.data.model

data class GameData(
    val gameId: String = "",
    val player1: UserData? = null,
    val player2: UserData? = null,
    val status: String = "Waiting",
)