package com.dracoo.medicinemanagement.model

import com.google.gson.annotations.SerializedName

data class MasterMedicineResponse(
    @SerializedName("Timestamp")
    val Timestamp: String? = null,

    @SerializedName("kodeobat")
    val kodeobat: String? = null,

    @SerializedName("satuanobat")
    val satuanobat: String? = null,

    @SerializedName("hargasatuan")
    val hargasatuan: String? = null,

    @SerializedName("namaobat")
    val namaobat: String? = null,

    @SerializedName("kategoriObat")
    val kategoriObat: String? = null

)
