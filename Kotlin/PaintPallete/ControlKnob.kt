package com.cs4530.jaredshaw.brushcontrol

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * Created by Jared on 9/15/2017.
 */
/* This class gives a view of the control knob in the form of a circle, with a smaller circle(nib)
   inside it which can be turned.
*
* */
class ControlKnob: View {
    constructor(context: Context?) : super(context)

    var ident: Int = -1
        set(value) {field = value}
        get() {return field}

    private var theta: Double = 0.0
        set(value){
            field = value
            invalidate()
        }
    private val controlKnobRect: RectF = RectF()
    private val nibCenter: PointF = PointF()
    private val nibRect: RectF = RectF()

    private companion object {
        const val radianToDegree: Float = 57.296f
    }

    private val controlKnobPaint: Paint = {
        val controlKnobPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        controlKnobPaint.color = Color.DKGRAY
        controlKnobPaint
    }()

    private val nibPaint: Paint = {
        val nibPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        nibPaint.color = Color.LTGRAY
        nibPaint
    }()

    interface onKnobValueChangedListener{
        fun onKnobValueChanged(ident: Int, value: Float)
    }

    fun changeKnobColor(newColor: Int){
        controlKnobPaint.color = newColor
        invalidate()
    }

    fun getCurrentColor(): Int{
        return controlKnobPaint.color
    }

    fun setCurrentColor(newColor: Int){
        controlKnobPaint.color = newColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode: Int = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode: Int = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize: Int = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize: Int = MeasureSpec.getSize(heightMeasureSpec)

        var width: Int = suggestedMinimumWidth
        var height: Int = suggestedMinimumHeight

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            width = widthSize
            height = heightSize
        } else if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize
            height = if(heightMode == MeasureSpec.EXACTLY && heightSize < widthSize){
                heightSize
            } else{
                width
            }
        } else if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize
            width = if(widthMode == MeasureSpec.EXACTLY && widthSize < heightSize){
                widthSize
            } else{
                height
            }
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            width = Math.min(widthSize, heightSize)
            height = width
        } else if(widthMode == MeasureSpec.AT_MOST){
            width = widthSize
            height = width
        } else if(heightMode == MeasureSpec.AT_MOST){
            height = heightSize
            width = height
        }else{
            width = Math.max(widthSize, heightSize)
            if(width < 500){
                width = 500
            }
            height = width
        }

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec))
    }

    private var knobValueChangedListener: onKnobValueChangedListener? = null

    fun setOnKnobValueChangedListener(onKnobValueChangedListener: onKnobValueChangedListener){
        knobValueChangedListener = onKnobValueChangedListener
    }

    //these values set the min and max values for the turning of the nib
    var minValue: Float = 0.0f
        set(value) {field = value}
        get() {return field}
    var maxValue: Float = 0.0f
        set(value) {field = value}
        get() {return field}

    //when the user touches the nib, the new position of the new is updated, and the calculation
    //is given, using the given min and max values passed in
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(event !is MotionEvent) return false

        val relativeX: Double = (event.x - controlKnobRect.centerX().toDouble())
        val relativeY: Double = (event.y - controlKnobRect.centerY().toDouble())
        theta = Math.atan2(relativeY, relativeX)


        if(knobValueChangedListener != null) knobValueChangedListener?.onKnobValueChanged(ident, convertDegreesToRangedValue())

        return true
    }

    //draws the nib and control knob with respect to padding
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //smart casts to Canvas
        if(canvas !is Canvas) return

        val availableWidth: Float = (width - paddingLeft - paddingRight).toFloat()
        val availableHeight: Float = (height - paddingTop - paddingBottom).toFloat()
        val controlKnobEdgeLength: Float = Math.min(availableHeight, availableWidth)

        controlKnobRect.left = paddingLeft.toFloat()
        controlKnobRect.top = paddingTop.toFloat()
        controlKnobRect.right = paddingLeft.toFloat() + controlKnobEdgeLength
        controlKnobRect.bottom = paddingTop.toFloat() + controlKnobEdgeLength

        if (availableWidth > availableHeight){
            val controlKnobHorizontalDisplacement: Float = (availableWidth - controlKnobRect.width()) * 0.5f
            controlKnobRect.left += controlKnobHorizontalDisplacement
            controlKnobRect.right += controlKnobHorizontalDisplacement
        }else{
            val controlKnobVerticalDisplacement: Float = (availableHeight - controlKnobRect.height()) * 0.5f
            controlKnobRect.top += controlKnobVerticalDisplacement
            controlKnobRect.bottom += controlKnobVerticalDisplacement
        }

        val nibCenterDisplacement: Float = controlKnobRect.width() * 0.35f
        nibCenter.x = controlKnobRect.centerX() + (nibCenterDisplacement * Math.cos(theta)).toFloat()
        nibCenter.y = controlKnobRect.centerY() + (nibCenterDisplacement * Math.sin(theta)).toFloat()

        val nibRadius: Float = nibCenterDisplacement * 0.25f
        nibRect.left = nibCenter.x - nibRadius
        nibRect.top = nibCenter.y - nibRadius
        nibRect.right = nibCenter.x + nibRadius
        nibRect.bottom = nibCenter.y + nibRadius

        canvas.drawOval(controlKnobRect, controlKnobPaint)
        canvas.drawOval(nibRect, nibPaint)
    }

    //this function converts the theta(radians) to degrees
    //it then converts the degrees into the range given by the min/max values
    //and returns the current value in the range
    private fun convertDegreesToRangedValue(): Float{
        var degrees = theta * radianToDegree
        if(degrees < 0) degrees += 360

        var delta: Float = 0.0f
        var result: Float = 0.0f

        if(minValue < 0.0f){
            delta = (maxValue + Math.abs(minValue))/360
            result = (degrees * delta).toFloat() - minValue
        }else{
            delta = (maxValue - minValue)/360
            result = (degrees * delta).toFloat() + minValue
        }

        return result
    }
}