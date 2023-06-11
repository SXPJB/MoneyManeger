package com.fsociety.moneymanager.model.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(
    tableName = "transaction",
    foreignKeys = [ForeignKey(
        entity = AccountVO::class,
        parentColumns = ["account_id"],
        childColumns = ["account_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TransactionVO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val id: Int = 0,
    @Embedded
    var accountId: AccountVO,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "type")
    var type: Int,
    @ColumnInfo(name = "amount")
    var amount: Double,
    @ColumnInfo(name = "transaction_date")
    var transactionDate: DateTime = DateTime.now(),
    @ColumnInfo(name = "ts_create_at")
    var createAt: DateTime = DateTime.now(),
    @ColumnInfo(name = "ts_update_at")
    var updateAt: DateTime? = null,
)
