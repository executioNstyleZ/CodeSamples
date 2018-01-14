package com.apps.jaredshaw.hotseatbattleships

import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Jared on 11/7/2017.
 */
class Player (playerEmail: String){
    private var defenseGrid: HashMap<Int, Int>
    private var offenseGrid: HashMap<Int, Int>
    private val shipSizes: List<Int>
    private var email: String

    enum class FireClass(stateNumber: Int){
        Hit(2), Miss(3), Sunk(4)
    }

    val upperBound = listOf<Int>(0,1,2,3,4,5,6,7,8,9)
    val rightBound = listOf<Int>(9,19,29,39,49,59,69,79,89,99)
    val lowerBound = listOf<Int>(90,91,92,93,94,95,96,97,98,99)
    val leftBound = listOf<Int>(0,10,20,30,40,50,60,70,80,90)

    val left = -1
    val right = 1
    val up = -10
    val down = 10

    init {
        defenseGrid = HashMap()
        offenseGrid = HashMap()

        when(playerEmail){
            "" -> email = "Waiting for opponent"
            else -> email = playerEmail
        }

        shipSizes = arrayListOf(2,3,3,4,5)

        setUpShipsAutomatically()
    }

    fun getPlayerEmail(): String{
        return email
    }

    fun setPlayerEmail(playerEmail: String){
        email = playerEmail
    }

    //return hit, miss, or sunk
    //TODO: return sunk
    fun getAttackedAt(location: Int): String{
        var result = ""

        if(defenseGrid.containsKey(location) && defenseGrid[location] == 1){
            result = FireClass.Hit.toString()
            defenseGrid[location] = 2
        }else{
            result = FireClass.Miss.toString()
        }
        return result
    }

    fun updateOffenseGrid(location: Int, newState: Int){
        offenseGrid[location] = newState
    }

    //setting up defensive grid
    //ship sizes: 2,3,3,4,5
    fun setUpShipsAutomatically(){

        val rand = Random()



        val direction: HashMap<Int, Int> = HashMap()
        direction[0] = left
        direction[1] = right
        direction[2] = up
        direction[3] = down

        for(i in shipSizes){        //cycles once per ship size

            val potentialPositions = mutableListOf<Int>()

            var valid = false
            while (valid == false) {

                valid = true

                var currentSize = i
                var pos = rand.nextInt(100)
                var dir: Int = direction[rand.nextInt(4)] as Int

                if(isInvalidPosition(pos, dir)) valid = false
                else potentialPositions.add(pos)

                for (i in 0 until currentSize - 1) {
                    pos += dir
                    if (isInvalidPosition(pos, dir)) valid = false
                    else potentialPositions.add(pos)
                }

                if(valid == true){
                    for(i in 0 until potentialPositions.size){
                        defenseGrid[potentialPositions[i]] = 1
                    }
                }else potentialPositions.clear()
            }

        }
    }

    private fun isInvalidPosition(pos: Int, dir: Int): Boolean{
        if(pos < 0 || pos > 99) return true
        if(defenseGrid.containsKey(pos)) return true
        if(upperBound.contains(pos) && dir == up) return true
        if(lowerBound.contains(pos) && dir == down) return true
        if(rightBound.contains(pos) && dir == right) return true
        if(leftBound.contains(pos) && dir == left) return true

        return false
    }

    fun getOffensiveGridData(): HashMap<Int, Int>{
        return offenseGrid
    }

    fun getDefensiveGridData(): HashMap<Int, Int>{
        return defenseGrid
    }

    fun setDefenseGrid(grid : HashMap<Int,Int>){
        defenseGrid = grid
    }

    fun setOffenseGrid(grid : HashMap<Int, Int>){
        offenseGrid = grid
    }

    fun getPartsOfShipsLeft():Int{
        var result = 0

        for(i in defenseGrid){
            if(i.value == 1) result++
        }
        return result
    }
}