package com.apps.jaredshaw.hotseatbattleships

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.GridView

/**
 * Created by Jared on 11/11/2017.
 */
class GridAdapter(context: Context, spectatorValue: Boolean) : BaseAdapter() {

    private var myContext: Context
    private lateinit var grid: HashMap<Int,Int>
    private var spectator: Boolean

    init {

        myContext = context
        spectator = spectatorValue

        buildDataSet()
    }


    private fun buildDataSet(){
        grid = HashMap<Int,Int>()

        //set everything to water
        for(i in 0 until 200)
        {
            grid[i] = 0
        }

        //for players
        var offensiveGrid: HashMap<Int, Int> = GameList.getOffensiveGridFor(Database.getEmail())
        var defensiveGrid: HashMap<Int, Int> = GameList.getDefensiveGridFor(Database.getEmail())

        //for spectators
        if(spectator == true){
            offensiveGrid = GameList.getOffensiveGridFor(GameList.getCurrentTurn())
            defensiveGrid = GameList.getDefensiveGridFor(GameList.getCurrentTurn())
        }

        var keys = offensiveGrid.keys
        var values = offensiveGrid.values
        for(i in keys.indices){
            grid[keys.elementAt(i)] = values.elementAt(i)
        }

        keys = defensiveGrid.keys
        values = defensiveGrid.values
        for(i in keys.indices){
            val offset = keys.elementAt(i) + 100
            grid[offset] = values.elementAt(i)
        }
    }

    override fun getCount(): Int {
        return grid.size
    }

    override fun isEnabled(p0: Int): Boolean {return true}

    override fun isEmpty(): Boolean {return false}

    //0 = water BLUE
    //1 = ship  GREY
    //2 = hit   RED
    //3 = miss  WHITE
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = TextView(myContext)
        view.gravity = Gravity.CENTER

        val gridState = grid[position]

        when(gridState){
            0 -> {
                if(position >= 0 && position < 100) view.setBackgroundColor(Color.BLUE)
                else view.setBackgroundColor(Color.BLUE - 100)
            }
            1 -> {
                view.setBackgroundColor(Color.GRAY)
                view.setText("S")
            }
            2 -> {
                view.setBackgroundColor(Color.RED)
                view.setText("H")
            }
            3 -> {
                view.setBackgroundColor(Color.WHITE)
                view.setText("M")
            }
        }

        return view
    }

    override fun registerDataSetObserver(p0: DataSetObserver?) {}

    override fun getItemViewType(p0: Int): Int {return 0}

    override fun getItem(p0: Int): Any {return p0}

    override fun getViewTypeCount(): Int {return 1}

    override fun getItemId(p0: Int): Long {return 0}

    override fun hasStableIds(): Boolean {return false}

    override fun areAllItemsEnabled(): Boolean {return true}

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {}

}