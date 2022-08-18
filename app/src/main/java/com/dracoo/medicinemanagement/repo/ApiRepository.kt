package com.dracoo.medicinemanagement.repo

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.utils.ConstantsObject
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


@ViewModelScoped
class ApiRepository@Inject
constructor(
    @ApplicationContext private val context: Context
) {
     suspend fun getMedicineMaster(callback: ApiCallback<List<MedicineMasterModel>>) {
         val list = ArrayList<MedicineMasterModel>()
         val queue = Volley.newRequestQueue(context)
         withContext(Dispatchers.IO) {
             val stringReq = StringRequest(Request.Method.GET, ConstantsObject.vMasterObatExcel2,
                 { response ->
                     try {
                         response.let {
//                             Timber.e("response $response")
                             val feedObj = JSONObject(it)
                             Timber.e("obj $feedObj")
                             val entryArray: JSONArray = feedObj.getJSONArray("values")
                             (0 until entryArray.length()).forEach { i ->
                                 Timber.e("array " +entryArray[i].toString())
//                                 val entryObj = entryArray.getJSONObject(i)
//                                 val timeStamp = entryObj.getJSONObject("gsx\$TimeStamp").getString("\$t")
//                                 val kodeObat = entryObj.getJSONObject("gsx\$kodeobat").getString("\$t")
//                                 val satuanObat = entryObj.getJSONObject("gsx\$satuanobat").getString("\$t")
//                                 val hargaObat = entryObj.getJSONObject("gsx\$hargasatuan").getInt("\$t")
//                                 val namaObat = entryObj.getJSONObject("gsx\$namaobat").getString("\$t")
//
//                                 list.add(MedicineMasterModel(
//                                     Timestamp = timeStamp,
//                                     kodeobat = kodeObat,
//                                     satuanobat = satuanObat,
//                                     hargasatuan = hargaObat,
//                                     namaobat = namaObat
//                                 ))
                             }
                             callback.onDataLoaded(list)
                         }
                     }catch (e :Exception){ callback.onDataError("error $e") }
                 },
                 { callback.onDataError("failed to get master medicine") }
             )
             queue.add(stringReq)
         }
    }

    //interface response from server
    interface ApiCallback<T> {
        fun onDataLoaded(data: T?)
        fun onDataError(error: String?)
    }
}