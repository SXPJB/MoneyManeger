package com.fsociety.moneymanager.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerAdapter<T>(
    context: Context,
    private val items: List<T>
) : ArrayAdapter<T>(context, android.R.layout.simple_spinner_dropdown_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val row = LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        row.findViewById<TextView>(android.R.id.text1).text = items[position].toString()
        return row
    }

    fun getPosition(filer: (T) -> Boolean): Int {
        return items.indexOfFirst { filer(it) }
    }
}