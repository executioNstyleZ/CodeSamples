package com.apps.jaredshaw.hotseatbattleships

import android.app.Application
import android.content.Context
import android.database.DataSetObserver
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.TextView
import org.w3c.dom.Text

/**
 * Created by Jared on 11/10/2017.
 */
class GameListAdapter(context: Context) : ListAdapter, BaseAdapter() {


    private var summaries: MutableList<String>
    private var myContext: Context? = null

    init {
        myContext = context
        summaries = GameList.getGameSummaries()
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val info: String = summaries[position]

        val textView: TextView = TextView(myContext)
        textView.setText(info)

        return textView
    }

    override fun registerDataSetObserver(p0: DataSetObserver?) {}

    override fun getItemViewType(p0: Int): Int {return 0}

    override fun getItem(position: Int): Any {return position}

    override fun getViewTypeCount(): Int {return 1}

    override fun getItemId(p0: Int): Long {return 0}

    override fun hasStableIds(): Boolean {return false}

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {}

    override fun getCount(): Int {return summaries.size}

    override fun isEnabled(p0: Int): Boolean { return true    }

    override fun areAllItemsEnabled(): Boolean {return true}

}