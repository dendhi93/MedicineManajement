package com.dracoo.medicinemanagement.utils

object ConstantsObject {

    //snack bar
    const val vSnackBarWithTombol = 11
    const val vSnackBarWithOutTombol = 22

    //date format
    const val vTglSetrip = "dd-MM-yyyy"
    const val vTahunSetrip = "yyyy-MM-dd"
    const val vTglGaring = "dd/MM/yyyy"
    const val vTahunGaring = "yyyy/MM/dd"
    const val vTglSpasiNamaBulan = "dd MMMM yyyy"
    const val vSpecialDateJson = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"

    //date time format
    const val vTahunJamSetrip = "yyyy-MM-dd HH:mm:ss"
    const val vDateGaringJam = "dd/MM/yyyy HH:mm:ss"
    const val vDateSetripJamMinute = "dd-MM-yyyy HH:mm"

    //message
    const val vNoConnectionTitle = "Tidak Terkoneksi"
    const val vNoConnectionMessage = "Handphone anda tidak terkoneksi jaringan"

    //menus
    const val vExitApps = "Keluar Aplikasi"
    const val vInputMedicine = "Input Obat Baru"
    const val vStockOpname = "Stock Opname"
    const val vReportStockOpname = "Laporan Stock Opname"
    const val vDirectSales = "Penjualan Obat"
    const val vReportDirectSales = "Laporan Penjualan Obat"

    //url
    const val vMasterObatGForm = "https://docs.google.com/forms/d/e/1FAIpQLSfwOK5HQthl2TC9IhvqrIQo-5tshIog2gdluBBSnPZhLlUO9w/formResponse"
//    const val vMasterObatPost = "https://script.google.com/macros/s/AKfycbzpIqee5pMXxAx8ebblmnL1lTrB6Hdf54dZPEqbWKEe6PU_MNnS6MnLfmS_6Zm1GqYFLA/exec"
//    const val vMasterObatPostV2 = "https://script.google.com/macros/s/AKfycbxuz-RFhg4JS3eBDcgWEP5FQqVBsmJpBonvXxKrPltmlJS2KElqYOuDfuVX958hlSpCJw/exec"
    const val vMasterObatPostV3 = "https://script.google.com/macros/s/AKfycbygl683bsvyzEXnGcfGTzTcTsecJXCWau4Q4T-prp3tvdZRwsuje-DAO_ZZLI37nOQ_ag/exec"
    const val vFinalMasterObatPost = "https://script.google.com/macros/s/AKfycbxYnDstP3HSmzCWtrCBFqZj7TjorqF_UToOE2_Uijw-8Z7mYy4ae-NyB6U6UqdmBD4kfQ/exec"
    const val vGetMasterObatExcel = "https://sheets.googleapis.com/v4/spreadsheets/1oH6Arqf17h5gdSBpjlh_tTuoNxF7qzNh_WsZQCFcAiY/values/Sheet1!A2:F1000?key=AIzaSyCvMjWrwreKmgqO9OspuYxY8f53V6SfQ8g"
    const val vStockOpnameGet = "https://script.google.com/macros/s/AKfycbxLq9ebIHp6ITztKMSygfL7F0yVc5D7IGhAAjygurjriZaFkUgWCsErDgwvfG_0iwZmAg/exec"
    const val vStockOpnamePostTrans = "https://script.google.com/macros/s/AKfycbxbSQJQeXVahWoHnHogXS6b8epnmT29XE4tUsCUr3etUSG69__vci1qZHqdk6OEMfYr/exec"

    //JSON POST master medicine
    const val medicineCodeJson = "entry.438343947"
    const val medicineNameJson = "entry.1827398503"
    const val piecesTypeJson = "entry.1875704685"
    const val piecesPrizeJson = "entry.1235443380"
    const val medicineCategoryJson = "entry.559034742"

    //JSON POST master medicine v2
//    const val idMasterObat = "1oH6Arqf17h5gdSBpjlh_tTuoNxF7qzNh_WsZQCFcAiY"
    const val idMasterJson = "id"
    const val actionJson = "action"
    const val medicineTimeStampJson = "TimeStamp"
    const val medicineCodeJsonV2 = "KodeObat"
    const val medicinePieceTypeJsonV2 = "SatuanObat"
    const val piecesPrizeJsonV2 = "HargaSatuan"
    const val medicineNameJsonV2 = "NamaObat"
    const val medicineCategoryJsonV2 = "KategoriObat"

    //JSON POST Stock Opname
    const val idStockOpname = "14Po1xAeOhNbrR_jpeo6oPgeLuiZID8t2fHQ17RgvPik"
    const val fakturJson = "NoFaktur"
    const val qtyJson = "Jumlah"
    const val createDateJson = "CreateDate"
    const val userCreateJson = "UserCreate"

    //input mode
    const val vNewData = "New Data"
    const val vShowData = "Show Data"
    const val vEditData = "Edit Data"

    //action API
    const val vAddEditAction = "Add Edit"
    const val vDeleteJson = "Delete"
}