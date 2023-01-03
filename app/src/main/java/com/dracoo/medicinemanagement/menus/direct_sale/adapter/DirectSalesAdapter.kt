package com.dracoo.medicinemanagement.menus.direct_sale.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ItemMedicineMasterBinding
import com.dracoo.medicinemanagement.model.DirectSaleModel
import com.dracoo.medicinemanagement.utils.DiffUtils
import com.dracoo.medicinemanagement.utils.MedicalUtil


class DirectSalesAdapter (private val context: Context)
    : RecyclerView.Adapter<DirectSalesAdapter.ViewHolder>(){
    private var listData = ArrayList<DirectSaleModel>()
    private var listCv = ArrayList<CardView>()
    private var listImage = ArrayList<ImageView>()

    fun initAdapter(list: List<DirectSaleModel>?) {
        list?.let {
            val diffUtil = DiffUtils(listData, it)
            val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
            listData.clear()
            listData.addAll(list)
            diffUtilResult.dispatchUpdatesTo(this)
        }
    }

    fun getData() = listData

    fun removeItem(position: Int) {
        listData.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(private val binding: ItemMedicineMasterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: DirectSaleModel, position: Int) {
            binding.apply {
                if (!listCv.contains(binding.subImmCl)) {
                    listCv.add(binding.subImmCl)
                }
                if (!listImage.contains(binding.rightImmIv)) {
                    listImage.add(binding.rightImmIv)
                }
                subImmCl.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.gray_4
                    ))
                rightImmIv.visibility = View.GONE
                if(position == 0){
                    rightImmIv.visibility = View.VISIBLE
                    subImmCl.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.green_4
                        ))
                }

                mendicineValueMmTv.text = data.namaObat
                when(val medicinePrize = data.hargaSatuan){
                    "0" -> prizeValueItiTv.text = medicinePrize
                    else -> prizeValueItiTv.text = "Rp. " + MedicalUtil.moneyFormat(medicinePrize.toDouble())
                }
                dateLblMmTv.text = data.jumlah
                subImmCl.setOnClickListener {
                    for (cl in listCv) { cl.setCardBackgroundColor(ContextCompat.getColor(
                        context,
                        R.color.gray_4
                    )) }
                    for (iv in listImage) { iv.visibility = View.GONE }
                    for (iv in listImage) { iv.visibility = View.GONE }
                    subImmCl.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.green_4
                        ))
                    rightImmIv.visibility = View.VISIBLE
                }
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
        holder.bind(listData[position], position)
    }

    override fun getItemCount() = listData.size

    override fun getItemViewType(position: Int) = position

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }
}