package com.fsociety.moneymanager.views.fargments.transaction

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.fsociety.moneymanager.R
import com.fsociety.moneymanager.config.db.MoneyManagerDB
import com.fsociety.moneymanager.databinding.ActivityTransactionFormBinding
import com.fsociety.moneymanager.model.AccountView
import com.fsociety.moneymanager.model.entities.TransactionVO
import com.fsociety.moneymanager.service.AccountService
import com.fsociety.moneymanager.service.TransactionService
import com.fsociety.moneymanager.views.adapters.AccountAdapterSpinner
import kotlinx.coroutines.launch

class TransactionFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionFormBinding

    private var transactionId: Int = 0

    private lateinit var accountService: AccountService
    private lateinit var transactionService: TransactionService

    private lateinit var db: MoneyManagerDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbTranssactionsBar)
        init()
    }

    private fun init() {
        db = Room.databaseBuilder(
            this,
            MoneyManagerDB::class.java,
            "money_manager"
        ).build()
        accountService = AccountService(db)
        transactionService = TransactionService(db)
        populateAccountSpinner()
    }

    private fun populateAccountSpinner() {
        lifecycleScope.launch {
            val accounts = accountService.findAll()
            binding.spAccountTF.adapter = AccountAdapterSpinner(
                this@TransactionFormActivity,
                android.R.layout.simple_spinner_dropdown_item,
                accounts
            )
            loadData()
        }
    }

    private suspend fun loadData() {
        transactionId = intent.getIntExtra("transaction_id", 0)
        if (transactionId != 0) {
            transactionService.findById(transactionId).let {
                val adapter = binding.spAccountTF.adapter as AccountAdapterSpinner
                val data = adapter.getPosition(AccountView(it.accountId.id, it.accountId.name))
                binding.spAccountTF.setSelection(data)
                binding.etTAmount.setText(it.amount.toString())
                binding.etTDescription.setText(it.description)
            }
        }
    }

    private fun saveTransaction() {
        lifecycleScope.launch {
            val accountSelected = binding.spAccountTF.selectedItem as AccountView
            accountSelected.id?.let {
                accountService.findAccountById(it)
            }.also {
                val transaction = TransactionVO(
                    id = 0,
                    amount = binding.etTAmount.text.toString().toDouble(),
                    description = binding.etTDescription.text.toString(),
                    accountId = it!!
                )
                transactionService.save(transaction!!)
                finish()
            }
        }
    }

    private fun updateTransaction() {
        lifecycleScope.launch {
            val accountSelected = binding.spAccountTF.selectedItem as AccountView
            accountSelected.id?.let {
                accountService.findAccountById(it)
            }.also {
                val data = mapOf(
                    "amount" to binding.etTAmount.text.toString().toDouble(),
                    "description" to binding.etTDescription.text.toString(),
                    "accountId" to it!!
                )
                transactionService.update(transactionId, data)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_account_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miSave -> if (transactionId == 0) saveTransaction() else updateTransaction()
            R.id.miClose -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}