package com.cs4530.jaredshaw.brushcontrol

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

/**
 * Created by Jared on 9/22/2017.
 * This class represents a single stroke on the canvas by the user
 * it is made up of a path
 */
class Stroke() {
    private val path: Path = Path()

    //draws a line to the given point, or if there is currently no point
    //it moves the cursor to the point
    fun addPoint(point: Point) {
        if(path.isEmpty){
            startStroke(point)
        }else{
            path.lineTo(point.getX(), point.getY())
        }
    }

    //clear the path
    fun clearPoints() {
        path.reset()
    }

    //draws the stroke on the given canvas
    fun drawStroke(canvas: Canvas, paint: Paint) {

        canvas.drawPath(path, paint)
    }

    //moves the path cursor to the point
    fun startStroke(point: Point){
        path.moveTo(point.getX(), point.getY())
    }

    //sets the last point of the stroke
    fun endStroke(point: Point){
        path.setLastPoint(point.getX(), point.getY())
    }

    //is the path empty?
    fun isStrokeEmpty():Boolean{
        return path.isEmpty
    }
}