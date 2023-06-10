package com.fsociety.moneymanager.views.fargments.accounts

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.fsociety.moneymanager.R
import com.fsociety.moneymanager.config.db.MoneyManagerDB
import com.fsociety.moneymanager.databinding.FragmentAccountsBinding
import com.fsociety.moneymanager.model.AccountView
import com.fsociety.moneymanager.service.AccountService
import com.fsociety.moneymanager.views.fargments.accounts.listener.AccountListener
import kotlinx.coroutines.launch

class AccountsFragment : Fragment(R.layout.fragment_accounts), AccountListener {

    private lateinit var binding: FragmentAccountsBinding
    private lateinit var bd: MoneyManagerDB
    private lateinit var accountService: AccountService
    private lateinit var accounts: List<AccountView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view.context)
        findAllAccounts(view.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun init(context: Context) {
        bd = Room.databaseBuilder(
            context,
            MoneyManagerDB::class.java,
            "money_manager"
        ).build()
        accountService = AccountService(bd)

        binding.btnAddAccount.setOnClickListener {
            Intent(context, AddAccountActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun findAllAccounts(context: Context) {
        lifecycleScope.launch {
            accounts = accountService.findAll()
            binding.rvAccounts.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = AccountAdapter(accounts, this@AccountsFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        findAllAccounts(requireContext())
    }

    override fun onEditAccount(id: Int) {
        Intent(requireContext(), AddAccountActivity::class.java).also {
            it.putExtra("account_id", id)
            startActivity(it)
        }
    }

    override fun onDeleteAccount(id: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete this account?")
            .setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch {
                    accountService.deleteAccount(id)
                    findAllAccounts(requireContext())
                }
            }
            .setNegativeButton("No") { _, _ -> }
            .create()
            .show()
    }
}