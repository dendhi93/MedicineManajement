package com.dracoo.medicinemanagement.utils

object ConstantsObject {

    //snack bar
    const val vSnackBarWithTombol = 11
    const val vSnackBarWithOutTombol = 22

    //toast
    const val vLongToast = "long_toast"
    const val vShortToast = "short_toast"

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

    //dialog type
    const val vConfirmTitle = "Konfirmasi"
    const val vWarningTitle = "Warning"

    //menus
    const val vExitApps = "Keluar Aplikasi"
    const val vInputMedicine = "Input Obat Baru"
    const val vStockOpname = "Input Stock"
    const val vReportStockOpname = "Laporan Input Stock"
    const val vDirectSales = "Penjualan Obat"
    const val vReportDirectSales = "Laporan Penjualan Obat"

    //url
    const val vFinalMasterObatPostV10 = "https://script.google.com/macros/s/AKfycbze5aFFN0D6PH3YB7cyXjI4t78M-Gbu6Js7kZhpzGog7npqmB1TvrckcG-_Q0Mei9eYow/exec"
    const val vFinalMasterObatPostV11 = "https://script.google.com/macros/s/AKfycbwQ6S_65nqAmgHKE2H2lekhU6qwevv8ruebLiljvDOLDtpzWrpS6luzqeNB3lV-HMej/exec"
    const val vGetMasterObatExcel = "https://sheets.googleapis.com/v4/spreadsheets/1oH6Arqf17h5gdSBpjlh_tTuoNxF7qzNh_WsZQCFcAiY/values/Sheet1!A2:F1000?key=AIzaSyCvMjWrwreKmgqO9OspuYxY8f53V6SfQ8g"
    const val vStockOpnamePostTransV3 ="https://script.google.com/macros/s/AKfycbyDXIlQL1XbpyWIFwq1A8WWRQCrfLkEEs4FzGa9QpNWsPQIDl3-VKHq3t7eo723nQeH/exec"
    const val vStockOpnamePostTransV4 ="https://script.google.com/macros/s/AKfycbz03efzmfjeTbxxwL24KfA3IDVmP3lQY8Qby7ADR0THMqxya09dO0ZuKbE-mCpoHCN1/exec"
    const val vPostLoginV3 = "https://script.google.com/macros/s/AKfycbwyU1Vj9IqpO69HK8zjafauf_cogd8dr__iQeZhhb4xeH_yRBeFJIwIW30dRucLKNOx/exec"
    const val vStockOpnameGetV3 = "https://script.google.com/macros/s/AKfycbzklfRJr8gw6u2kOIIXakKodmGNNbc660gif1mitSu_liM02IM9gLcc5fH4LTGBdoMElQ/exec"

    //JSON POST master medicine
//    const val medicineCodeJson = "entry.438343947"
//    const val medicineNameJson = "entry.1827398503"
//    const val piecesTypeJson = "entry.1875704685"
//    const val piecesPrizeJson = "entry.1235443380"
//    const val medicineCategoryJson = "entry.559034742"

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
    const val usernameJson = "username"
    const val passwordJson = "password"
    const val vNewJson = "New"
    const val vLoginJson = "Login"
    const val vMonthYearJson = "monthYear"

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

    //response message API
    const val vSuccessResponse = "Success"
    const val vMessageKey = "message"

    //action API
    const val vAddEditAction = "Add Edit"
    const val vDeleteJson = "Delete"

    //extras
    const val vExtrasStockOpname = "extras_Stock_Opname"
    const val vExtrasActionForm = "extras_Action_Form"
}