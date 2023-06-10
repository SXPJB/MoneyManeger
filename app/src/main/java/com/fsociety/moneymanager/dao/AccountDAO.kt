package com.fsociety.moneymanager.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fsociety.moneymanager.model.entities.AccountVO

@Dao
interface AccountDAO {

    @Query("SELECT * FROM account")
    suspend fun findAll(): List<AccountVO>

    @Query("SELECT * FROM account WHERE account_id = :id")
    suspend fun findAccountById(id: Int): AccountVO

    @Insert
    suspend fun saveAccount(account: AccountVO): Long

    @Delete
    suspend fun deleteAccount(account: AccountVO)

    @Update
    suspend fun updateAccount(account: AccountVO)
}