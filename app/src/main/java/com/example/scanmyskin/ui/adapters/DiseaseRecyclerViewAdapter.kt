package com.example.scanmyskin.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.databinding.DiseaseItemBinding
import com.example.scanmyskin.helpers.shortDelay
import com.example.scanmyskin.helpers.veryShortDelay

class DiseaseRecyclerViewAdapter(private val diseases: List<Disease>, private val listener: OnDiseaseClicked) : RecyclerView.Adapter<DiseaseRecyclerViewAdapter.DiseaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val binding = DiseaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiseaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        holder.bind(diseases[position], listener)
    }

    override fun getItemCount(): Int {
        return diseases.size
    }

    class DiseaseViewHolder(private val binding: DiseaseItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(disease: Disease, listener: OnDiseaseClicked){
            binding.apply {
                title.text = disease.title
                description.text = disease.description
                diseaseCardView.setOnClickListener{
                    YoYo.with(Techniques.Pulse).duration(veryShortDelay).onEnd{
                        listener.onDiseaseClicked(disease)
                    }.playOn(root)
                }
            }
        }
    }

    interface OnDiseaseClicked{
        fun onDiseaseClicked(disease: Disease)
    }
}


