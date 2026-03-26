package uz.gita.seabattle.data.model

data class UserData(
    val uid: String = "",
    val isReady: Boolean = false,
    val ships: List<String> = emptyList(),
)
