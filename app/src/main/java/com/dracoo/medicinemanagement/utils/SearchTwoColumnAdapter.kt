package com.dracoo.medicinemanagement.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ItemSearchTwoColumnBinding
import com.dracoo.medicinemanagement.model.ThreeColumnModel

class SearchTwoColumnAdapter(
    private val listTwoColumn: ArrayList<ThreeColumnModel>,
    private val twoColumnInterface : MedicalUtil.TwoColumnInterface
): RecyclerView.Adapter<SearchTwoColumnAdapter.ViewHolder>() {
    private lateinit var itemSearchTwoColumnBinding: ItemSearchTwoColumnBinding
    private var listCL = ArrayList<ConstraintLayout>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_two_column, parent, false)
        itemSearchTwoColumnBinding = ItemSearchTwoColumnBinding.bind(layoutInflater)
        return ViewHolder(itemSearchTwoColumnBinding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!listCL.contains(itemSearchTwoColumnBinding.root)) {
            listCL.add(itemSearchTwoColumnBinding.root)
        }

        itemSearchTwoColumnBinding.apply {
            when{
                position % 2 == 0 -> root.setBackgroundResource(R.color.white)
                else -> root.setBackgroundResource(R.color.gray_3)
            }

            root.setOnClickListener {
                var pos = 1
                for (cl in listCL) {
                    if (pos % 2 == 0) {
                        cl.setBackgroundResource(R.color.white)
                    } else {
                        cl.setBackgroundResource(R.color.gray_3)
                    }
                    pos += 1
                }
                root.setBackgroundResource(R.color.purple_200)
                twoColumnInterface.selectedTwoSearch(
                        ThreeColumnModel(
                            listTwoColumn[position].column1,
                            listTwoColumn[position].column2,
                            listTwoColumn[position].column3,
                            listTwoColumn[position].column4
                        )
                    )
            }

            col1IsTwocTv.text = listTwoColumn[position].column1
            colTwoIsTwocTv.text = listTwoColumn[position].column2
        }
    }

    override fun getItemCount(): Int = listTwoColumn.size

    override fun getItemViewType(position: Int): Int = position

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}