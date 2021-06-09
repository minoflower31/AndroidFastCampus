package com.minoflower.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.isDigitsOnly

class MainActivity : AppCompatActivity() {
    private val heightEditText: EditText by lazy {
        findViewById(R.id.heightEditText)
    }
    private val weightEditText: EditText by lazy {
        findViewById(R.id.weightEditText)
    }
    private val confirmButton: Button by lazy {
        findViewById(R.id.confirmButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        confirmButton.setOnClickListener {
            val height = heightEditText.text.toString()
            val weight = weightEditText.text.toString()

            if (!height.isDigitsOnly() || !weight.isDigitsOnly()) {
                Toast.makeText(this, "숫자를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (height.isBlank() || weight.isBlank()) {
                Toast.makeText(this, "빈 칸을 채워주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, ResultActivity::class.java)
            intent.apply {
                putExtra("height", height.toDouble())
                putExtra("weight", weight.toDouble())
            }
            startActivity(intent)
        }
    }
}