package com.example.scanmyskin.ui.adapters

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.scanmyskin.R

class AdapterItemDecoration(
    private val context: Context
) : RecyclerView.ItemDecoration() {

    private val spaceBetweenItems by lazy { context.resources.getDimensionPixelSize(R.dimen.margin_zero) }
    private val horizontalMargin by lazy { context.resources.getDimensionPixelSize(R.dimen.margin_small_to_medium) }
    private val topMargin by lazy { context.resources.getDimensionPixelSize(R.dimen.margin_very_small) }
    private val bottomMargin by lazy { context.resources.getDimensionPixelSize(R.dimen.margin_very_small) }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        if (position == RecyclerView.NO_POSITION) {
            return
        }

        outRect.top =
            if (position == 0) {
                topMargin
            } else {
                spaceBetweenItems / 2
            }

        outRect.bottom =
            if (position == state.itemCount - 1) {
                bottomMargin
            } else {
                spaceBetweenItems / 2
            }

        outRect.left =
            horizontalMargin / 2
        outRect.right =
            horizontalMargin / 2
    }
}
