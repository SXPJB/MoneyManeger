package com.fsociety.moneymanager.config.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fsociety.moneymanager.dao.AccountDAO
import com.fsociety.moneymanager.dao.TransactionDAO
import com.fsociety.moneymanager.model.entities.AccountVO
import com.fsociety.moneymanager.model.entities.TransactionVO

@Database(
    entities = [AccountVO::class, TransactionVO::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MoneyManagerDB : RoomDatabase() {
    abstract fun accountDAO(): AccountDAO
    abstract fun transactionDAO(): TransactionDAO
}