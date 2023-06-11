package com.fsociety.moneymanager.service

import com.fsociety.moneymanager.config.db.MoneyManagerDB
import com.fsociety.moneymanager.model.AccountView
import com.fsociety.moneymanager.model.TransactionType
import com.fsociety.moneymanager.model.entities.AccountVO
import com.fsociety.moneymanager.model.entities.TransactionVO
import org.joda.time.DateTime

class AccountService(
    val db: MoneyManagerDB
) {
    suspend fun findAll(): List<AccountView> {
        val accounts = db.accountDAO().findAll()
        val accountViews = mutableListOf<AccountView>()
        accounts.forEach { account ->
            val balance = db.transactionDAO().getBalance(account.id)
            accountViews.add(AccountView.toAccountView(account, balance))
        }
        return accountViews
    }

    suspend fun saveAccount(account: AccountVO, amount: Double): Long {
        val accountId: Long = db.accountDAO().saveAccount(account)
        val transactionVO = TransactionVO(
            accountId = AccountVO(
                id = accountId.toInt(),
                name = account.name
            ),
            amount = amount,
            description = "Initial balance",
            type = TransactionType.INCOME.id
        )
        db.transactionDAO()
            .saveTransaction(transactionVO)
        return accountId
    }

    suspend fun deleteAccount(id: Int) {
        val account: AccountVO = db.accountDAO().findAccountById(id)
        if (account.id == 0) {
            throw Exception("Account not found")
        }
        db.accountDAO().deleteAccount(account)
    }

    suspend fun updateAccount(id: Int, name: String) {
        val account: AccountVO = db.accountDAO().findAccountById(id)
        if (account.id == 0) {
            throw Exception("Account not found")
        }
        account.name = name
        account.updateAt = DateTime.now()
        db.accountDAO().updateAccount(account)
    }

    suspend fun findAccountById(id: Int): AccountVO {
        return db.accountDAO().findAccountById(id)
    }

    suspend fun getBalance(accountId: Int): Double {
        return db.transactionDAO().getBalance(accountId)
    }
}