package com.apps.jaredshaw.hotseatbattleships

/**
 * Created by Jared on 11/7/2017.
 */
class Game(newPlayer1: Player, newPlayer2: Player, turn: Int, startingState: String, newGameID: Int) {

    private var gameID: Int
    private var currentTurn: Int
    private var players: Pair<Player, Player>
    private var currentState: String

    enum class GameState(val stateNumber: Int) {
        JustStarting(0), InProgress(1), Player1Won(2), Player2Won(3)
    }

        init {
            players = Pair(newPlayer1, newPlayer2)
            currentTurn = turn
            currentState = startingState
            gameID = newGameID
        }

        //the player number passed in is the person making an attack
        fun launchMissle(playerNumber: Int, attackLocation: Int): String {
            currentState = GameState.InProgress.toString()

            var result: String = ""

            if (playerNumber == 1) {//player 1 attacks player 2
                result = players.second.getAttackedAt(attackLocation)
                if(result == Player.FireClass.Hit.toString()) players.first.updateOffenseGrid(attackLocation, 2)
                else if(result == Player.FireClass.Miss.toString()) players.first.updateOffenseGrid(attackLocation, 3)

                if(players.second.getPartsOfShipsLeft() == 0){
                    currentState = GameState.Player1Won.toString()
                }

            } else {
                result = players.first.getAttackedAt(attackLocation)
                if(result == Player.FireClass.Hit.toString()) players.second.updateOffenseGrid(attackLocation, 2)
                else if(result == Player.FireClass.Miss.toString()) players.second.updateOffenseGrid(attackLocation, 3)

                if(players.first.getPartsOfShipsLeft() == 0){
                    currentState = GameState.Player2Won.toString()
                }
            }

            return result
        }

        fun getGameID(): Int {
            return gameID
        }

    fun getNameOfPlayerTurn(index: Int): String{
        if(index == 1){
            return players.first.getPlayerEmail()
        }else{
            return players.second.getPlayerEmail()
        }
    }

        fun setGameID(id: Int) {
            gameID = id
        }

        fun setGameState(newState: String) {
            currentState = newState
        }

        fun getGameState(): String {
            return currentState
        }

        fun switchTurn() {
            if (currentTurn == 1) {
                currentTurn = 2
            } else {
                currentTurn = 1
            }
        }

        fun getTurn(): Int {
            return currentTurn
        }

        fun getPartsOfShipsLeft(player: Int): Int {
            var result: Int = -1
            when (player) {
                1 -> {
                    result = players.first.getPartsOfShipsLeft()
                    if(result == 0) currentState = GameState.Player2Won.toString()
                }
                2 -> {
                    result = players.second.getPartsOfShipsLeft()
                    if(result == 0) currentState = GameState.Player1Won.toString()
                }
            }
            return result
        }

    fun getOffensiveGridFor(player: Int):HashMap<Int,Int>{
        if(player == 1){
            return players.first.getOffensiveGridData()
        }else{
            return players.second.getOffensiveGridData()
        }
    }

    fun getDefensiveGridFor(player: Int):HashMap<Int,Int>{
        if(player == 1){
            return players.first.getDefensiveGridData()
        }else{
            return players.second.getDefensiveGridData()
        }
    }

    fun isCompleted(): Boolean{
        if(currentState == GameState.Player1Won.toString() || currentState == GameState.Player2Won.toString()) return true

        return false
    }

    fun getPlayerNames(): Pair<String, String>{
        return Pair(players.first.getPlayerEmail(), players.second.getPlayerEmail())
    }

    fun setSecondPlayerName(name: String){
        players.second.setPlayerEmail(name)
    }

    fun getPlayer1Info(): Player{
        return players.first
    }

    fun getPlayer2Info(): Player{
        return players.second
    }

}