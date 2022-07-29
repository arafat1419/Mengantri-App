package com.arafat1419.mengantri_app.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.databinding.ListTicketsBinding
import com.arafat1419.mengantri_app.core.domain.model.TicketServiceDomain
import com.arafat1419.mengantri_app.core.utils.DateHelper
import com.arafat1419.mengantri_app.core.utils.StatusHelper

class TicketsAdapter : RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {
    private var listData = ArrayList<TicketServiceDomain>()

    var onItemClicked: ((TicketServiceDomain) -> Unit)? = null

    fun setData(newListData: List<TicketServiceDomain>?) {
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
        fun bind(data: TicketServiceDomain) {
            with(binding) {
                txtId.text = itemView.resources.getString(
                    R.string.id_format,
                    data.ticketId.toString()
                )
                txtTitle.text = data.serviceId?.serviceName
                txtEstimatedTime.text = data.ticketEstimatedTime
                txtDate.text = data.ticketDate?.let { DateHelper.toUpdateLabel(it) }
                txtStatus.text = data.ticketStatus

                when (data.ticketStatus) {
                    StatusHelper.TICKET_PROGRESS -> cardProses.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.primary)
                    )
                    StatusHelper.TICKET_WAITING -> cardProses.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.c_yellow)
                    )
                    StatusHelper.TICKET_CANCEL -> cardProses.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.c_red)
                    )
                    StatusHelper.TICKET_SUCCESS -> cardProses.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.c_green)
                    )
                }

                itemView.setOnClickListener {
                    onItemClicked?.invoke(data)
                }
            }
        }
    }
}