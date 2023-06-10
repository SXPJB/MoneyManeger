package com.fsociety.moneymanager.views.fargments.accounts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.fsociety.moneymanager.R
import com.fsociety.moneymanager.config.db.MoneyManagerDB
import com.fsociety.moneymanager.databinding.ActivityAddAccountBinding
import com.fsociety.moneymanager.model.entities.AccountVO
import com.fsociety.moneymanager.service.AccountService
import kotlinx.coroutines.launch

class AddAccountActivity : AppCompatActivity() {
    private var accountId: Int = 0
    private lateinit var binding: ActivityAddAccountBinding

    private lateinit var db: MoneyManagerDB

    private lateinit var accountService: AccountService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbAccountsBar)
        init()
    }

    private fun init() {
        db = Room.databaseBuilder(
            this,
            MoneyManagerDB::class.java,
            "money_manager"
        ).build()
        accountService = AccountService(db)
        binding.clDetailsContainer.visibility = android.view.View.GONE
        loadData()
    }

    private fun saveAccount() {
        val name = binding.etAccountName.text.toString().trim()
        val initialAmount: String = binding.etInitialAmount.text.toString()
        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (initialAmount.isEmpty()) {
            Toast.makeText(this, "Initial amount is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (initialAmount.toDouble() <= 0) {
            Toast.makeText(this, "Initial amount must be greater than 0", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            accountService.saveAccount(
                AccountVO(name = name), initialAmount.toDouble()
            )
            finish()
        }
    }

    private fun editAccount() {
        val name = binding.etAccountName.text.toString().trim()
        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            accountService.updateAccount(accountId, name)
            finish()
        }
    }

    private fun loadData() {
        accountId = intent.getIntExtra("account_id", 0)
        if (accountId != 0) {
            lifecycleScope.launch {
                binding.etInitialAmount.isEnabled = false
                binding.clDetailsContainer.visibility = android.view.View.VISIBLE

                accountService.findAccountById(accountId).let {
                    binding.etAccountName.setText(it.name)
                    binding.tvCreationDate.text = it.createAt.toString("dd/MM/yyyy")
                }
                accountService.getBalance(accountId).let {
                    binding.etInitialAmount.setText(it.toString())
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_account_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miSave -> if (accountId == 0) saveAccount() else editAccount()
            R.id.miClose -> finish()
        }
        return true
    }

}