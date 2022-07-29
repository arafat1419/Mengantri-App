package com.arafat1419.mengantri_app.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.databinding.ListServicesBinding
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback

class ServicesAdapter: RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {
    private var listData = ArrayList<ServiceCountDomain>()

    var onItemClicked: ((ServiceCountDomain) -> Unit)? = null

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
                txtTitle.text = data.service?.serviceName
                val timeFormat = itemView.resources.getString(R.string.time_format)
                txtTime.text = String.format(
                    timeFormat,
                    data.service?.serviceOpenTime?.substring(0..4),
                    data.service?.serviceCloseTime?.substring(0..4)
                )
                txtServed.text = data.served.toString()
                txtTotal.text = itemView.resources.getString(
                    R.string.total_format,
                    data.total.toString()
                )
                txtMax.text = itemView.resources.getString(
                    R.string.max_format,
                    data.total.toString(),
                    data.service?.serviceMaxCustomer
                )
                itemView.setOnClickListener {
                    onItemClicked?.invoke(data)
                }
            }
        }
    }
}