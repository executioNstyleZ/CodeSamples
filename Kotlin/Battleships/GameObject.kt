package com.apps.jaredshaw.hotseatbattleships

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by Jared on 12/14/2017.
 */

@IgnoreExtraProperties
class GameObject(val turn: String, val state: String, var firstPlayer: PlayerObject, var secondPlayer: PlayerObject){

    constructor() : this("1", "JustStarting", PlayerObject(), PlayerObject()){

    }
}