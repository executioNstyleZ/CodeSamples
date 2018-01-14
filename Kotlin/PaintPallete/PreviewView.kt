package com.cs4530.jaredshaw.brushcontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

/**
 * Created by Jared on 9/13/2017.
 * This is the main canvas in the brush controller, it allows the user to draw
 * onto a canvas with the current configuration made by PaintPickerLayout and
 * ConfigurationLayout
 */

class PreviewView : View {

    private var paint: Paint
    private var clearedScreen: Boolean = false
    private var userTouchedScreen: Boolean = false
    private var colorForBackground: Int = 0
    private var currentStroke: Stroke
    private companion object {
        const val widthFactor: Float = 0.333f
    }

    //sets up all the default values passed in (this is before the user has touched anything)
    constructor(context: Context?, color: Int, join: String, cap: String, stroke: Float, backgroundColor: Int) : super(context){
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        currentStroke = Stroke()
        colorForBackground = backgroundColor
        setBackgroundColor(colorForBackground)
        setPaintConfiguration(join, cap, stroke, color)
        invalidate()
    }

    //sets up a default stroke before the user has touched the screen
    private fun setDefaultPoints(){

        if(currentStroke.isStrokeEmpty() == false) return

        var pointX: Float = paddingLeft.toFloat() + (width * 0.1f)
        var pointY: Float = paddingTop.toFloat() + (height * 0.9f)

        val firstPoint: Point = Point(pointX, pointY)

        pointX = paddingLeft.toFloat() + (width * 0.5f)
        pointY = paddingTop.toFloat() + (height * 0.1f)

        val secondPoint: Point = Point(pointX, pointY)

        pointX = paddingLeft.toFloat() + (width * 0.9f)
        pointY = paddingTop.toFloat() + (height * 0.9f)

        val thirdPoint: Point = Point(pointX, pointY)

        currentStroke.addPoint(firstPoint)
        currentStroke.addPoint(secondPoint)
        currentStroke.addPoint(thirdPoint)
        currentStroke.endStroke(thirdPoint)
    }

    //sets up the paint, including stroke style, color, pixels, join type, and cap type
    private fun setPaintConfiguration(join: String, cap: String, stroke: Float, color: Int){

        if(join.equals("Bevel")) paint.strokeJoin = Paint.Join.BEVEL
        else if(join.equals("Miter")) paint.strokeJoin = Paint.Join.MITER
        else if(join.equals("Round")) paint.strokeJoin = Paint.Join.ROUND

        if(cap.equals("Butt")) paint.strokeCap = Paint.Cap.BUTT
        else if(cap.equals("Round")) paint.strokeCap = Paint.Cap.ROUND
        else if(cap.equals("Square")) paint.strokeCap = Paint.Cap.SQUARE

        paint.strokeWidth = floatToDisplayPixels(stroke * widthFactor)
        paint.style = Paint.Style.STROKE
        paint.color = color
    }

    //scales the given float to the display pixels of the screen
    private fun floatToDisplayPixels(value: Float): Float{
        val dm: DisplayMetrics = resources.displayMetrics
        val result: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm)
        return result
    }

    //updates the type of join in the paint, if the given value matches the
    //current value, no changed is made
    fun changeJoinType(newType: String){
        if(newType.equals("Bevel") && paint.strokeJoin != Paint.Join.BEVEL) {
            paint.strokeJoin = Paint.Join.BEVEL
            invalidate()

        }
        else if(newType.equals("Miter") && paint.strokeJoin != Paint.Join.MITER) {
            paint.strokeJoin = Paint.Join.MITER
            invalidate()

        }
        else if(newType.equals("Round") && paint.strokeJoin != Paint.Join.ROUND) {
            paint.strokeJoin = Paint.Join.ROUND
            invalidate()
        }
    }

    //updates the type of cap in the paint, if the given value matches the
    //current value, no change is made
    fun changeCapType(newType: String){
        if(newType.equals("Butt") && paint.strokeCap != Paint.Cap.BUTT){
            paint.strokeCap = Paint.Cap.BUTT
            invalidate()
        }
        else if(newType.equals("Round") && paint.strokeCap != Paint.Cap.ROUND){
            paint.strokeCap = Paint.Cap.ROUND
            invalidate()
        }
        else if(newType.equals("Square") && paint.strokeCap != Paint.Cap.SQUARE){
            paint.strokeCap = Paint.Cap.SQUARE
            invalidate()
        }

    }

    //updates the width of the stroke
    fun changeStrokeWidth(newValue: Float){
        paint.strokeWidth = floatToDisplayPixels(newValue * widthFactor)
        invalidate()
    }

    //updates the color of the paint using helper function
    fun setRGBcolors(red: Int, green: Int, blue: Int){
        setPaintColor(getIntFromColor(red, green, blue))
    }

    //sets the paint to the given color
    private fun setPaintColor(color: Int){
        paint.color = color
        invalidate()
    }

    //given separate r,g,b values it returns a single color as a mix of the three
    private fun getIntFromColor(Red: Int, Green: Int, Blue: Int): Int {
        var Red = Red
        var Green = Green
        var Blue = Blue
        Red = Red shl 16 and 0x00FF0000
        Green = Green shl 8 and 0x0000FF00
        Blue = Blue and 0x000000FF

        return 0xFF000000.toInt() or Red or Green or Blue
    }

    //called when the user touches the screen, saves the touch points
    //to the Stroke class
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event !is MotionEvent) return false

        //this is to keep track of whether the default points should be drawn or not
        //based on if the user has touched the screen yet
        if(userTouchedScreen == false) {
            currentStroke.clearPoints()
            userTouchedScreen = true
        }

        val point: Point = Point(event.x, event.y)

        if(event.action == android.view.MotionEvent.ACTION_DOWN){ //starts a new stroke
            currentStroke.clearPoints()
            currentStroke.addPoint(point)
        }else if(event.action == android.view.MotionEvent.ACTION_UP){ //ends the current stroke
            currentStroke.endStroke(point)
        }else{                                                      //just adds points to the stroke
            currentStroke.addPoint(point)
        }

        invalidate()

        return true
    }

    //draws the current stroke, the border, and clears the canvas when needed
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas !is Canvas) return

        //set up default points and add to stroke if the user hasnt touched the screen
        //this is called here to gaurantee that the view has access to width/height/padding
        if(userTouchedScreen == false){
            setDefaultPoints()
        }

        //clears the canvas
        if(userTouchedScreen && !clearedScreen){
            clearCanvas(canvas)
            clearedScreen == true
        }

        //draws the border
        drawBorder(canvas)

        //draws the stroke
        currentStroke.drawStroke(canvas, paint)
    }

    //draws a border around the canvas
    private fun drawBorder(canvas: Canvas){
        var border: RectF = RectF()
        border.left = paddingLeft.toFloat()
        border.right = paddingRight.toFloat() + width.toFloat()
        border.top = paddingTop.toFloat()
        border.bottom = paddingTop.toFloat() + height.toFloat()

        var borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint.color = Color.DKGRAY
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = floatToDisplayPixels(7.5f)
        canvas.drawRect(border, borderPaint)
    }

    //"clearing" the canvas is simply drawing a box over the whole canvas
    private fun clearCanvas(canvas: Canvas){
        var rect: RectF = RectF()
        rect.left = paddingLeft.toFloat()
        rect.right = paddingRight.toFloat() + width.toFloat()
        rect.top = paddingTop.toFloat()
        rect.bottom = paddingTop.toFloat() + height.toFloat()
        var canvasPaint: Paint = Paint()
        canvasPaint.color = colorForBackground
        canvas.drawRect(rect, canvasPaint)
    }




}
