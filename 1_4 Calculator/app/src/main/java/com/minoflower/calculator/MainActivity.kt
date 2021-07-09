package com.minoflower.calculator

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import model.History
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val calTextView: TextView by lazy {
        findViewById(R.id.calTextView)
    }

    private val resultTextView: TextView by lazy {
        findViewById(R.id.resultTextView)
    }

    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.recyclerView)
    }

    private val historyLayout: View by lazy {
        findViewById(R.id.historyLayout)
    }

    private lateinit var db: AppDatabase

    private val itemList = ArrayList<History>()
    private val adapter = HistoryAdapter(itemList)
    private var isOperator = false
    private var isBracket = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun buttonClicked(view: View) {
        when (view.id) {
            R.id.zeroButton -> numberButtonClicked("0")
            R.id.oneButton -> numberButtonClicked("1")
            R.id.twoButton -> numberButtonClicked("2")
            R.id.threeButton -> numberButtonClicked("3")
            R.id.fourButton -> numberButtonClicked("4")
            R.id.fiveButton -> numberButtonClicked("5")
            R.id.sixButton -> numberButtonClicked("6")
            R.id.sevenButton -> numberButtonClicked("7")
            R.id.eightButton -> numberButtonClicked("8")
            R.id.nineButton -> numberButtonClicked("9")
            R.id.plusButton -> operatorButtonClicked("+")
            R.id.minusButton -> operatorButtonClicked("-")
            R.id.divideButton -> operatorButtonClicked("/")
            R.id.modButton -> operatorButtonClicked("%")
            R.id.multiButton -> operatorButtonClicked("x")
            R.id.bracketButton -> {
                isBracket = if (!isBracket) {
                    operatorButtonClicked("(")
                    true
                } else {
                    operatorButtonClicked(")")
                    false
                }
            }
        }
    }

    fun historyButtonClicked(view: View) {
        historyLayout.isVisible = true
        itemList.clear()
        Thread {
            db.historyDao().getAll().reversed().forEach {
                itemList.add(it)
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
        }.start()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun resultButtonClicked(view: View) {
        val expression = calTextView.text.split(" ").filterNot { it == "" }
        if (expression.isEmpty()) {
            return
        }

        if (expression.last() in "+/%*-") {
            Toast.makeText(this, "연산자 맨끝 x", Toast.LENGTH_SHORT).show()
            return
        }

        val expressionText = calTextView.text.toString()
        val resultText = completeResultValue(createSuffix())

        if (resultText == -1)
            return

        resultTextView.text = resultText.toString()

        Thread {
            db.historyDao().insert(History(null, expressionText, resultText.toString()))
        }.start()

        isOperator = false
        isBracket = false
    }

    fun resetButtonClicked(view: View) {
        calTextView.text = ""
        resultTextView.text = ""
        isOperator = false
        isBracket = false
    }

    private fun createSuffix(): ArrayList<String> {
        val stack = Stack<String>()
        val result = ArrayList<String>()
        var bracketCnt = 0

        calTextView.text.split(" ").filterNot { it == "" }.forEach {
            if (it.isDigitsOnly()) {
                result.add(it)
            } else {
                when (it) {
                    "(" -> bracketCnt++
                    ")" -> bracketCnt--
                }

                if (stack.isEmpty()) {
                    stack.push(it)
                } else {
                    if (it > stack.peek())
                        stack.push(it)
                    else {
                        if (it == ")") {
                            while (stack.peek() != "(")
                                result.add(stack.pop())
                            stack.pop()
                        } else
                            result.add(stack.pop())
                    }
                }
            }
        }
        while (stack.isNotEmpty()) {
            result.add(stack.pop())
        }

        return if (bracketCnt != 0)
            ArrayList()
        else
            result
    }

    private fun completeResultValue(list: ArrayList<String>): Int {
        if (list.isEmpty())
            return -1

        val stack = Stack<String>()
        stack.push(list[0])

        for (i in 1 until list.size) {
            if (list[i].isDigitsOnly()) {
                stack.push(list[i])
            } else {
                val op1 = stack.pop().toInt()
                val op2 = stack.pop().toInt()
                var value = 0
                when (list[i]) {
                    "+" -> value = op2 + op1
                    "-" -> value = op2 - op1
                    "x" -> value = op2 * op1
                    "%" -> value += op2 % op1
                    "/" -> value += op2 / op1
                }
                stack.push(value.toString())
            }
        }
        return stack.pop().toInt()
    }

    @SuppressLint("SetTextI18n")
    private fun numberButtonClicked(number: String) {
        calTextView.append(number)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private fun operatorButtonClicked(op: String) {
        if (calTextView.text.isEmpty() && op in "+/%*-") {
            return
        }

        calTextView.append(" $op ")

        val ssb = SpannableStringBuilder(calTextView.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            calTextView.text.length - 2,
            calTextView.text.length - 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        calTextView.text = ssb

        isOperator = true
    }

    fun closeHistoryButtonClicked(view: View) {
        historyLayout.isVisible = false
    }

    fun deleteHistoryButtonClicked(view: View) {
        Thread {
            db.historyDao().deleteAll()
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }.start()

        itemList.clear()
    }
}