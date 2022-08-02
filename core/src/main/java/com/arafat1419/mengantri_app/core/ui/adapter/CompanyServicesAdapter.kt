package com.arafat1419.mengantri_app.core.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.databinding.ListServicesCompanyBinding
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import com.arafat1419.mengantri_app.core.domain.model.TicketServiceDomain
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import com.google.android.material.card.MaterialCardView

class CompanyServicesAdapter : RecyclerView.Adapter<CompanyServicesAdapter.ViewHolder>() {
    private var listData = ArrayList<ServiceDomain>()

    var onItemClicked: ((ServiceDomain) -> Unit)? = null
    var onDeleteClicked: ((ServiceDomain?) -> Unit)? = null

    fun setData(newListData: List<ServiceDomain>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompanyServicesAdapter.ViewHolder {
        val itemsDataBinding =
            ListServicesCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemsDataBinding)
    }

    override fun onBindViewHolder(holder: CompanyServicesAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(private val binding: ListServicesCompanyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ServiceDomain) {
            with(binding) {
                txtTitle.text = data.serviceName
                val timeFormat = itemView.resources.getString(R.string.time_format)
                txtTime.text = String.format(
                    timeFormat,
                    data.serviceOpenTime?.substring(0..4),
                    data.serviceCloseTime?.substring(0..4)
                )

                setBackgroundTint(
                    cardProses, when (data.serviceStatus) {
                        StatusHelper.SERVICE_SHOW -> {
                            txtStatus.text = itemView.context.getString(R.string.show)
                            R.color.primary
                        }
                        StatusHelper.SERVICE_HIDE -> {
                            txtStatus.text = itemView.context.getString(R.string.hide)
                            R.color.c_yellow
                        }
                        else -> R.color.black
                    }
                )

                btnDelete.setOnClickListener {
                    onDeleteClicked?.invoke(data)
                }

                itemView.setOnClickListener {
                    onItemClicked?.invoke(data)
                }
            }
        }

        private fun setBackgroundTint(card: MaterialCardView, color: Int) {
            card.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(itemView.context, color)
            )
        }
    }
}