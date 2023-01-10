package com.dracoo.medicinemanagement.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.dracoo.medicinemanagement.R
import com.dracoo.medicinemanagement.databinding.DialogSearch2ColumnBinding
import com.dracoo.medicinemanagement.model.DirectSaleModel
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.model.StockOpnameModel
import com.dracoo.medicinemanagement.model.ThreeColumnModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList


object MedicalUtil {
    private lateinit var search2ColumnAdapter: SearchTwoColumnAdapter


    fun snackBarMessage(snackMessage: String, activity: Activity, action: Int) {
        val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
        when(action) {
            ConstantsObject.vSnackBarWithTombol -> {
                val snackBar = Snackbar.make(rootView, snackMessage, Snackbar.LENGTH_INDEFINITE)
                snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5
                snackBar.setActionTextColor(Color.WHITE)
                snackBar.setAction("OK") {
                    snackBar.dismiss()
                }
                snackBar.show()
            }
            else -> {
                val snackBar = Snackbar.make(rootView, snackMessage, Snackbar.LENGTH_LONG)
                snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5
                snackBar.setActionTextColor(Color.WHITE)
                snackBar.show()
            }
        }
    }

    fun toastMessage(context: Context, message: String, toastType: String) {
        val toast = when (toastType) {
            ConstantsObject.vLongToast -> Toast.makeText(context, message, Toast.LENGTH_LONG)
            else -> Toast.makeText(context, message, Toast.LENGTH_SHORT)
        }
        toast.show()
    }

    fun alertDialogDismiss(alertMessage : String,
                           alertTitle : String,context: Context,
                           isDismiss : Boolean){
        val alertDialog = AlertDialog.Builder(context).setTitle(alertTitle)
        alertDialog.setMessage(alertMessage)
        alertDialog.setPositiveButton(android.R.string.ok){
                dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setCancelable(false)
        val alert: AlertDialog = alertDialog.create()
        when(isDismiss){
            true -> {
                if(alert.isShowing){
                    alert.dismiss()
                }
            }
            else ->{
                if(!alert.isShowing){
                    alert.show()
                }
            }
        }
    }

    fun showDialogConfirmation(
        context: Context,
        title: String,
        msg: String,
        callback: () -> Unit
    ) {
        val dialogConfirmation =  MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(msg)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ -> callback() }
            .setCancelable(false)
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
            }

        when {
            !dialogConfirmation.isShowing -> {
                dialogConfirmation.show()
            }
            else -> {
                dialogConfirmation.cancel()
                dialogConfirmation.dismiss()
            }
        }
    }

    fun getCurrentDateTime(dateTimeFormat : String): String =
        SimpleDateFormat(dateTimeFormat, Locale.getDefault()).format(Date())

    fun getChangeDateFormat(stDateTime: String, stSourceDateTimeFormat : String, stResultDateFormat : String): String? {
        return try {
            val source = SimpleDateFormat(stSourceDateTimeFormat, Locale.getDefault())

            val dateSource = source.parse(stDateTime.trim())
            val dateFormat = SimpleDateFormat(stResultDateFormat, Locale.getDefault())

            if (dateSource != null) {
                dateFormat.format(dateSource)
            } else { "" }
        } catch (e: Exception) { "" }
    }

    fun moneyFormat(input : Double) : String?{
        return try{
//            val format: NumberFormat = NumberFormat.getCurrencyInstance()
//            format.maximumFractionDigits = 0
//            format.currency = Currency.getInstance("IDR")
//
//            format.format(input).toString()
            val formatter: NumberFormat = DecimalFormat("#,###")
            formatter.format(input)
        }catch (e:Exception){ "" }
    }

    fun filterMedicineMaster(text: String?, displayedList: ArrayList<MedicineMasterModel>): ArrayList<MedicineMasterModel> {
        val temp: ArrayList<MedicineMasterModel> = ArrayList()
        for (d in displayedList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (text != null) {
                if (d.namaobat.lowercase().contains(text.lowercase())) {
                    temp.add(d)
                }
            }
        }
        //update recyclerview
        return temp
    }

    fun filterSOAdapter(text: String?, displayedList: ArrayList<StockOpnameModel>): ArrayList<StockOpnameModel> {
        val tempSO: ArrayList<StockOpnameModel> = ArrayList()
        for (d in displayedList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (text != null) {
                if (d.NamaObat.lowercase().contains(text.lowercase())) {
                    tempSO.add(d)
                }
            }
        }
        //update recyclerview
        return tempSO
    }

    fun filterDirectSalesAdapter(text: String?, displayedList: ArrayList<DirectSaleModel>): ArrayList<DirectSaleModel> {
        val tempDs: ArrayList<DirectSaleModel> = ArrayList()
        for (d in displayedList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (text != null) {
                if (d.namaObat.lowercase().contains(text.lowercase()) || d.noTagihan.lowercase().contains(text.lowercase())) {
                    tempDs.add(d)
                }
            }
        }
        //update recyclerview
        return tempDs
    }

    fun initPopUpSearch2Column(
        activity: AppCompatActivity,
        titleText: String,
        listData: ArrayList<ThreeColumnModel>,
        col1Text: String,
        col2Text: String,
        twoColumnInterface: TwoColumnInterface
    ): Dialog {
        val dialogData = Dialog(activity)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogData.window!!.setGravity(Gravity.CENTER)

        val bindingPopUpS2C = DialogSearch2ColumnBinding.inflate(activity.layoutInflater)
        val view = bindingPopUpS2C.root
        dialogData.setContentView(view)

        val lpNumberPicker = WindowManager.LayoutParams()
        val window: Window = dialogData.window!!
        lpNumberPicker.copyFrom(window.attributes)

        lpNumberPicker.width = WindowManager.LayoutParams.MATCH_PARENT
        lpNumberPicker.height = WindowManager.LayoutParams.WRAP_CONTENT

        window.setGravity(Gravity.CENTER)
        window.attributes = lpNumberPicker

        bindingPopUpS2C.apply {
            searchLabelS2cdTv.text = titleText
            searchS2cdEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    val listDisplayed2Data = filter2Column(s.toString(), listData).distinct().toList()
                    search2ColumnAdapter =
                        SearchTwoColumnAdapter(ArrayList(listDisplayed2Data), twoColumnInterface)
                    listS2cdRv.apply {
                        layoutManager =
                            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                        adapter = search2ColumnAdapter
                    }
                }
            })

            search2ColumnAdapter = SearchTwoColumnAdapter(listData, twoColumnInterface)
            listS2cdRv.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                adapter = search2ColumnAdapter
            }
            cancelS2cdMb.setOnClickListener {
                dialogData.dismiss()
                dialogData.cancel()
            }
            col1S2cdTv.text = col1Text
            col2S2cdTv.text = col2Text
        }
        return dialogData
    }

    private fun filter2Column(
        text: String?,
        displayedList: ArrayList<ThreeColumnModel>): ArrayList<ThreeColumnModel>
    {
        val temp: ArrayList<ThreeColumnModel> = ArrayList()
        for (d in displayedList) {
            if (text != null) {
                if (d.column1.lowercase().contains(text.lowercase()) || d.column2.lowercase()
                        .contains(text.lowercase())
                ) {
                    temp.add(d)
                }
            }
        }
        //update recyclerview
        return temp
    }

    fun showPopUpMenu(context: Context,
                      view: View,
                      onClickMenu: ((selectedItem : String) -> Unit)? = null) {
        val popup = PopupMenu(context, view)
        popup.inflate(R.menu.master_medicine_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item?.itemId) {
                R.id.mnu_edit -> onClickMenu?.invoke(context.getString(R.string.edit_mnu))
                R.id.mnu_detail -> onClickMenu?.invoke(context.getString(R.string.detail_mnu))
                else -> onClickMenu?.invoke(context.getString(R.string.delete_mnu))
            }
            true
        }
        popup.show()
    }

    fun monthAndYearPicker(
        activity: Activity,
        onSelected: ((
            mSelectedMonth : String,
            mSelectedYear : String,
        ) -> Unit)? = null){
        val calendar = Calendar.getInstance()
        val mYear = when{
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> calendar.get(Calendar.YEAR)
            else -> Year.now().value
        }
        MonthYearPickerDialog.Builder(
            activity,
            R.style.Style_MonthYearPickerDialog_Red,
            { year, month ->
                val selectedMonth = month + 1
                when{
                    selectedMonth < 10 -> onSelected?.invoke("0$selectedMonth", year.toString())
                    else -> onSelected?.invoke(selectedMonth.toString(), year.toString())
                }
            },
            mYear,
            Calendar.JANUARY
        )
            .setMinMonth(Calendar.JANUARY)
            .setMinYear(mYear)
            .setMaxMonth(Calendar.DECEMBER)
            .build().show()
    }

    fun initReturnMedical(jsonObject: JSONObject) : ArrayList<MedicineMasterModel>{
        val list = ArrayList<MedicineMasterModel>()
        val jArrayValue = jsonObject.getJSONArray("values")
        (0 until jArrayValue.length()).forEach { i ->
            val stDate = jArrayValue[i].toString().split(",")[0]
                .split("[")[1].replace("\"", "").replace("\\", "")
            val stMedicineCode = jArrayValue[i].toString().split(",")[1].replace("\"", "")
            val stMedicineType = jArrayValue[i].toString().split(",")[2].replace("\"", "")
            val stMedicinePrize = jArrayValue[i].toString().split(",")[3].replace("\"", "")
            val stMedicineName = jArrayValue[i].toString().split(",")[4].split("]")[0].replace("\"", "")
            val stMedicineCategory = jArrayValue[i].toString().split(",")[5].split("]")[0].replace("\"", "")
//                            Timber.e("split result date "  + stDate + "\ncode " + stMedicineCode + " " +
//                                    "\nstMedicineType " + stMedicineType + "\nstMedicinePrize"
//                                    +stMedicinePrize+ "\nstMedicineName " +stMedicineName)
            list.add(MedicineMasterModel(stDate, stMedicineCode,stMedicineType,stMedicinePrize, stMedicineName,stMedicineCategory))
        }

        return list
    }

    fun initReturnMedicalStock(jsonObject: JSONObject) : ArrayList<StockOpnameModel>{
        val listStock = ArrayList<StockOpnameModel>()

        val jArray: JSONArray = jsonObject.getJSONArray(ConstantsObject.vItemRequestJson)
        (0 until jArray.length()).forEach { i ->
            val jo: JSONObject = jArray.getJSONObject(i)
            val medicineName = jo.getString(ConstantsObject.medicineNameJsonV2)
            val medicineCode = jo.getString(ConstantsObject.medicineCodeJsonV2)
            val invoiceNo = jo.getString(ConstantsObject.fakturJson)
            val piecesPrize = jo.getString(ConstantsObject.piecesPrizeJsonV2)
            val qtyMedicine = jo.getString(ConstantsObject.qtyJson)
            val userInput = jo.getString(ConstantsObject.userCreateJson)
            val dateSO = getChangeDateFormat(jo.getString(ConstantsObject.createDateJson),
                ConstantsObject.vSpecialDateJson, ConstantsObject.vTahunJamSetrip)

            listStock.add(StockOpnameModel(medicineCode, medicineName, invoiceNo, piecesPrize, qtyMedicine, dateSO.toString(), userInput))
        }
        return listStock
    }

    fun initReturnDirectSales(jsonObject: JSONObject) : ArrayList<DirectSaleModel>{
        val alDirectSale = ArrayList<DirectSaleModel>()
        val jArray: JSONArray = jsonObject.getJSONArray(ConstantsObject.vItemRequestJson)
        (0 until jArray.length()).forEach { i ->
            val jo: JSONObject = jArray.getJSONObject(i)
            val billNo = jo.getString(ConstantsObject.tagihanNo)
            val medicineCode = jo.getString(ConstantsObject.medicineCodeJsonV2)
            val medicineName = jo.getString(ConstantsObject.medicineNameJsonV2)
            val piecesPrize = jo.getString(ConstantsObject.piecesPrizeJsonV2)
            val qtySold = jo.getString(ConstantsObject.qtyJson)
            val totalMedicine = jo.getString(ConstantsObject.total)
            val dateSale = getChangeDateFormat(jo.getString(ConstantsObject.createDateJson),
                ConstantsObject.vSpecialDateJson, ConstantsObject.vTahunJamSetrip)
            val userCreate = jo.getString(ConstantsObject.userCreateJson)
            val isReverse = jo.getString(ConstantsObject.isReverse)

            alDirectSale.add(DirectSaleModel(billNo, medicineCode, medicineName,piecesPrize,qtySold,
                totalMedicine,dateSale.toString(),userCreate,isReverse))
        }
        return alDirectSale
    }

    fun getRandomString(length: Int) : String {
       return UUID.randomUUID().toString().substring(0,length)
    }

    interface TwoColumnInterface{
        fun selectedTwoSearch(selectedData : ThreeColumnModel)
    }
}