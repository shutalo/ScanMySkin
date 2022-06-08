package com.example.scanmyskin.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.databinding.DiseaseItemBinding

class DiseaseRecyclerViewAdapter(private val diseases: List<Disease>, private val listener: OnDiseaseClicked) : RecyclerView.Adapter<DiseaseRecyclerViewAdapter.DiseaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val binding = DiseaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiseaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        holder.bind(diseases[position])
    }

    override fun getItemCount(): Int {
        return diseases.size
    }

    inner class DiseaseViewHolder(itemView: DiseaseItemBinding) : RecyclerView.ViewHolder(itemView.root){
        private val titleTv = itemView.title
        private val descriptionTv = itemView.description
        private val cardView = itemView.diseaseCardView
        fun bind(disease: Disease){
            titleTv.text = disease.title
            descriptionTv.text = disease.description
            cardView.setOnClickListener{
                listener.onDiseaseClicked(disease)
            }
        }
    }

    interface OnDiseaseClicked{
        fun onDiseaseClicked(disease: Disease)
    }
}


