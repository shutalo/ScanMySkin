package com.example.scanmyskin.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.data.models.HistoryItem
import com.example.scanmyskin.databinding.HistoryItemBinding
import com.example.scanmyskin.helpers.camelCaseString
import com.example.scanmyskin.helpers.formatChance

class HistoryRecyclerViewAdapter(
    private val items: List<HistoryItem>,
    private val listener: OnHistoryItemClickListener
) : RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class HistoryViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItem) {
            binding.apply {
                root.setOnClickListener {
                    listener.onHistoryItemClicked(
                        item.chance.toFloat(),
                        formatDiseaseLabel(item.disease),
                        item.imageUri
                    )
                }
                if (item.chance.toFloat() < 0.2) {
                    disease.text =
                        ScanMySkin.context.resources.getString(R.string.unknown_disease)
                    chance.visibility = View.GONE
                } else {
                    disease.text = camelCaseString(item.disease)
                    chance.visibility = View.VISIBLE
                    chance.text = formatChance(item.chance.toFloat()).plus("%")
                }
                Glide.with(binding.root)
                    .load(item.imageUri)
                    .into(imageContainer)
            }
        }
    }

    interface OnHistoryItemClickListener {
        fun onHistoryItemClicked(chance: Float, disease: String, uri: String)
    }

    private fun formatDiseaseLabel(label: String): String {
        return label.replace("_", " ")
    }
}