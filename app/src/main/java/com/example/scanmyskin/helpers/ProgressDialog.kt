package com.example.scanmyskin.helpers

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.scanmyskin.R
import com.example.scanmyskin.ScanMySkin
import com.example.scanmyskin.databinding.ProgressDialogBinding

class ProgressDialog(private val activity: Activity) {

    private lateinit var dialog: Dialog

    fun show(): Dialog {
        val inflater = activity.layoutInflater
        val view = ProgressDialogBinding.inflate(inflater)

        val gradientDrawable = GradientDrawable()
        gradientDrawable.cornerRadius = 40f
        gradientDrawable.setColor(ContextCompat.getColor(activity, R.color.transparent))

        dialog = Dialog(activity)
        dialog.window?.setBackgroundDrawable(gradientDrawable)
        dialog.setCancelable(false)
        dialog.setContentView(view.root)
        dialog.show()
        return dialog
    }

    fun dismiss() {
        dialog.dismiss()
    }
}