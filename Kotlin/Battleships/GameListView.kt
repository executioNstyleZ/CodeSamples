package com.apps.jaredshaw.hotseatbattleships

import android.content.Context
import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView

/**
 * Created by Jared on 11/11/2017.
 */
class GameListView(context: Context?) : ListView(context), ListAdapter {

    private var summaries: MutableList<String>
    private var myContext: Context? = null

    fun removeSummaryAt(position: Int){
        summaries.removeAt(position)
    }

    init {
        myContext = context
        summaries = GameList.getGameSummaries()
    }

    override fun isEnabled(p0: Int): Boolean {return true}

    override fun isEmpty(): Boolean {return false}

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val info: String = summaries[position]

        val textView: TextView = TextView(myContext)
        textView.setText(info)

        return textView    }

    override fun registerDataSetObserver(p0: DataSetObserver?) {}

    override fun getItemViewType(p0: Int): Int {return 0}

    override fun getItem(p0: Int): Any {return p0}

    override fun getViewTypeCount(): Int {return 1}

    override fun getItemId(p0: Int): Long {return 0}

    override fun hasStableIds(): Boolean {return false}

    override fun areAllItemsEnabled(): Boolean {return true}

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {}


}