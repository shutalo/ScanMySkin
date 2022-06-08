package com.example.scanmyskin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.databinding.DiseaseItemBinding
import com.example.scanmyskin.databinding.DiseaseUrlItemBinding

class DiseaseInfoRecyclerViewAdapter(private val diseaseUrls: List<String>, private val listener: OnUrlClicked) : RecyclerView.Adapter<DiseaseInfoRecyclerViewAdapter.DiseaseInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseInfoViewHolder {
        val binding = DiseaseUrlItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiseaseInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiseaseInfoViewHolder, position: Int) {
        holder.bind(diseaseUrls[position])
    }

    override fun getItemCount(): Int {
        return diseaseUrls.size
    }

    inner class DiseaseInfoViewHolder(itemView: DiseaseUrlItemBinding) : RecyclerView.ViewHolder(itemView.root){
        private val titleTv = itemView.titleTv
        private val cardView = itemView.diseaseUrlCardView
        fun bind(url: String){
            titleTv.text = url
            cardView.setOnClickListener{
                listener.onUrlClicked(url)
            }
        }
    }

    interface OnUrlClicked{
        fun onUrlClicked(url: String)
    }
}
