package com.fsociety.moneymanager.views.fargments.accounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fsociety.moneymanager.R
import com.fsociety.moneymanager.databinding.ItemAccountBinding
import com.fsociety.moneymanager.model.AccountView
import com.fsociety.moneymanager.utils.formatBalance
import com.fsociety.moneymanager.views.fargments.accounts.listener.AccountListener

class AccountAdapter(
    private val accounts: List<AccountView>,
    private val listener: AccountListener
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {
    inner class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemAccountBinding = ItemAccountBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.binding.apply {
            tvAccountName.text = accounts[position].name
            tvAccountBalance.text = formatBalance(accounts[position].balance)
            btnDeleteAccount.setOnClickListener {
                accounts[position].id?.let { it1 -> listener.onDeleteAccount(it1) }
            }
            cvContainer.setOnClickListener {
                accounts[position].id?.let { it1 -> listener.onEditAccount(it1) }
            }
        }
    }
}