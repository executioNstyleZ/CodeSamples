package com.cs4530.jaredshaw.brushcontrol

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * Created by Jared on 9/13/2017.
 * this is the parent class for the brush controller, it is made up of a vertical
 * linear layout with 3 separate layouts that manage thier own views
 * this class acts as a controller and median between the Configruation/paintPickLayout
 * and the PreviewView
 */

class BrushController : LinearLayout, ConfigurationLayout.onConfigValueChangedListener, PaintPickerLayout.onColorKnobsChangedListener {

    private var configLayout: ConfigurationLayout
    private var previewView: PreviewView
    private var paintPicker: PaintPickerLayout

    private var currentColor: Int = Color.BLACK
    private var currentJoinType: String = "Bevel"
    private var currentCapType: String = "Butt"
    private var currentStrokeWidth: Float = 0.5f

    constructor(context: Context?) : super(context){

        var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.50f)
        var params2: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.25f)

        paintPicker = PaintPickerLayout(context)
        paintPicker.setOnColorKnobsChangedListener(this)
        paintPicker.setBackgroundColor(Color.WHITE)
        paintPicker.layoutParams = params
        paintPicker.setPadding(10,10,10,10)

        configLayout = ConfigurationLayout(context, currentJoinType, currentCapType, currentStrokeWidth)
        configLayout.setOnValueChangedListener(this)
        configLayout.layoutParams = params
        configLayout.setBackgroundColor(Color.WHITE)
        configLayout.setPadding(10,10,10,10)

        previewView = PreviewView(context, currentColor, currentJoinType, currentCapType, currentStrokeWidth, Color.LTGRAY)
        previewView.layoutParams = params2

        orientation = LinearLayout.VERTICAL
        addView(paintPicker)
        addView(previewView)
        addView(configLayout)
    }

    //called when a change in a join,cap knob occurs
    //tells the previewView to update it's join/cap types
    override fun onBrushTypeValueChanged(optionName: String, newType: String) {
        when(optionName){
            "Join" -> previewView.changeJoinType(newType)
            "Cap" -> previewView.changeCapType(newType)
        }
    }

    //called when a change in the width knob occurs,
    //tells the previewView to update its stroke width
    override fun onStrokeWidthValueChanged(value: Float) {
        previewView.changeStrokeWidth(value)
    }

    //called when a change on one of the color knobs occurs
    //tells the preview view to update the color of the stroke
    override fun onColorKnobsChanged(red: Int, green: Int, blue: Int) {
        previewView.setRGBcolors(red, green, blue)
    }

}