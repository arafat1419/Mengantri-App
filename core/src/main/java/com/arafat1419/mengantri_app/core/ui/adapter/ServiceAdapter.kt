package com.arafat1419.mengantri_app.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.databinding.ListServicesBinding
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback

class ServiceAdapter(private val callback: AdapterCallback<ServiceDomain>) :
    RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {
    private var listData = ArrayList<ServiceDomain>()
    private var count = ArrayList<Int>()

    fun setData(newListData: List<ServiceDomain>?, servedCount: List<Int>?) {
        if (newListData == null || servedCount == null) return
        listData.clear()
        count.clear()
        listData.addAll(newListData)
        count.addAll(servedCount)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceAdapter.ViewHolder {
        val itemsDataBinding =
            ListServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemsDataBinding)
    }

    override fun onBindViewHolder(holder: ServiceAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        val dataCount = count[position]
        holder.bind(data, dataCount)
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(private val binding: ListServicesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ServiceDomain, count: Int) {
            with(binding) {
                txtServiceTitle.text = data.serviceName
                txtServiceTime.text = itemView.resources.getString(
                    R.string.time_format,
                    data.serviceOpenTime?.substring(0..4),
                    data.serviceCloseTime?.substring(0..4)
                )
                txtServiceServed.text = count.toString()
                itemView.setOnClickListener {
                    callback.onItemClicked(data)
                }
            }
        }
    }
}