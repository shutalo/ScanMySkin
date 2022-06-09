package com.example.scanmyskin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.data.models.Disease
import com.example.scanmyskin.databinding.DiseaseItemBinding
import com.example.scanmyskin.databinding.DiseaseUrlItemBinding
import com.example.scanmyskin.helpers.veryShortDelay

class DiseaseInfoRecyclerViewAdapter(private val diseaseUrls: HashMap<String, String>, private val listener: OnUrlClicked) : RecyclerView.Adapter<DiseaseInfoRecyclerViewAdapter.DiseaseInfoViewHolder>() {

    private var keys: ArrayList<String> = ArrayList()

    init {
        diseaseUrls.keys.forEach{
            keys.add(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseInfoViewHolder {
        val binding = DiseaseUrlItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiseaseInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiseaseInfoViewHolder, position: Int) {
        holder.bind(diseaseUrls[keys[position]]!!, listener)
    }

    override fun getItemCount(): Int {
        return diseaseUrls.size
    }

    inner class DiseaseInfoViewHolder(private val binding: DiseaseUrlItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(url: String, listener: OnUrlClicked){
            binding.apply {
                titleTv.text = url
                root.setOnClickListener{
                    YoYo.with(Techniques.FadeOut).playOn(it)
                }
                diseaseUrlCardView.setOnClickListener{
                    YoYo.with(Techniques.Pulse).duration(veryShortDelay).onEnd{
                        listener.onUrlClicked(url)
                    }.playOn(root)
                }
            }
        }
    }

    interface OnUrlClicked{
        fun onUrlClicked(url: String)
    }
}
