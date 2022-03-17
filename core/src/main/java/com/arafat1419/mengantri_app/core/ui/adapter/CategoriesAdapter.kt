package com.arafat1419.mengantri_app.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arafat1419.mengantri_app.core.databinding.ListCategoriesBinding
import com.arafat1419.mengantri_app.core.domain.model.CategoryDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.utils.DataMapper
import com.bumptech.glide.Glide
import java.util.ArrayList

class CategoriesAdapter(private val callback: AdapterCallback<CategoryDomain>) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    private var listData = ArrayList<CategoryDomain>()

    fun setData(newListData: List<CategoryDomain>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.ViewHolder {
        val itemsDataBinding =
            ListCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemsDataBinding)
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(private val binding: ListCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CategoryDomain) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(DataMapper.imageDirectus + data.categoryImage)
                    .into(imgCategoriesIcon)

                txtCategoriesTitle.text = data.categoryName
                itemView.setOnClickListener {
                    callback.onItemClicked(data)
                }
            }
        }
    }
}