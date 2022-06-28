package com.arafat1419.mengantri_app.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.databinding.ListServicesCompanyBinding
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.utils.StatusHelper

class CompanyServicesAdapter(private val callback: AdapterCallback<ServiceDomain>) :
    RecyclerView.Adapter<CompanyServicesAdapter.ViewHolder>() {
    private var listData = ArrayList<ServiceDomain>()

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
                txtServiceTitle.text = data.serviceName
                val timeFormat = itemView.resources.getString(R.string.time_format)
                txtServiceTime.text = String.format(
                    timeFormat,
                    data.serviceOpenTime?.substring(0..4),
                    data.serviceCloseTime?.substring(0..4)
                )
                txtLServiceStatus.text =
                    if (data.serviceStatus == 1) itemView.context.getString(R.string.show) else itemView.context.getString(
                        R.string.hide
                    )
                when (data.serviceStatus) {
                    StatusHelper.SERVICE_SHOW -> cardLServiceStatus.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.c_green)
                    )
                    StatusHelper.SERVICE_HIDE -> cardLServiceStatus.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.c_red)
                    )
                }
                itemView.setOnClickListener {
                    callback.onItemClicked(data)
                }
            }
        }
    }
}