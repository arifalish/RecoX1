package com.example.recox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val items: List<Pair<String,String>>) : RecyclerView.Adapter<HistoryAdapter.VH>() {
    class VH(view: View): RecyclerView.ViewHolder(view) {
        val tvTimestamp: TextView = view.findViewById(R.id.tvTimestamp)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
        return VH(v)
    }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val (ts, st) = items[position]
        holder.tvTimestamp.text = ts
        holder.tvStatus.text = st
    }
}
