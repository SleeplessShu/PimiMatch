package com.sleeplessdog.matchthewords.score.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sleeplessdog.matchthewords.R

class ViewHolderScore(view: View): RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.tvName)
    val score: TextView = view.findViewById(R.id.tvScore)
}
