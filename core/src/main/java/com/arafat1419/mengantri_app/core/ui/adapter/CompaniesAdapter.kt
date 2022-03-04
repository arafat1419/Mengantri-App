package com.arafat1419.mengantri_app.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.databinding.ListCompaniesBinding
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.bumptech.glide.Glide
import java.util.*

class CompaniesAdapter(private val callback: AdapterCallback<CompanyDomain>) :
    RecyclerView.Adapter<CompaniesAdapter.ViewHolder>() {
    private var listData = ArrayList<CompanyDomain>()

    fun setData(newListData: List<CompanyDomain>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompaniesAdapter.ViewHolder {
        val itemsDataBinding =
            ListCompaniesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemsDataBinding)
    }

    override fun onBindViewHolder(holder: CompaniesAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(private val binding: ListCompaniesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CompanyDomain) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.companyBanner)
                    .into(imgCompaniesLogo)

                txtCompaniesTitle.text = data.companyName
                txtCompaniesLocation.text = data.companyDistrics
                txtCompaniesTime.text = itemView.resources.getString(
                    R.string.time_format,
                    data.companyOpenTime?.substring(0..4),
                    data.companyCloseTime?.substring(0..4)
                )
                itemView.setOnClickListener {
                    callback.onItemClicked(data)
                }
            }
        }
    }
}