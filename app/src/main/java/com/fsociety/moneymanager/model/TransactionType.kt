package com.fsociety.moneymanager.model

enum class TransactionType(val id: Int, name: String, val modifier: Int) {
    INCOME(1, "Income", 1),
    EXPENSE(2, "Expense", -1);

    override fun toString(): String {
        return name
    }

    companion object {
        fun getAllTypes(): List<TransactionType> {
            return listOf(INCOME, EXPENSE)
        }

        fun getById(id: Int): TransactionType {
            return when (id) {
                1 -> INCOME
                2 -> EXPENSE
                else -> INCOME
            }
        }

        fun getModifier(transactionType: TransactionType): Int {
            return when (transactionType) {
                INCOME -> 1
                EXPENSE -> -1
            }
        }
    }
}