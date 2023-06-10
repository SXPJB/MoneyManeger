package com.fsociety.moneymanager.model

import com.fsociety.moneymanager.model.entities.TransactionVO
import org.joda.time.DateTime

data class TransactionView(
    val id: Int,
    val accountName: String,
    val amount: Double,
    val description: String,
    val transactionDate: DateTime = DateTime.now()
) {
    companion object {
        fun toTransactionView(transaction: TransactionVO): TransactionView {
            return TransactionView(
                transaction.id,
                transaction.accountId.name,
                transaction.amount,
                transaction.description,
                transaction.transactionDate
            )
        }
    }
}