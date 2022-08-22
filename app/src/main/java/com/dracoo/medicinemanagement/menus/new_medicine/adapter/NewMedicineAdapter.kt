package com.dracoo.medicinemanagement.menus.new_medicine.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dracoo.medicinemanagement.databinding.ItemMedicineMasterBinding
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.utils.DiffUtils

class NewMedicineAdapter(private val activity: Activity)
    : RecyclerView.Adapter<NewMedicineAdapter.ViewHolder>(){

    private var listData = ArrayList<MedicineMasterModel>()

    fun initAdapter(list: List<MedicineMasterModel>?) {
        list?.let {
            val diffUtil = DiffUtils(listData, it)
            val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
            listData = it as ArrayList<MedicineMasterModel>
            diffUtilResult.dispatchUpdatesTo(this)
        }
    }

    inner class ViewHolder(private val binding: ItemMedicineMasterBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(data: MedicineMasterModel, activity: Activity) {
                binding.apply {
                    mendicineValueMmTv.text = data.namaobat
                    prizeValueItiTv.text = data.hargasatuan
                    dateLblMmTv.text = data.Timestamp
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemMedicineMasterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position], activity)
    }

    override fun getItemCount(): Int = listData.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

}