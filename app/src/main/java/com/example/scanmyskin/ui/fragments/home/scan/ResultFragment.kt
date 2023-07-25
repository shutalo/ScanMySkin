package com.example.scanmyskin.ui.fragments.home.scan

import android.net.Uri
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.scanmyskin.R
import com.example.scanmyskin.databinding.FragmentResultBinding
import com.example.scanmyskin.helpers.formatChance
import com.example.scanmyskin.helpers.formatStringDisease
import com.example.scanmyskin.helpers.veryShortDelay
import com.example.scanmyskin.ui.fragments.base.BaseFragment
import com.example.scanmyskin.ui.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Locale


class ResultFragment : BaseFragment<FragmentResultBinding>() {

    private val viewModel by sharedViewModel<HomeViewModel>()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentResultBinding
        get() = FragmentResultBinding::inflate

    override fun setupUi() {
        val args = ResultFragmentArgs.fromBundle(requireArguments())
        val disease = args.disease
        val chance = args.chance
        val uri = args.uri

        Log.d("Navigation", "Disease: $disease, Chance: $chance")

        uri?.let {
            updateImage(it)
        } ?: updateImage(viewModel.imageUri)

        if (chance < 0.2) {
            binding.advice.text = getString(R.string.consult_doctor)
            binding.disease.text = getString(R.string.model_determination)
            binding.moreInfo.visibility = View.GONE
        } else {
            binding.advice.text = formatAdvice(disease.advice)
            binding.warning.text = disease.warning
            val spannedString: Spanned = HtmlCompat.fromHtml(
                getString(
                    R.string.there_is_chance,
                    formatChance(chance),
                    formatStringDisease(disease.title.lowercase(Locale.ROOT))
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.disease.text = spannedString
        }

        binding.moreInfo.setOnClickListener {
            YoYo.with(Techniques.Pulse).duration(veryShortDelay).onEnd {
                findNavController().navigate(
                    ResultFragmentDirections.actionResultFragmentToInfoFragment(
                        disease.title
                    )
                )
            }.playOn(it)
        }

    }

    private fun formatAdvice(advice: String): String {
        return advice.replace("\\n", "\n")
    }

    private fun updateImage(uri: Uri?) {
        uri?.let {
            Glide.with(binding.root)
                .load(it)
                .into(binding.imageContainer)
        }
    }

    private fun updateImage(uri: String) {
        Glide.with(binding.root)
            .load(uri)
            .into(binding.imageContainer)
    }
}