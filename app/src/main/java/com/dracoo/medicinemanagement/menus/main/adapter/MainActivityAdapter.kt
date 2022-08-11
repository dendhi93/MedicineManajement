package com.dracoo.medicinemanagement.menus.main.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.ItemGridHomeBinding
import com.dracoo.medicinemanagement.menus.new_medicine.NewMedicineActivity
import com.dracoo.medicinemanagement.model.MenuModel
import com.dracoo.medicinemanagement.utils.ConstantsObject
import timber.log.Timber

class MainActivityAdapter(private val listMenu: ArrayList<MenuModel>,
                          private val activity: Activity,
                          private val mCallBackSelectedMenu: CallBackExitApps
): RecyclerView.Adapter<MainActivityAdapter.ViewHolder>() {
    private lateinit var homeAdapterBinding: ItemGridHomeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_home, parent, false)
        homeAdapterBinding = ItemGridHomeBinding.bind(layoutInflater)

        return ViewHolder(homeAdapterBinding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        homeAdapterBinding.apply {
            titleItemMenuTv.text = listMenu[position].title
            subtitleItemMenuHomeTv.text = listMenu[position].subtitle
            iconItemMenuHomeIv.setBackgroundResource(listMenu[position].icon)
            intersectItemMenuHomeIv.setBackgroundResource(listMenu[position].intersectImage)
            itemMenuHomeCv.backgroundTintList =
                ContextCompat.getColorStateList(activity, listMenu[position].color)
            itemMenuHomeCv.setOnClickListener {
                when (listMenu[position].title) {
                    ConstantsObject.vExitApps -> mCallBackSelectedMenu.onExitApps()
                    ConstantsObject.vInputMedicine -> {
                        activity.startActivity(Intent(activity, NewMedicineActivity::class.java))
                        activity.finish()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = listMenu.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}

interface CallBackExitApps{
    fun onExitApps()
}