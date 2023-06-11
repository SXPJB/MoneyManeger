package com.fsociety.moneymanager.service

import com.fsociety.moneymanager.config.db.MoneyManagerDB
import com.fsociety.moneymanager.model.TransactionType
import com.fsociety.moneymanager.model.TransactionView
import com.fsociety.moneymanager.model.entities.AccountVO
import com.fsociety.moneymanager.model.entities.TransactionVO
import org.joda.time.DateTime
import kotlin.math.abs

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
        data["accountId"]?.let {
            transaction.accountId = it as AccountVO
        }
        data["amount"]?.let {
            val type: TransactionType = TransactionType.getById(data["type"] as Int)
            val amount: Double = abs(data["amount"] as Double)
            transaction.amount = type.modifier * amount
        }
        data["description"]?.let {
            transaction.description = it as String
        }
        data["type"]?.let {
            transaction.type = it as Int
        }
        data["transactionDate"]?.let {
            transaction.transactionDate = it as DateTime
        }
        transaction.updateAt = DateTime.now()
        return db.transactionDAO().updateTransaction(transaction)
    }

    suspend fun delete(id: Int) {
        val transaction = findById(id)
        db.transactionDAO().deleteTransaction(transaction)
    }
}