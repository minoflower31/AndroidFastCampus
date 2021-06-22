package com.minoflower.secretdiary

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    private val openButton: AppCompatButton by lazy {
        findViewById(R.id.openButton)
    }

    private val resetButton: AppCompatButton by lazy {
        findViewById(R.id.resetButton)
    }

    private val numberPicker1: NumberPicker by lazy {
        findViewById(R.id.numberPicker1)
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById(R.id.numberPicker2)
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById(R.id.numberPicker3)
    }

    private var isSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNumberPickers()
        openLock()
        setPassword()
    }

    private fun initNumberPickers() {
        numberPicker1.apply {
            minValue = 0
            maxValue = 9
        }

        numberPicker2.apply {
            minValue = 0
            maxValue = 9
        }

        numberPicker3.apply {
            minValue = 0
            maxValue = 9
        }
    }

    private fun openLock() {
        openButton.setOnClickListener {
            val sharedPref = getSharedPreferences("secret", MODE_PRIVATE)
            val pw = sharedPref.getString("password", "000")

            if (isSet) {
                Toast.makeText(this, "비밀번호를 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pw != getPassword()) {
                Toast.makeText(this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            startActivity(
                Intent(this, DetailActivity::class.java)
            )
        }
    }

    private fun getPassword(): String {
        return numberPicker1.value.toString() + numberPicker2.value.toString() + numberPicker3.value.toString()
    }

    private fun setPassword() {
        resetButton.setOnClickListener {
            val sharedPref = getSharedPreferences("secret", MODE_PRIVATE)
            if (!isSet) {
                val data = sharedPref.getString("password", "000")
                if (data == getPassword()) {
                    isSet = true
                    changedResetButton(isSet)
                }
            } else {
                sharedPref.edit(true) {
                    putString("password", getPassword())
                }
                isSet = false
                changedResetButton(isSet)
            }
        }
    }

    private fun changedResetButton(bool: Boolean) {
        if (bool) resetButton.setBackgroundColor(Color.RED)
        else resetButton.setBackgroundColor(Color.GREEN)
    }
}
