package com.fsociety.moneymanager.views.fargments.transaction

import android.app.DatePickerDialog
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
import com.fsociety.moneymanager.model.TransactionType
import com.fsociety.moneymanager.model.entities.TransactionVO
import com.fsociety.moneymanager.service.AccountService
import com.fsociety.moneymanager.service.TransactionService
import com.fsociety.moneymanager.utils.DATE_FORMAT
import com.fsociety.moneymanager.views.adapters.SpinnerAdapter
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.Calendar

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

        defaultValues()

        populateAccountSpinner()
        populateTransactionTypeSpinner()
        setListeners()
    }

    private fun defaultValues() {
        binding.etTransactionDate.setText(DateTime.now().toString(DATE_FORMAT))
    }

    private fun setListeners() {
        binding.etTransactionDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun populateAccountSpinner() {
        lifecycleScope.launch {
            val accounts = accountService.findAll()
            binding.spAccountTF.adapter = SpinnerAdapter(
                this@TransactionFormActivity,
                accounts.map { AccountView(it.id, it.name) }
            )
            loadData()
        }
    }

    private fun populateTransactionTypeSpinner() {
        binding.spTranssactionType.adapter = SpinnerAdapter(
            this@TransactionFormActivity,
            TransactionType.getAllTypes()
        )
    }

    private suspend fun loadData() {
        transactionId = intent.getIntExtra("transaction_id", 0)
        if (transactionId != 0) {
            transactionService.findById(transactionId).let {
                val accountAdapter = binding.spAccountTF.adapter as SpinnerAdapter<AccountView>
                val accountSelected =
                    accountAdapter.getPosition { view -> view.id == it.accountId.id }
                binding.spAccountTF.setSelection(accountSelected)
                val transactionTypeAdapter =
                    binding.spTranssactionType.adapter as SpinnerAdapter<TransactionType>
                val transactionTypeSelected =
                    transactionTypeAdapter.getPosition { view -> view.id == it.type }
                binding.spTranssactionType.setSelection(transactionTypeSelected)
                binding.etTAmount.setText(it.amount.toString())
                binding.etTDescription.setText(it.description)
                val dateStr = it.transactionDate.toString(DATE_FORMAT)
                binding.etTransactionDate.setText(dateStr)
            }
        }
    }

    private fun saveTransaction() {
        lifecycleScope.launch {
            val accountSelected = binding.spAccountTF.selectedItem as AccountView
            val transactionTypeSelected = binding.spTranssactionType.selectedItem as TransactionType
            accountSelected.id?.let {
                accountService.findAccountById(it)
            }?.also {
                val transaction = TransactionVO(
                    id = 0,
                    amount = (binding.etTAmount.text.toString()
                        .toDouble() * transactionTypeSelected.modifier),
                    description = binding.etTDescription.text.toString(),
                    type = transactionTypeSelected.id,
                    accountId = it
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
            }?.also {
                val data = mapOf(
                    "amount" to binding.etTAmount.text.toString().toDouble(),
                    "description" to binding.etTDescription.text.toString(),
                    "type" to (binding.spTranssactionType.selectedItem as TransactionType).id,
                    "accountId" to it,
                    "transactionDate" to DateTimeFormat
                        .forPattern(DATE_FORMAT)
                        .parseDateTime(binding.etTransactionDate.text.toString())
                )
                transactionService.update(transactionId, data)
                finish()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                binding.etTransactionDate.setText(
                    DateTimeFormat
                        .forPattern(DATE_FORMAT)
                        .print(selectedDate.timeInMillis)
                )
            },
            year,
            month,
            day
        ).show()
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