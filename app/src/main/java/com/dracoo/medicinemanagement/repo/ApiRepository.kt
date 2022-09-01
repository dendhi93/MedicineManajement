package com.dracoo.medicinemanagement.repo

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.utils.ConstantsObject
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


@ViewModelScoped
class ApiRepository @Inject
constructor(
    @ApplicationContext private val context: Context,
) {
     suspend fun getMedicineMaster(callback: ApiCallback<JSONObject>) {
         val queue = Volley.newRequestQueue(context)
         withContext(Dispatchers.IO) {
             val stringReq = StringRequest(Request.Method.GET, ConstantsObject.vMasterObatExcel2,
                 { response ->
                     try {
                         response.let {
                             Timber.e("response $response")
                             callback.onDataLoaded(JSONObject(it))
                         }
                     }catch (e :Exception){ callback.onDataError("error $e") }
                 },
                 {
                     val errObj = JSONObject(it?.message.toString())
                     callback.onDataError(errObj.getString("message"))
                 }
             )
             val retryPolicy: RetryPolicy =
                 DefaultRetryPolicy(6000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
             stringReq.retryPolicy = retryPolicy
             queue.add(stringReq)
             queue.add(stringReq)
         }
    }

    suspend fun postMedicineMaster(model : MedicineMasterModel, callback: ApiCallback<String>){
        val queue = Volley.newRequestQueue(context)
        withContext(Dispatchers.IO) {
            val stringReq: StringRequest = object : StringRequest(
                Method.POST, ConstantsObject.vMasterObatExcel2,
                { response ->
                    try {
                        response.let {
                            Timber.e("response $response")
                            callback.onDataLoaded(response.toString())
                        }
                    }catch (e :Exception){ callback.onDataError("error $e") }
                },
                {
                    callback.onDataError("error")
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val parmas: MutableMap<String, String> = HashMap()

                    //here we pass params
                    parmas["action"] = "addItem"
                    parmas["KodeObat"] = model.kodeobat
                    parmas["SatuanObat"] = model.satuanobat
//                    parmas[ConstantsObject.piecesTypeJson] = model.satuanobat
//                    parmas[ConstantsObject.piecesPrizeJson] = model.hargasatuan
//                    parmas[ConstantsObject.medicineCategoryJson] = model.kategoriObat
                    return parmas
                }
            }

            val retryPolicy: RetryPolicy =
                DefaultRetryPolicy(6000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            stringReq.retryPolicy = retryPolicy
            queue.add(stringReq)
        }
    }

    //interface response from server
    interface ApiCallback<T> {
        fun onDataLoaded(data: T?)
        fun onDataError(error: String?)
    }
}