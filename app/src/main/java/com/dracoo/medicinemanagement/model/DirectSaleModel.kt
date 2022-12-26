package com.dracoo.medicinemanagement.model

data class DirectSaleModel(
    var noTagihan : String,
    var kodeObat : String,
    var namaObat : String,
    var hargaSatuan : Double,
    var jumlah : Double,
    var total : String,
    var createDate : String,
    var userCreate : String
)
