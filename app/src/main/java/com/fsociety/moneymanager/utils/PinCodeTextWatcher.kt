package com.fsociety.moneymanager.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button

class PinCodeTextWatcher(
    private val btnEnterButton: Button,
    private val PIN_CODE: String
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (s.toString() == PIN_CODE) {
            btnEnterButton.isEnabled = true
        }
    }
}