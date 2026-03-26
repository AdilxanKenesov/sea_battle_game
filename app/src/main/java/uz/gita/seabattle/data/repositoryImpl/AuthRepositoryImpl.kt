package uz.gita.seabattle.data.repositoryImpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uz.gita.seabattle.data.model.GameData
import uz.gita.seabattle.data.model.UserData
import uz.gita.seabattle.domain.repository.AuthRepository

class AuthRepositoryImpl(private val db: DatabaseReference, private val auth: FirebaseAuth) : AuthRepository {
    companion object{
        private lateinit var instance: AuthRepository
        fun getInstance(): AuthRepository{
            if (!(::instance.isInitialized)){
                instance = AuthRepositoryImpl(FirebaseDatabase.getInstance().reference, FirebaseAuth.getInstance())
            }
            return instance
        }
    }

    override suspend fun signUser(): Result<Unit> {
        return try {
            auth.signInAnonymously().await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }


    override suspend fun createGame(user: UserData): Result<String> {
        return try {
            val gameId = (10_000..99999).random().toString()
            val gameData = GameData(
                gameId = gameId,
                player1 = user,
                status = "Waiting"
            )
            db.child("games").child(gameId).setValue(gameData).await()

            Result.success(gameId)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun joinGame(gameId: String, user: UserData): Result<Unit> {
        return try {
            db.child("games").child(gameId).child("player2").setValue(user).await()
            db.child("games").child(gameId).child("status").setValue("Setup").await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun sendShips(gameId: String, player1: Boolean, ships: List<String>): Result<Unit> {
        return try {
            val player: String = if(player1){
                "player1"
            }else{
                "player2"
            }
            db.child("games").child(gameId).child(player).child("ships").setValue(ships).await()
            db.child("games").child(gameId).child(player).child("isReady").setValue(true).await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override fun getGameData(gameId: String): Flow<Result<GameData>> = callbackFlow {
        val l = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(GameData::class.java)
                if (data != null) {
                    trySend(Result.success(data))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }

        }

        val r = db.child("games").child(gameId)
        r.addValueEventListener(l)
        awaitClose { r.removeEventListener(l) }
    }

}