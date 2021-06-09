package com.minoflower.bmicalculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ResultActivity : AppCompatActivity() {
    private val bmiTextView: TextView by lazy {
        findViewById(R.id.bmiTextView)
    }
    private val resultTextView: TextView by lazy {
        findViewById(R.id.resultTextView)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val height = intent.getDoubleExtra("height", 0.0) / 100
        val weight = intent.getDoubleExtra("weight", 0.0)

        val bmi = weight / (height * height)
        val result: String = when {
            bmi <= 18.5 -> "저체중"
            bmi <= 23.0 -> "정상체중"
            bmi <= 25.0 -> "과체중"
            bmi <= 30.0 -> "경도 비만"
            bmi <= 35.0 -> "중정도 비만"
            else -> "고도 비만"
        }

        bmiTextView.text = "BMI 수치 : " + "%.5f".format(bmi)
        resultTextView.text = "결과 : $result"
    }
}