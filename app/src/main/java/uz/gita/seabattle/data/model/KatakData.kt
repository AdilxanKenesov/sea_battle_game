package uz.gita.seabattle.data.model

data class KatakData(
    val id: Int,
    val row: Int,
    val col: Int,
    var hasShip: Boolean = false,
    var isCenter: Boolean = false
)