package com.fsociety.moneymanager.views.fargments.transaction

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.fsociety.moneymanager.config.db.MoneyManagerDB
import com.fsociety.moneymanager.databinding.FragmentTransactionBinding
import com.fsociety.moneymanager.model.AccountView
import com.fsociety.moneymanager.model.TransactionView
import com.fsociety.moneymanager.service.AccountService
import com.fsociety.moneymanager.service.TransactionService
import com.fsociety.moneymanager.views.adapters.AccountAdapterSpinner
import com.fsociety.moneymanager.views.fargments.transaction.listeners.TransactionListener
import kotlinx.coroutines.launch

class TransactionFragment : Fragment(), TransactionListener {

    private lateinit var binding: FragmentTransactionBinding

    private lateinit var transactions: List<TransactionView>

    private lateinit var transactionService: TransactionService
    private lateinit var accountService: AccountService

    private lateinit var bd: MoneyManagerDB

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view.context)
        populateAccountSpinner(view.context)
        findAllTransactions(view.context)
        initListeners(view.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        findAllTransactions(requireContext())
    }

    private fun init(context: Context) {
        bd = Room.databaseBuilder(
            context,
            MoneyManagerDB::class.java,
            "money_manager"
        ).build()
        transactionService = TransactionService(bd)
        accountService = AccountService(bd)
    }

    private fun findAllTransactions(context: Context, accountId: Int? = null) {
        lifecycleScope.launch {
            transactions = transactionService.finTransactions(accountId)
            binding.rvTransactions.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = TransactionAdapter(transactions, this@TransactionFragment)
            }
        }
    }

    private fun populateAccountSpinner(context: Context) {
        lifecycleScope.launch {
            val accounts: MutableList<AccountView> = accountService.findAll().toMutableList()
            val account = AccountView(name = "All Accounts")
            accounts.add(0, account)
            binding.spAccounts.adapter = AccountAdapterSpinner(
                context,
                R.layout.simple_spinner_dropdown_item,
                accounts
            )
        }
    }

    private fun initListeners(context: Context) {
        binding.btnSearch.setOnClickListener {
            val account = binding.spAccounts.selectedItem as AccountView
            findAllTransactions(context, account.id)
        }
        binding.fbtnAddTransaction.setOnClickListener {
            Intent(context, TransactionFormActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onUpdateTransaction(id: Int) {
        Intent(requireContext(), TransactionFormActivity::class.java).also {
            it.putExtra("transaction_id", id)
            startActivity(it)
        }
    }

    override fun onDeleteTransaction(id: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete this transaction?")
            .setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch {
                    transactionService.delete(id)
                    findAllTransactions(requireContext())
                }
            }
            .setNegativeButton("No") { _, _ -> }
            .create()
            .show()
    }
}