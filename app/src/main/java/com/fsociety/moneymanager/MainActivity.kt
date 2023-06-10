package com.fsociety.moneymanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fsociety.moneymanager.databinding.ActivityMainBinding
import com.fsociety.moneymanager.utils.PIN_CODE
import com.fsociety.moneymanager.utils.PinCodeTextWatcher
import com.fsociety.moneymanager.views.ContainerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListeners()
    }

    private fun initViews() {
        binding.btnEnterButton.isEnabled = false
    }

    private fun initListeners() {
        binding.btnEnterButton.setOnClickListener(::onEnterClick)
        val textWatcher = PinCodeTextWatcher(binding.btnEnterButton, PIN_CODE)
        binding.etPinCode.addTextChangedListener(textWatcher)
    }

    private fun onEnterClick(it: View) {
        Intent(this, ContainerActivity::class.java).also {
            resetView()
            startActivity(it)
        }
    }

    private fun resetView() {
        binding.etPinCode.text.clear()
        binding.btnEnterButton.isEnabled = false
    }
}

