package com.fsociety.moneymanager.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.fsociety.moneymanager.R

val PIN_CODE: String = "797589"

fun setCurrentFragment(
    supportFragmentManager: FragmentManager,
    fragment: Fragment,
    container: Int
) =
    supportFragmentManager.beginTransaction().apply {
        replace(container, fragment)
        commit()
    }

fun formatBalance(balance: Double): String {
    return String.format("$%,.2f", balance)
}
