package com.arafat1419.mengantri_app.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.databinding.ListTicketsBinding
import com.arafat1419.mengantri_app.core.domain.model.TicketWithServiceDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.utils.DateHelper

class TicketsAdapter(private val callback: AdapterCallback<TicketWithServiceDomain>) :
    RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {
    private var listData = ArrayList<TicketWithServiceDomain>()

    fun setData(newListData: List<TicketWithServiceDomain>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TicketsAdapter.ViewHolder {
        val itemsDataBinding =
            ListTicketsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemsDataBinding)
    }

    override fun onBindViewHolder(holder: TicketsAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(private val binding: ListTicketsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TicketWithServiceDomain) {
            with(binding) {
                txtLTicketId.text = itemView.resources.getString(
                    R.string.id_format,
                    data.ticketId.toString()
                )
                txtLTicketTitle.text = data.serviceId?.serviceName
                val timeFormat = itemView.resources.getString(R.string.time_format)
                txtLTicketTime.text = String.format(
                    timeFormat,
                    data.serviceId?.serviceOpenTime?.substring(0..4),
                    data.serviceId?.serviceCloseTime?.substring(0..4)
                )
                txtLTicketDate.text = data.ticketDate?.let { DateHelper.toUpdateLabel(it) }
                txtLTicketStatus.text = data.ticketStatus
                itemView.setOnClickListener {
                    callback.onItemClicked(data)
                }
            }
        }
    }
}