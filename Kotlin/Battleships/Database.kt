package com.apps.jaredshaw.hotseatbattleships

import android.content.Context
import android.support.annotation.UiThread
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Created by Jared on 11/21/2017.
 */
object Database{

    private var mAuth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private var gameListRef: DatabaseReference
    private lateinit var singleGameRef: DatabaseReference
    private var database: FirebaseDatabase
    private lateinit var gameListListener: ValueEventListener
    private lateinit var singleGameListener: ValueEventListener

    fun listenToGameList(context: Context){
        var gameMap: HashMap<String, Game> = hashMapOf()


        gameListListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(data: DataSnapshot?) {
                if(data!!.exists()){
                    for(child in data.children){
                        val gameID = child.key
                        val gameObj = child.getValue(GameObject::class.java)
                        if(gameObj != null){
                            gameMap.put(gameID, parseGameFromDatabase(gameObj, Integer.parseInt(gameID)))
                        }
                    }
                }

                GameList.addGameList(filterOutCompletedGames(gameMap))
                val listActivity: ListActivity = context as ListActivity
                listActivity.displayMainMenu()
            }
        }

        gameListRef.addValueEventListener(gameListListener)

    }

    fun removeListenerToGameList(){
        gameListRef.removeEventListener(gameListListener)
    }

    fun listenToSingleGame(context: Context, id: String){
        singleGameRef = database.getReference().child(id)

        singleGameListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(data: DataSnapshot?) {
                if(data!!.exists()){
                    val gameID: Int = Integer.parseInt(id)
                    val gameObj: GameObject? = data.getValue(GameObject::class.java)
                    if(gameObj is GameObject){
                        GameList.updateCurrentGame(parseGameFromDatabase(gameObj, gameID), gameID)

                        val gameScreenActivity: GameActivity = context as GameActivity
                        gameScreenActivity.displayGameView()
                    }
                }
            }
        }
        singleGameRef.addValueEventListener(singleGameListener)

    }

    fun removeListenerToSingleGame(){
        singleGameRef.removeEventListener(singleGameListener)
    }

    init {
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        database = FirebaseDatabase.getInstance()
        gameListRef = database.getReference()
    }

    fun signout(){
        mAuth.signOut()
    }

    fun getEmail(): String{
        if(currentUser is FirebaseUser){
            return currentUser?.email.toString()
        }

        return ""
    }

    fun isSignedIn(): Boolean{
        if(currentUser is FirebaseUser){
            return true
        }

        return false
    }

    fun sendVerification(context: Context){
        if(currentUser is FirebaseUser){
            currentUser?.sendEmailVerification()
            val toasty = Toast.makeText(context, "An email has been sent to " + Database.getEmail(), Toast.LENGTH_SHORT)
            toasty.show()
        }
    }

    fun login(email: String, password: String, context: Context) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                currentUser = mAuth.currentUser

                val toasty = Toast.makeText(context, "Logged in as " + currentUser!!.email, Toast.LENGTH_SHORT)
                toasty.show()

                val activity: MainActivity = context as MainActivity
                activity.goToGameListActivity()
            }else{
                val toasty = Toast.makeText(context, "There was an error logging in", Toast.LENGTH_SHORT)
                toasty.show()
            }
        }

    }

    fun registerNewUser(email: String, password: String, context: Context){

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                currentUser = mAuth.currentUser

                val toasty = Toast.makeText(context, "User successfully created", Toast.LENGTH_SHORT)
                toasty.show()

                val activity: MainActivity = context as MainActivity
                activity.displayLoginMenu()
            }else{
                val toasty = Toast.makeText(context, "There was an error registering the new user", Toast.LENGTH_SHORT)
                toasty.show()
            }
        }
    }

    fun isEmailVerified(): Boolean {
        var result = false

        if(currentUser is FirebaseUser){
            mAuth.currentUser?.reload()
            currentUser = mAuth.currentUser
            result = currentUser?.isEmailVerified!!
        }

        return result
    }

    private fun parseGameFromDatabase(game: GameObject, id: Int): Game{

        val player1 = Player(game.firstPlayer.email)
        player1.setDefenseGrid(shipsDestToMap(game.firstPlayer.ships, game.firstPlayer.destroyed))
        player1.setOffenseGrid(hitsMissesToMap(game.firstPlayer.hits, game.firstPlayer.misses))

        val player2 = Player(game.secondPlayer.email)
        player2.setDefenseGrid(shipsDestToMap(game.secondPlayer.ships, game.secondPlayer.destroyed))
        player2.setOffenseGrid(hitsMissesToMap(game.secondPlayer.hits, game.secondPlayer.misses))

        return Game(player1, player2, Integer.parseInt(game.turn), game.state, id)
    }

    private fun hitsMissesToMap(hits: List<String>, misses: List<String>) :HashMap<Int,Int>{

        var offenseGrid: HashMap<Int, Int> = hashMapOf()

        for(i in hits){
            var intValue: Int = Integer.parseInt(i)
            offenseGrid.put(intValue, 2)
        }
        for(i in misses){
            var intValue: Int = Integer.parseInt(i)
            offenseGrid.put(intValue, 3)
        }

        return offenseGrid
    }

    private fun shipsDestToMap(ships: List<String>, dest: List<String>) :HashMap<Int,Int>{

        var defenseGrid: HashMap<Int, Int> = hashMapOf()

        for(i in ships){
            var intValue: Int = Integer.parseInt(i)
            defenseGrid.put(intValue, 1)
        }
        for(i in dest){
            var intValue: Int = Integer.parseInt(i)
            defenseGrid.put(intValue, 2)
        }

        return defenseGrid
    }

    fun updateCurrentGame(){
        val database = FirebaseDatabase.getInstance()

        val gameUnformatted: Game = GameList.getCurrentGameInfo()
        val gameID = gameUnformatted.getGameID().toString()

        val game: GameObject = parseGameIntoObject(gameUnformatted)

        database.getReference().child(gameID).setValue(game)
    }

    fun deleteGame(context: Context, id: String){
        removeListenerToGameList()
        val database = FirebaseDatabase.getInstance()

        database.getReference().child(id).removeValue()
        listenToGameList(context)
    }

    private fun parseGameIntoObject(gameUnformatted: Game): GameObject{

        val player1Unformatted = gameUnformatted.getPlayer1Info()
        val player2Unformatted = gameUnformatted.getPlayer2Info()

        val firstPlayer = PlayerObject(player1Unformatted.getPlayerEmail(), shipMapToList(player1Unformatted.getDefensiveGridData()),
                hitMapToList(player1Unformatted.getOffensiveGridData()), missMapToList(player1Unformatted.getOffensiveGridData()), destMapToList(player1Unformatted.getDefensiveGridData()))
        val secondPlayer = PlayerObject(player2Unformatted.getPlayerEmail(), shipMapToList(player2Unformatted.getDefensiveGridData()),
                hitMapToList(player2Unformatted.getOffensiveGridData()), missMapToList(player2Unformatted.getOffensiveGridData()), destMapToList(player2Unformatted.getDefensiveGridData()))

        return GameObject(gameUnformatted.getTurn().toString(), gameUnformatted.getGameState(), firstPlayer, secondPlayer)
    }

    fun addGame(gameUnformatted: Game){
        val database = FirebaseDatabase.getInstance()

        val gameID: String = gameUnformatted.getGameID().toString()

        val game: GameObject = parseGameIntoObject(gameUnformatted)

        database.getReference().child(gameID).setValue(game)
    }

    private fun shipMapToList(defensiveGrid: HashMap<Int, Int>): List<String>{

        var shipLocs: MutableList<String> = mutableListOf()

        var keys = defensiveGrid.keys
        var values = defensiveGrid.values

        for(i in keys.indices){
            var value = values.elementAt(i)
            if(value == 1) shipLocs.add(keys.elementAt(i).toString())
        }

        return shipLocs
    }

    private fun hitMapToList(offensiveGrid: HashMap<Int, Int>): List<String>{
        var hitLocs: MutableList<String> = mutableListOf()

        var keys = offensiveGrid.keys
        var values = offensiveGrid.values

        for(i in keys.indices){
            var value = values.elementAt(i)
            if(value == 2) hitLocs.add(keys.elementAt(i).toString())
        }
        return hitLocs
    }

    private fun missMapToList(offensiveGrid: HashMap<Int, Int>): List<String>{
        var missLocs: MutableList<String> = mutableListOf()

        var keys = offensiveGrid.keys
        var values = offensiveGrid.values
        for(i in keys.indices){
            var value = values.elementAt(i)
            if(value == 3) missLocs.add(keys.elementAt(i).toString())
        }
        return missLocs
    }

    private fun destMapToList(defensiveGrid: HashMap<Int, Int>): List<String>{

        var destroyedShipLocs: MutableList<String> = mutableListOf()

        var keys = defensiveGrid.keys
        var values = defensiveGrid.values

        for(i in keys.indices){
            var value = values.elementAt(i)
            if(value == 2) destroyedShipLocs.add(keys.elementAt(i).toString())
        }
        return destroyedShipLocs
    }

    private fun filterOutCompletedGames(games: HashMap<String,Game>): MutableList<Game>{

        var gameList = mutableListOf<Game>()

        for(key in games.keys){
            var currentGame = games.get(key)
            if(!currentGame!!.isCompleted()){
                gameList.add(currentGame)
            }else{
                if(currentGame.getPlayer1Info().getPlayerEmail() == getEmail() || currentGame.getPlayer2Info().getPlayerEmail() == getEmail()){
                    gameList.add(currentGame)
                }
            }
        }

        return gameList
    }
}