package com.cs4530.jaredshaw.brushcontrol

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //everything is done inside the brush controller for easy portability
        val brushController: BrushController = BrushController(this)
        setContentView(brushController)
    }
}
