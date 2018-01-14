package com.apps.jaredshaw.hotseatbattleships

import android.content.Context
import android.content.SharedPreferences
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Jared on 11/7/2017.
 */
object GameList {

    lateinit private var games: MutableList<Game>
    private var currentGame: Game? = null

    fun addGameList(gameList: MutableList<Game>){
        var blah = 10

        games = gameList.toMutableList()
        blah = 11
    }

    fun getCurrentGameInfo(): Game{
        return currentGame!!
    }


    fun continueGame(index: Int){
        currentGame = games[index]
    }

    fun spectateGame(index: Int){
        continueGame(index)
    }

    fun joinGame(index: Int){
        currentGame = games[index]
        currentGame!!.setSecondPlayerName(Database.getEmail())
        currentGame!!.setGameState(Game.GameState.InProgress.toString())

        Database.updateCurrentGame()
    }

    fun startNewGame(){
        val player1 = Player( Database.getEmail())
        val player2 = Player( "")
        val newGame = Game(player1, player2, 1, Game.GameState.JustStarting.toString(), makeGameID())
        games.add(newGame)
        currentGame = newGame

        Database.addGame(currentGame!!)
    }

    fun makeGameID():Int{
        val rand = Random()
        val rValue = rand.nextInt(Int.MAX_VALUE)
        return rValue
    }

    fun updateCurrentGame(game: Game, id: Int){
        var index = -1

        for(i in games.indices){
            if(games[i].getGameID() == id){
                index = i
                break
            }
        }

        try{
            games[index] = game
            currentGame = games[index]
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getGameSummaries(): MutableList<String>{
        var summaries: MutableList<String> = mutableListOf()

        for(i in 0 until games.size){
            var currentGame = games.get(i)

            var info: StringBuilder = StringBuilder()
            info.append("Game ID: ${currentGame.getGameID()}\n")
            info.append("Game State: ${currentGame.getGameState()}\n")
            if(currentGame.getGameState() == Game.GameState.Player1Won.toString() || currentGame.getGameState() == Game.GameState.Player2Won.toString()){
                info.append("Current Turn: N/A\n")
            }else{
                info.append("Current Turn: ${currentGame.getNameOfPlayerTurn(currentGame.getTurn())}\n")
            }
            info.append("${currentGame.getNameOfPlayerTurn(1)} ship pieces left: ${currentGame.getPartsOfShipsLeft(1)}\n")
            info.append("${currentGame.getNameOfPlayerTurn(2)} ship pieces left: ${currentGame.getPartsOfShipsLeft(2)}\n")

            summaries.add(info.toString())
        }

        if(summaries.size == 0){
            summaries.add("There are currently no active games.")
        }

        return summaries
    }

    fun getGameState(index: Int):String{
        return games[index]?.getGameState()
    }

    fun switchTurn(): Int{
        currentGame?.switchTurn()
        return currentGame?.getTurn()!!
    }

    fun getNameOfLoser(): String{
        if(currentGame!!.isCompleted()){
            if(currentGame!!.getGameState() == Game.GameState.Player1Won.toString()){
                return currentGame!!.getPlayerNames().second
            }else{
                return  currentGame!!.getPlayerNames().first
            }
        }
        return ""
    }

    fun currentGameState(): String{
        return currentGame!!.getGameState()
    }

    fun nameOfCurrentTurn(): String{
        return currentGame!!.getNameOfPlayerTurn(getCurrentTurn())
    }

    fun getOppositePlayer(player: Int): Int{
        if(player == 1) return 2
        else return 1
    }

    fun shipsLeft(player: Int): Int{
        return currentGame?.getPartsOfShipsLeft(player)!!
    }

    fun playerWonMessage(): String{
        if(currentGame!!.isCompleted() == false) return ""

        var names = currentGame!!.getPlayerNames()

        if(currentGame!!.getGameState() == Game.GameState.Player1Won.toString()){
            return "${names.first} won!!"
        }else return "${names.second} won!!"
    }

    fun isGameCompleted(): Boolean{
        return currentGame!!.isCompleted()
    }

    fun deleteGameAt(position: Int): String{

        var id: String = "-1"

        try{
            id = games[position].getGameID().toString()
            games.removeAt(position)

        }catch (e: Exception){
            e.printStackTrace()
        }
        return id
    }

    fun containsGame(position: Int): Boolean{
        if(position >= 0 && position < games.size) return true
        else return false
    }

    fun launchMissle(player: Int, position: Int): String{
        return currentGame?.launchMissle(player, position)!!
    }

    fun getCurrentTurn(): Int{
        return currentGame?.getTurn()!!
    }

    fun getOffensiveGridFor(player: Int):HashMap<Int,Int>{
        return currentGame?.getOffensiveGridFor(player)!!
    }

    fun getDefensiveGridFor(player: Int):HashMap<Int,Int>{
        return currentGame?.getDefensiveGridFor(player)!!
    }

    fun getPlayerNames(): Pair<String, String>{
        return currentGame!!.getPlayerNames()
    }

    fun getPlayerNames(index: Int): Pair<String, String>{
        return games[index].getPlayerNames()
    }

    fun getOffensiveGridFor(name: String):HashMap<Int,Int>{
        var names = currentGame!!.getPlayerNames()
        if(names.first == name){
            return currentGame!!.getOffensiveGridFor(1)
        }

        else if(names.second == name){
            return currentGame!!.getOffensiveGridFor(2)
        }

        return hashMapOf()
    }

    fun getDefensiveGridFor(name: String):HashMap<Int,Int>{
        var names = currentGame!!.getPlayerNames()

        if(names.first == name){
            return currentGame!!.getDefensiveGridFor(1)
        }

        else if(names.second == name){
            return currentGame!!.getDefensiveGridFor(2)
        }

        return hashMapOf()
    }

    fun getAllGames(): List<Game>{
        return games
    }
}