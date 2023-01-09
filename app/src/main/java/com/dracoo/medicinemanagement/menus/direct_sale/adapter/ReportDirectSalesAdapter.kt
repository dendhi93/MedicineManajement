package com.dracoo.medicinemanagement.menus.direct_sale.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ItemResultDirectSaleBinding
import com.dracoo.medicinemanagement.model.DirectSaleModel
import com.dracoo.medicinemanagement.utils.DiffUtils

class ReportDirectSalesAdapter(private val context: Context)
    : RecyclerView.Adapter<ReportDirectSalesAdapter.ViewHolder>(){
    private var listData = ArrayList<DirectSaleModel>()

    fun initDataAdapter(list: List<DirectSaleModel>?) {
        list?.let {
            val diffUtil = DiffUtils(listData, it)
            val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
            listData.clear()
            listData.addAll(list)
            diffUtilResult.dispatchUpdatesTo(this)
        }
    }

    inner class ViewHolder(private val binding: ItemResultDirectSaleBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(data: DirectSaleModel, position: Int) {
                binding.apply {
                    saleDateIrdsTv.text = data.createDate
                    billNoIrdsTv.text = data.noTagihan
                    nameIrdsTv.text = data.namaObat
                    qtyIrdsTv.text = data.jumlah

                    when{
                        position % 2 == 0-> root.setBackgroundResource(R.color.white)
                        else -> root.setBackgroundResource(R.color.gray_3)
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemResultDirectSaleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position], position)
    }

    override fun getItemCount() = listData.size

    override fun getItemViewType(position: Int) = position

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

}