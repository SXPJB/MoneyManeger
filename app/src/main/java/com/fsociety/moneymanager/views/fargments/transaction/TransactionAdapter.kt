package com.fsociety.moneymanager.views.fargments.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fsociety.moneymanager.R
import com.fsociety.moneymanager.databinding.ItemTranssactionBinding
import com.fsociety.moneymanager.model.TransactionType
import com.fsociety.moneymanager.model.TransactionView
import com.fsociety.moneymanager.utils.DATE_FORMAT
import com.fsociety.moneymanager.views.fargments.transaction.listeners.TransactionListener

class TransactionAdapter(
    private val transactions: List<TransactionView>,
    private val listener: TransactionListener
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView) {
        val binding = ItemTranssactionBinding.bind(itemView)
        val view = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transsaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val color =
            if (transactions[position].type == TransactionType.INCOME)
                holder.view.context.getColor(R.color.green)
            else
                holder.view.context.getColor(R.color.red)
        holder.binding.apply {
            tvAccount.text = transactions[position].accountName
            tvDescription.text = transactions[position].description
            tvAmount.text = transactions[position].amount.toString()
            tvAmount.setTextColor(color)
            val dateStr =
                "Transaction Date: ${transactions[position].transactionDate.toString(DATE_FORMAT)}"
            tvTransactionDate.text = dateStr
            btnDeleteTransaction.setOnClickListener {
                listener.onDeleteTransaction(transactions[position].id)
            }
            cvContainer.setOnClickListener {
                listener.onUpdateTransaction(transactions[position].id)
            }
        }
    }
}