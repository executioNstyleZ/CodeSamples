package com.cs4530.jaredshaw.brushcontrol

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by Jared on 9/16/2017.
 * This class is a layout which consists of 3 textviews and 3 contorl knobs
 * each knob corresponds to an RGB value respectively (one for red, one for blue, one for green)
 * it laid out by both a horizontal and a vertical linear layout
 */
class PaintPickerLayout : LinearLayout, ControlKnob.onKnobValueChangedListener {

    private var redText: TextView
    private var redKnob: ControlKnob
    private var greenText: TextView
    private var greenKnob: ControlKnob
    private var blueText: TextView
    private var blueKnob: ControlKnob

    private var currentRed: Int = 0
    private var currentGreen: Int = 0
    private var currentBlue: Int = 0

    interface onColorKnobsChangedListener{
        fun onColorKnobsChanged(red: Int, green: Int, blue: Int)
    }

    private var colorKnobsChangedListener: onColorKnobsChangedListener? = null

    fun setOnColorKnobsChangedListener(onColorKnobsChangedListener: onColorKnobsChangedListener){
        colorKnobsChangedListener = onColorKnobsChangedListener
    }

    constructor(context: Context?) : super(context){
        this.orientation = LinearLayout.HORIZONTAL

        val redLayout: LinearLayout = LinearLayout(context)
        redLayout.orientation = LinearLayout.VERTICAL
        redLayout.layoutParams = matchParentLayoutWithWeight(1.0f)
        val greenLayout: LinearLayout = LinearLayout(context)
        greenLayout.orientation = LinearLayout.VERTICAL
        greenLayout.layoutParams = matchParentLayoutWithWeight(1.0f)
        val blueLayout: LinearLayout = LinearLayout(context)
        blueLayout.orientation = LinearLayout.VERTICAL
        blueLayout.layoutParams = matchParentLayoutWithWeight(1.0f)

        redText = TextView(context)
        redText.setText("Red")
        redText.gravity = Gravity.CENTER_HORIZONTAL
        redText.layoutParams = matchParentLayoutWithWeight(2.0f)
        redText.setTextColor(Color.BLACK)

        redKnob = ControlKnob(context)
        redKnob.setOnKnobValueChangedListener(this)
        redKnob.minValue = 0.0f
        redKnob.maxValue = 255.0f
        redKnob.ident = 1
        redKnob.layoutParams = matchParentLayoutWithWeight(1.0f)
        redKnob.setCurrentColor(Color.RED)
        onKnobValueChanged(redKnob.ident, redKnob.getCurrentColor().toFloat())

        greenText = TextView(context)
        greenText.setText("Green")
        greenText.gravity = Gravity.CENTER_HORIZONTAL
        greenText.layoutParams = matchParentLayoutWithWeight(2.0f)
        greenText.setTextColor(Color.BLACK)

        greenKnob = ControlKnob(context)
        greenKnob.setOnKnobValueChangedListener(this)
        greenKnob.minValue = 0.0f
        greenKnob.maxValue = 255.0f
        greenKnob.ident = 2
        greenKnob.layoutParams = matchParentLayoutWithWeight(1.0f)
        greenKnob.setCurrentColor(Color.GREEN)
        onKnobValueChanged(greenKnob.ident, greenKnob.getCurrentColor().toFloat())


        blueText = TextView(context)
        blueText.setText("Blue")
        blueText.gravity = Gravity.CENTER_HORIZONTAL
        blueText.layoutParams = matchParentLayoutWithWeight(2.0f)
        blueText.setTextColor(Color.BLACK)

        blueKnob = ControlKnob(context)
        blueKnob.setOnKnobValueChangedListener(this)
        blueKnob.minValue = 0.0f
        blueKnob.maxValue = 255.0f
        blueKnob.ident = 3
        blueKnob.layoutParams = matchParentLayoutWithWeight(1.0f)
        blueKnob.setCurrentColor(Color.BLUE)
        onKnobValueChanged(blueKnob.ident, blueKnob.getCurrentColor().toFloat())

        redLayout.addView(redText)
        redLayout.addView(redKnob)
        greenLayout.addView(greenText)
        greenLayout.addView(greenKnob)
        blueLayout.addView(blueText)
        blueLayout.addView(blueKnob)

        addView(redLayout)
        addView(greenLayout)
        addView(blueLayout)
    }

    //returns a linear layout with match parent for both width and height and the given weight
    private fun matchParentLayoutWithWeight(weight: Float): LayoutParams{
        return LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight)
    }

    //this function is called when a knob changes its value, i use INTS to identify the knobs
    //the passed in value is to represent a new color to assign,
    //i check the color is within 0-255 range and then change the color of the knob, and
    //pass the value on to the listener to change the paint color
    override fun onKnobValueChanged(ident: Int, value: Float) {
        if(ident == 1){
            currentRed = checkColorBounds(Math.round(value))
            changeKnobColor(redKnob, currentRed, 0, 0)
            if(colorKnobsChangedListener != null) colorKnobsChangedListener?.onColorKnobsChanged(currentRed, currentGreen, currentBlue)
        }else if(ident == 2){
            currentGreen = checkColorBounds(Math.round(value))
            changeKnobColor(greenKnob, 0, currentGreen, 0)
            if(colorKnobsChangedListener != null) colorKnobsChangedListener?.onColorKnobsChanged(currentRed, currentGreen, currentBlue)
        }else if(ident == 3){
            currentBlue = checkColorBounds(Math.round(value))
            changeKnobColor(blueKnob, 0, 0, currentBlue)
            if(colorKnobsChangedListener != null) colorKnobsChangedListener?.onColorKnobsChanged(currentRed, currentGreen, currentBlue)
        }
    }

    //changes the knob color
    private fun changeKnobColor(knob: ControlKnob, r: Int, g: Int, b: Int){
        knob.changeKnobColor(getIntFromColor(r,g,b))
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

    //makes sure the given value is within 0 to 255 range
    private fun checkColorBounds(value: Int): Int{
        if(value < 0) return 0
        if(value > 255) return 255
        return value
    }

}