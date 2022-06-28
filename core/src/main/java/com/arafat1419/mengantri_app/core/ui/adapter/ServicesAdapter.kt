package com.arafat1419.mengantri_app.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.databinding.ListServicesBinding
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback

class ServicesAdapter(private val callback: AdapterCallback<ServiceCountDomain>) :
    RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {
    private var listData = ArrayList<ServiceCountDomain>()

    fun setData(newListData: List<ServiceCountDomain>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServicesAdapter.ViewHolder {
        val itemsDataBinding =
            ListServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemsDataBinding)
    }

    override fun onBindViewHolder(holder: ServicesAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(private val binding: ListServicesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ServiceCountDomain) {
            with(binding) {
                txtServiceTitle.text = data.services.serviceName
                val timeFormat = itemView.resources.getString(R.string.time_format)
                txtServiceTime.text = String.format(
                    timeFormat,
                    data.services.serviceOpenTime?.substring(0..4),
                    data.services.serviceCloseTime?.substring(0..4)
                )
                txtServiceServed.text = data.served.toString()
                txtServiceTotal.text = itemView.resources.getString(
                    R.string.total_format,
                    data.total.toString()
                )
                itemView.setOnClickListener {
                    callback.onItemClicked(data)
                }
            }
        }
    }
}