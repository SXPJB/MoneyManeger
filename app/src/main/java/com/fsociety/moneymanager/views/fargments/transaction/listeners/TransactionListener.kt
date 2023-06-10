package com.fsociety.moneymanager.views.fargments.transaction.listeners

interface TransactionListener {
    fun onUpdateTransaction(id: Int)
    fun onDeleteTransaction(id: Int)
}