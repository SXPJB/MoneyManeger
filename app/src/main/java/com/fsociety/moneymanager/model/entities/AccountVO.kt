package com.fsociety.moneymanager.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.joda.time.DateTime

@Entity(
    tableName = "account"
)
data class AccountVO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "ac_create_at")
    val createAt: DateTime = DateTime.now(),
    @ColumnInfo(name = "ac_update_at")
    var updateAt: DateTime? = null,
)