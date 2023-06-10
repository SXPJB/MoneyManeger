package com.fsociety.moneymanager.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.fsociety.moneymanager.R
import com.fsociety.moneymanager.databinding.ActivityContainerBinding
import com.fsociety.moneymanager.utils.setCurrentFragment
import com.fsociety.moneymanager.views.fargments.accounts.AccountsFragment
import com.fsociety.moneymanager.views.fargments.dashboard.DashboardFragment
import com.fsociety.moneymanager.views.fargments.transaction.TransactionFragment

class ContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContainerBinding

    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var accountsFragment: AccountsFragment
    private lateinit var transactionFragment: TransactionFragment

    private lateinit var toogle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragment()
        setUpDrawer()
        setUpNavMenuItems()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initFragment() {
        dashboardFragment = DashboardFragment()
        accountsFragment = AccountsFragment()
        transactionFragment = TransactionFragment()
        setCurrentFragment(
            supportFragmentManager,
            dashboardFragment,
            R.id.flContainerView
        )
    }

    private fun setUpDrawer() {
        setSupportActionBar(binding.tbContainerView)

        toogle = ActionBarDrawerToggle(this, binding.dlContainerView, R.string.open, R.string.close)
        binding.dlContainerView.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUpNavMenuItems() {
        binding.nvMenuView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miDashboard -> {
                    setCurrentFragment(
                        supportFragmentManager,
                        dashboardFragment,
                        R.id.flContainerView,
                    )
                    binding.dlContainerView.closeDrawers()
                }

                R.id.miAccounts -> {
                    setCurrentFragment(
                        supportFragmentManager,
                        accountsFragment,
                        R.id.flContainerView,
                    )
                    binding.dlContainerView.closeDrawers()
                }

                R.id.miTransactions -> {
                    setCurrentFragment(
                        supportFragmentManager,
                        transactionFragment,
                        R.id.flContainerView,
                    )
                    binding.dlContainerView.closeDrawers()
                }
            }
            true
        }
    }
}