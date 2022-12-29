package com.dracoo.medicinemanagement.model

data class DirectSaleModel(
    var noTagihan : String,
    var kodeObat : String,
    var namaObat : String,
    var hargaSatuan : String,
    var jumlah : String,
    var total : String,
    var createDate : String,
    var userCreate : String,
    var isReverse : String = "0"
)
