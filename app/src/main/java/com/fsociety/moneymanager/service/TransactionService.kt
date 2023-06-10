package com.fsociety.moneymanager.service

import com.fsociety.moneymanager.config.db.MoneyManagerDB
import com.fsociety.moneymanager.model.TransactionView
import com.fsociety.moneymanager.model.entities.AccountVO
import com.fsociety.moneymanager.model.entities.TransactionVO
import org.joda.time.DateTime

class TransactionService(
    private val db: MoneyManagerDB
) {
    suspend fun finTransactions(idAccount: Int?): List<TransactionView> {
        val transactions: List<TransactionVO> = if (idAccount != null) {
            db.transactionDAO().getTransactionsByAccount(idAccount)
        } else {
            db.transactionDAO().getAllTransactions()
        }
        return transactions.map { TransactionView.toTransactionView(it) }
    }

    suspend fun findById(id: Int): TransactionVO = db.transactionDAO().findById(id)

    suspend fun save(transaction: TransactionVO): Long {
        return db.transactionDAO().saveTransaction(transaction)
    }

    suspend fun update(id: Int, data: Map<String, Any>): Int {
        val transaction = findById(id)
        if (transaction.id == 0) {
            throw Exception("Transaction not found")
        }
        if (data.containsKey("accountId")) {
            transaction.accountId = data["accountId"] as AccountVO
        }
        if (data.containsKey("amount")) {
            transaction.amount = data["amount"] as Double
        }
        if (data.containsKey("description")) {
            transaction.description = data["description"] as String
        }
        if (data.containsKey("transactionDate")) {
            transaction.transactionDate = data["transactionDate"] as DateTime
        }
        transaction.updateAt = DateTime.now()
        return db.transactionDAO().updateTransaction(transaction)
    }

    suspend fun delete(id: Int) {
        val transaction = findById(id)
        db.transactionDAO().deleteTransaction(transaction)
    }
}