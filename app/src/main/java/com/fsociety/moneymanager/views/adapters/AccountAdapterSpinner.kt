package com.fsociety.moneymanager.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.fsociety.moneymanager.model.AccountView

class AccountAdapterSpinner(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private val accounts: List<AccountView>
) : ArrayAdapter<AccountView>(context, layoutResource, accounts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val row = inflater.inflate(layoutResource, parent, false)
        row.findViewById<TextView>(android.R.id.text1).text = accounts[position].name
        return row
    }

    override fun getPosition(item: AccountView?): Int {
        return accounts.indexOfFirst { it.id == item?.id }
    }

}