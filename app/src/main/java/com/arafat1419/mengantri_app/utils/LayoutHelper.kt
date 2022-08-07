package com.arafat1419.mengantri_app.utils

import android.view.View
import com.arafat1419.mengantri_app.databinding.BaseEmptyBinding
import com.arafat1419.mengantri_app.databinding.BaseLoadingBinding

object LayoutHelper {

    fun isLoading(binding: BaseLoadingBinding, state: Boolean) {
        binding.apply {
            if (state) {
                root.visibility = View.VISIBLE
            } else {
                root.visibility = View.GONE
            }
        }
    }

    fun isEmpty(
        binding: BaseEmptyBinding,
        state: Boolean,
        buttonState: Boolean,
        actionText: String = "",
        onAction: (() -> Unit)? = null
    ) {
        binding.apply {
            if (state) {
                root.visibility = View.VISIBLE
                btnAction.apply {
                    visibility = if(buttonState) View.VISIBLE else View.GONE
                    text = actionText
                    setOnClickListener {
                        onAction?.invoke()
                    }
                }
            } else {
                root.visibility = View.GONE
            }
        }
    }

}
