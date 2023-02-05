package com.dracoo.medicinemanagement.utils

import androidx.constraintlayout.widget.ConstraintLayout
import com.dracoo.medicinemanagement.databinding.ItemSearchTwoColumnBinding
import com.dracoo.medicinemanagement.model.ThreeColumnModel

class DialogDirectSaleAdapter(
    private val listFourColumn: ArrayList<ThreeColumnModel>,
    private val fourColumnInterface : MedicalUtil.TwoColumnInterface
) {
    private lateinit var itemDirectSale: ItemSearchTwoColumnBinding
    private var listCL = ArrayList<ConstraintLayout>()


}