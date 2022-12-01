package com.dracoo.medicinemanagement.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockOpnameModel(
    val KodeObat : String,
    val NamaObat : String,
    val NoFaktur : String,
    val HargaSatuan : String,
    val Jumlah : String,
    val CreateDate : String,
    val UserCreate : String,
    val monthYear : String
): Parcelable
