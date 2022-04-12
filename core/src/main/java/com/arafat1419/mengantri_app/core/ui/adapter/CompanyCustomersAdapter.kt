package com.arafat1419.mengantri_app.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.databinding.ListCustomersBinding
import com.arafat1419.mengantri_app.core.domain.model.TicketDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.utils.DateHelper
import com.arafat1419.mengantri_app.core.utils.StatusHelper

class CompanyCustomersAdapter(private val callback: AdapterCallback<TicketDomain>) :
    RecyclerView.Adapter<CompanyCustomersAdapter.ViewHolder>() {
    private var listData = ArrayList<TicketDomain>()

    fun setData(newListData: List<TicketDomain>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompanyCustomersAdapter.ViewHolder {
        val itemsDataBinding =
            ListCustomersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemsDataBinding)
    }

    override fun onBindViewHolder(holder: CompanyCustomersAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(private val binding: ListCustomersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TicketDomain) {
            with(binding) {
                txtTicketId.text = itemView.resources.getString(
                    R.string.id_format,
                    data.ticketId.toString()
                )
                txtTicketStatus.text = data.ticketStatus
                txtTicketName.text = data.ticketPersonName
                txtTicketDate.text = data.ticketDate?.let { DateHelper.toUpdateLabel(it) }
                when (data.ticketStatus) {
                    StatusHelper.TICKET_PROGRESS -> cardLTicketStatus.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.primary)
                    )
                    StatusHelper.TICKET_WAITING -> cardLTicketStatus.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.c_yellow)
                    )
                    StatusHelper.TICKET_CANCEL -> cardLTicketStatus.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.c_red)
                    )
                    StatusHelper.TICKET_SUCCESS -> cardLTicketStatus.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.c_green)
                    )
                }
                itemView.setOnClickListener {
                    callback.onItemClicked(data)
                }
            }
        }
    }
}