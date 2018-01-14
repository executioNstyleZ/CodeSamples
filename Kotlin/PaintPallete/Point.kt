package com.cs4530.jaredshaw.brushcontrol

/**
 * Created by Jared on 9/16/2017.
 * this class keeps track of a point made up of two floats to represent an X and Y point
 */
class Point{
    private var pointX: Float = 0.0f
    private var pointY: Float = 0.0f

    constructor(x: Float, y: Float){
        pointX = x
        pointY = y
    }

    fun getX(): Float{
        return pointX
    }

    fun getY(): Float{
        return pointY
    }
}