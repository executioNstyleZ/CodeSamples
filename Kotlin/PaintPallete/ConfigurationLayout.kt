package com.cs4530.jaredshaw.brushcontrol

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by Jared on 9/13/2017.
 *  This class is a layout for the configuration knobs which consist of
 *  3 text views and 3 control knobs, it is arranged using horizontal and
 *  vertical linear layouts.
 *  This class basically controls the configuration of the paint:
 *  the join type, the cap type, and the stroke width
 */

class ConfigurationLayout: LinearLayout, ControlKnob.onKnobValueChangedListener {

    private var joinKnob: ControlKnob
    private var joinNameText: TextView

    private var capKnob: ControlKnob
    private var capNameText: TextView

    private var strokeKnob: ControlKnob
    private var strokeNameText: TextView


    interface onConfigValueChangedListener{
        fun onBrushTypeValueChanged(optionName: String, newType: String)
        fun onStrokeWidthValueChanged(value: Float)
    }

    var configValueChangedListener: onConfigValueChangedListener? = null
    fun setOnValueChangedListener(listener: onConfigValueChangedListener){
        configValueChangedListener = listener
    }

    constructor(context: Context?, join: String, cap: String, width: Float) : super(context){

        orientation = LinearLayout.HORIZONTAL


        val joinLayout: LinearLayout = LinearLayout(context)
        joinLayout.orientation = LinearLayout.VERTICAL
        val capLayout: LinearLayout = LinearLayout(context)
        capLayout.orientation = LinearLayout.VERTICAL
        val strokeLayout: LinearLayout = LinearLayout(context)
        strokeLayout.orientation = LinearLayout.VERTICAL

        joinLayout.layoutParams = matchParentLayoutWithWeight(1.0f)
        capLayout.layoutParams = matchParentLayoutWithWeight(1.0f)
        strokeLayout.layoutParams = matchParentLayoutWithWeight(1.0f)

        addView(joinLayout)
        addView(capLayout)
        addView(strokeLayout)

        //join views
        joinNameText = TextView(context)
        joinNameText.setText("Join Type: Bevel")
        joinNameText.gravity = Gravity.CENTER_HORIZONTAL
        joinNameText.layoutParams = matchParentLayoutWithWeight(2.0f)
        joinNameText.setTextColor(Color.BLACK)
        joinKnob = ControlKnob(context)
        joinKnob.ident = 1
        joinKnob.setOnKnobValueChangedListener(this)
        joinKnob.maxValue = 3.0f
        joinKnob.minValue = 0.0f
        joinKnob.layoutParams = matchParentLayoutWithWeight(1.0f)


        //cap views
        capNameText = TextView(context)
        capNameText.setText("Cap Type: Butt")
        capNameText.layoutParams = matchParentLayoutWithWeight(2.0f)
        capNameText.setTextColor(Color.BLACK)
        capNameText.gravity = Gravity.CENTER_HORIZONTAL
        capKnob = ControlKnob(context)
        capKnob.ident = 2
        capKnob.setOnKnobValueChangedListener(this)
        capKnob.maxValue = 3.0f
        capKnob.minValue = 0.0f
        capKnob.layoutParams = matchParentLayoutWithWeight(1.0f)

        //stroke views
        strokeNameText = TextView(context)
        strokeNameText.setText("Stroke Width: \n0.5")
        strokeNameText.layoutParams = matchParentLayoutWithWeight(2.0f)
        strokeNameText.setTextColor(Color.BLACK)
        strokeNameText.gravity = Gravity.CENTER_HORIZONTAL
        strokeKnob = ControlKnob(context)
        strokeKnob.ident = 3
        strokeKnob.setOnKnobValueChangedListener(this)
        strokeKnob.maxValue = 50.0f
        strokeKnob.minValue = 0.5f
        strokeKnob.layoutParams = matchParentLayoutWithWeight(1.0f)

        joinLayout.addView(joinNameText)
        joinLayout.addView(joinKnob)
        capLayout.addView(capNameText)
        capLayout.addView(capKnob)
        strokeLayout.addView(strokeNameText)
        strokeLayout.addView(strokeKnob)
    }

    //returns a linear layout with match parent for both width and height and the given weight
    private fun matchParentLayoutWithWeight(weight: Float): LayoutParams{
        return LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight)
    }

    //updates the text in the join type textview and then proceeds to call the listener to inform it of this change
    private fun joinChange(value: String){
        joinNameText.setText("Join type: ${value}")
        joinNameText.invalidate()
        if(configValueChangedListener != null) configValueChangedListener?.onBrushTypeValueChanged("Join", value)
    }

    //updates the text in the join cap textview and then proceeds to call the listener to inform it of this change
    private fun capChange(value: String){
        capNameText.setText("Cap type: ${value}")
        capNameText.invalidate()
        if(configValueChangedListener != null) configValueChangedListener?.onBrushTypeValueChanged("Cap", value)
    }

    //this is the listener function for all 3 knobs in this layout
    //uses assigned ID's to distinguish which knob is calling this function
    //for the cap type and join type it uses ranges to determine which
    //type is assigned based. these ranges are within the min/max that i
    //passed into the control knob
    override fun onKnobValueChanged(ident: Int, value: Float) {
        if(ident == 1){ //join type
            if(value <= 1.0f){
                joinChange("Bevel")
            }else if(value > 1.0f && value <= 2.0f){
                joinChange("Miter")
            }else if(value > 2.0f && value <= 3.0f){
                joinChange("Round")
            }
        }else if(ident == 2){ //cap type
            if(value <= 1.0f){
                capChange("Butt")
            }else if(value > 1.0f && value <= 2.0f){
                capChange("Round")
            }else if(value > 2.0f && value <= 3.0f){
                capChange("Square")

            }
        }else if(ident == 3){ //stroke width
            strokeNameText.setText("Stroke Width: \n${Math.round(value * 100.0f)/100.0f}") //to get 2 decimal places
            strokeNameText.invalidate()
            if(configValueChangedListener != null) configValueChangedListener?.onStrokeWidthValueChanged(value)

        }
    }

}