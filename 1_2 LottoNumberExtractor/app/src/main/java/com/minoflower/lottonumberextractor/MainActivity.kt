package com.minoflower.lottonumberextractor

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private val addButton: AppCompatButton by lazy {
        findViewById(R.id.addNumberButton)
    }

    private val resetButton: AppCompatButton by lazy {
        findViewById(R.id.resetButton)
    }

    private val autoCreateButton: AppCompatButton by lazy {
        findViewById(R.id.autoCreateButton)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.numberPicker)
    }

    private val numberList: List<TextView> by lazy {
        listOf(
            findViewById(R.id.numberOne),
            findViewById(R.id.numberTwo),
            findViewById(R.id.numberThree),
            findViewById(R.id.numberFour),
            findViewById(R.id.numberFive),
            findViewById(R.id.numberSix)
        )
    }

    private val numberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initNumberPicker()
        initAddNumberButton()
        initAutoCreateButton()
        initResetButton()
    }

    private fun initNumberPicker() {
        numberPicker.apply {
            minValue = 1
            maxValue = 45
        }
    }

    private fun initAddNumberButton() {
        addButton.setOnClickListener {
            val data = numberPicker.value

            if (numberSet.size >= 6) {
                Toast.makeText(this, "최대 6개까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (numberSet.contains(data)) {
                Toast.makeText(this, "이미 선택한 숫자입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            numberList[numberSet.size].apply {
                isVisible = true
                text = data.toString()
                setBallBackground(this, data.toString())
            }
            numberSet.add(data)
        }
    }

    private fun initAutoCreateButton() {
        autoCreateButton.setOnClickListener {
            val list = getRandomNumber()
            list.forEachIndexed { index, i ->
                numberList[index].apply {
                    text = i.toString()
                    isVisible = true
                    setBallBackground(this, i.toString())
                }
                numberSet.add(i)
            }
        }
    }

    private fun getRandomNumber(): List<Int> {
        val list = mutableListOf<Int>().apply {
            for (i in 1..45) {
                if (numberSet.contains(i))
                    continue
                this.add(i)
            }
        }
        list.shuffle()
        if (numberSet.size == 6) {
            resetNumber()
        }
        val newList = numberSet.toList() + list.subList(0, 6 - numberSet.size)

        return newList.sorted()
    }

    private fun initResetButton() {
        resetButton.setOnClickListener {
            resetNumber()
        }
    }

    private fun resetNumber() {
        numberList.forEach {
            it.visibility = View.GONE
        }
        numberSet.clear()
    }

    private fun setBallBackground(textView: TextView, number: String) {
        when (number.toInt()) {
            in 1..10 -> textView.setBackgroundResource(R.drawable.circle_yellow)
            in 11..20 -> textView.setBackgroundResource(R.drawable.circle_blue)
            in 21..30 -> textView.setBackgroundResource(R.drawable.circle_red)
            in 31..40 -> textView.setBackgroundResource(R.drawable.circle_gray)
            else -> textView.setBackgroundResource(R.drawable.circle_green)
        }
    }
}