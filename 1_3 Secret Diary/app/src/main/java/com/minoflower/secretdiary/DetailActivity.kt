package com.minoflower.secretdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DetailActivity : AppCompatActivity() {
    private val contentEditText: EditText by lazy {
        findViewById(R.id.contentEditText)
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        getContent()
    }

    private fun getContent() {
        val data = getSharedPreferences("secret", MODE_PRIVATE).getString("content", "")
        contentEditText.setText(data)

        val runnable = Runnable {
            getSharedPreferences("secret", MODE_PRIVATE).edit(true) {
                putString("content", contentEditText.text.toString())
            }
        }

        contentEditText.addTextChangedListener {
            Log.d("Diary", "text Changed : $it")
            handler.postDelayed(runnable, 500)
        }
    }
}