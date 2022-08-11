package com.dracoo.medicinemanagement.model

data class MenuModel(
    val title: String,
    val subtitle: String,
    val color: Int,
    val icon: Int,
    val intersectImage: Int,
    val menu_id: String,
    val totalNotUpload: Int = 0
)
