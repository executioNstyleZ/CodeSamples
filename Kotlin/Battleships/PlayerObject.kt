package com.apps.jaredshaw.hotseatbattleships

/**
 * Created by Jared on 12/14/2017.
 */
class PlayerObject(val email: String, val ships: List<String>, val hits: List<String>,
                   val misses: List<String>, val destroyed: List<String>){

    constructor() : this("N/A", listOf(), listOf(), listOf(), listOf()){

    }
}