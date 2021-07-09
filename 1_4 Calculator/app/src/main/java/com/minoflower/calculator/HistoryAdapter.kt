package com.minoflower.calculator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import model.History

class HistoryAdapter(private val list: ArrayList<History>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    inner class HistoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val expression: TextView = v.findViewById(R.id.expressionTextView)
        private val result: TextView = v.findViewById(R.id.resultTextView)

        @SuppressLint("SetTextI18n")
        fun bind(history: History) {
            expression.text = history.expression
            result.text = "= ${history.result}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.history_row, parent, false)
        )

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}