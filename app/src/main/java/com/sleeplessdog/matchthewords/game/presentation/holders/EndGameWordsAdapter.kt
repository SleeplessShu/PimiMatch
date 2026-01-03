package com.sleeplessdog.matchthewords.game.presentation.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sleeplessdog.matchthewords.databinding.EndGameRwCheckboxElementBinding
import com.sleeplessdog.matchthewords.game.presentation.models.Word

data class SelectableWordPair(
    val pair: Pair<Word, Word>,
    val isSelected: Boolean = false,
)

class EndGameWordsAdapter(
    private val onSelectionChanged: (selectedPairs: List<Pair<Word, Word>>) -> Unit,
) : RecyclerView.Adapter<EndGameWordsAdapter.WordPairViewHolder>() {


    private var items: MutableList<SelectableWordPair> = mutableListOf()

    inner class WordPairViewHolder(val binding: EndGameRwCheckboxElementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    items[bindingAdapterPosition] =
                        switchSelectedState(items[bindingAdapterPosition])
                    notifyItemChanged(bindingAdapterPosition)
                    reportSelectionChange()
                }
            }
        }

        fun bind(selectablePair: SelectableWordPair) {
            binding.tvOrigin.text = selectablePair.pair.first.text
            binding.tvTranslate.text = selectablePair.pair.second.text
            binding.checkbox.isChecked = selectablePair.isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordPairViewHolder {
        val binding = EndGameRwCheckboxElementBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WordPairViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordPairViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(pairs: List<Pair<Word, Word>>) {
        val oldSize = items.size
        items.clear()
        notifyItemRangeRemoved(0, oldSize)
        items.addAll(pairs.map { SelectableWordPair(it) })
        notifyItemRangeInserted(0, items.size)
        reportSelectionChange()
    }

    fun toggleSelectAll(newState: Boolean) {
        val updatedItems = items.map { it.copy(isSelected = newState) }
        items = updatedItems.toMutableList()
        notifyItemRangeChanged(0, items.size)
        reportSelectionChange()
    }

    private fun reportSelectionChange() {
        val selected = items.filter { it.isSelected }.map { it.pair }
        onSelectionChanged(selected)
    }

    private fun switchSelectedState(currentItem: SelectableWordPair): SelectableWordPair {
        return currentItem.copy(isSelected = !currentItem.isSelected)
    }
}