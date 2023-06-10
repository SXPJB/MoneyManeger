package com.fsociety.moneymanager.model

import com.fsociety.moneymanager.model.entities.AccountVO


data class AccountView(
    val id: Int? = null,
    val name: String,
    val balance: Double = 0.0
) {
    companion object {
        fun toAccountView(account: AccountVO, balance: Double): AccountView {
            return AccountView(
                id = account.id,
                name = account.name,
                balance = balance
            )
        }
    }
}

