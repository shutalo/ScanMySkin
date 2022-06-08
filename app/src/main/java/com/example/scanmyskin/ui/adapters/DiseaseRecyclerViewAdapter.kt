package com.example.scanmyskin.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.databinding.DiseaseItemBinding

class DiseaseRecyclerViewAdapter(private val diseases: List<Disease>) : RecyclerView.Adapter<DiseaseRecyclerViewAdapter.DiseaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val binding = DiseaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiseaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        holder.bind(diseases[position].title, diseases[position].description)
    }

    override fun getItemCount(): Int {
        return diseases.size
    }

    class DiseaseViewHolder(itemView: DiseaseItemBinding) : RecyclerView.ViewHolder(itemView.root){
        private val titleTv = itemView.title
        private val descriptionTv = itemView.description
        fun bind(title: String, description: String){
            titleTv.text = title
            descriptionTv.text = description
        }
    }
}


