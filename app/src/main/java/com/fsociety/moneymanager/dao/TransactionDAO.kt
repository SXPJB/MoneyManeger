package com.fsociety.moneymanager.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fsociety.moneymanager.model.entities.TransactionVO

@Dao
interface TransactionDAO {

    @Query("SELECT sum(amount) FROM `transaction` where account_id = :accountId")
    suspend fun getBalance(accountId: Int): Double

    @Query("SELECT * FROM `transaction` where account_id = :accountId order by transaction_date desc")
    suspend fun getTransactionsByAccount(accountId: Int): List<TransactionVO>

    @Query("SELECT * FROM `transaction` order by transaction_date desc")
    suspend fun getAllTransactions(): List<TransactionVO>

    @Query("SELECT * FROM `transaction` where transaction_id = :id")
    suspend fun findById(id: Int): TransactionVO

    @Insert
    suspend fun saveTransaction(transaction: TransactionVO): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionVO): Int

    @Delete
    suspend fun deleteTransaction(transaction: TransactionVO)
}