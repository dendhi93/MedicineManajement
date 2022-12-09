package com.dracoo.medicinemanagement.repo

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.model.StockOpnameModel
import com.dracoo.medicinemanagement.model.UserModel
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
             val stringReq = StringRequest(Request.Method.GET, ConstantsObject.vGetMasterObatExcel,
                 { response ->
                     try {
                         response.let {
                             Timber.e("response $response")
                             callback.onDataLoaded(JSONObject(it))
                         }
                     }catch (e :Exception){ callback.onDataError("error $e") }
                 },
                 {
                     callback.onDataError(it.toString())
                 }
             )
             val retryPolicy: RetryPolicy =
                 DefaultRetryPolicy(6000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
             stringReq.retryPolicy = retryPolicy
             queue.add(stringReq)
             queue.add(stringReq)
         }
    }

    suspend fun postMedicineMaster(
        model : MedicineMasterModel,
        actionPost : String,
        monthYear : String,
        callback: ApiCallback<MedicineMasterModel>
    ){
        val queue = Volley.newRequestQueue(context)
        withContext(Dispatchers.IO) {
            val stringReq: StringRequest = object : StringRequest(
                Method.POST, ConstantsObject.vFinalMasterObatPostV10,
                { response ->
                    try {
                        response.let {
                            Timber.e("response $response")
                            when {
                                response.contains("Success") -> callback.onDataLoaded(model)
                                else -> callback.onDataError(it.toString())
                            }
                        }
                    }catch (e :Exception){ callback.onDataError("error $e") }
                },
                {
                    callback.onDataError("$it")
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params : HashMap<String, String>  = HashMap()

                    //here we pass params
//                    params[ConstantsObject.idMasterJson] = ConstantsObject.idMasterObat
                    params[ConstantsObject.actionJson] = actionPost
                    params[ConstantsObject.medicineTimeStampJson] = model.Timestamp
                    params[ConstantsObject.medicineCodeJsonV2] = model.kodeobat
                    params[ConstantsObject.medicineNameJsonV2] = model.namaobat
                    params[ConstantsObject.medicinePieceTypeJsonV2] = model.satuanobat
                    params[ConstantsObject.piecesPrizeJsonV2] = model.hargasatuan
                    params[ConstantsObject.medicineCategoryJsonV2] = model.kategoriObat
                    params[ConstantsObject.vMonthYearJson] = monthYear
                    return params
                }
            }

            val retryPolicy: RetryPolicy =
                DefaultRetryPolicy(6000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            stringReq.retryPolicy = retryPolicy
            queue.add(stringReq)
        }
    }

    suspend fun postTransStockOpname(model : StockOpnameModel, actionRequest : String,callback: ApiCallback<String>){
        val queue = Volley.newRequestQueue(context)
        withContext(Dispatchers.IO) {
            val stringReq: StringRequest = object : StringRequest(
                Method.POST, ConstantsObject.vStockOpnamePostTransV3,
                { response ->
                    try {
                        response.let {
                            Timber.e("response $response")
                            when {
                                response.contains("Success") -> callback.onDataLoaded("Success")
                                else -> callback.onDataError(it.toString())
                            }
                        }
                    }catch (e :Exception){ callback.onDataError("error $e") }
                },
                {
                    callback.onDataError("$it")
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params : HashMap<String, String>  = HashMap()

                    //here we pass params
                    params[ConstantsObject.idMasterJson] = ConstantsObject.idStockOpname
                    params[ConstantsObject.medicineCodeJsonV2] = model.KodeObat
                    params[ConstantsObject.fakturJson] = model.NoFaktur
                    params[ConstantsObject.medicineNameJsonV2] = model.NamaObat
                    params[ConstantsObject.piecesPrizeJsonV2] = model.HargaSatuan
                    params[ConstantsObject.qtyJson] = model.Jumlah
                    params[ConstantsObject.createDateJson] = model.CreateDate
                    params[ConstantsObject.userCreateJson] = model.UserCreate
                    params[ConstantsObject.vMonthYearJson] = model.monthYear
                    params[ConstantsObject.actionJson] = actionRequest
                    return params
                }
            }

            val retryPolicy: RetryPolicy =
                DefaultRetryPolicy(6000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            stringReq.retryPolicy = retryPolicy
            queue.add(stringReq)
        }
    }

    suspend fun postStockOpnameData(monthYear: String, callback: ApiCallback<JSONObject>){
        val queue = Volley.newRequestQueue(context)
        withContext(Dispatchers.IO) {
            val stringReq: StringRequest = object : StringRequest(
                Method.POST, ConstantsObject.vStockOpnameGetV2,
                { response ->
                    try {
                        response.let {
                            Timber.e("response $response")
                            callback.onDataLoaded(JSONObject(it))
                        }
                    }catch (e :Exception){ callback.onDataError("error $e") }
                },
                {
                    callback.onDataError("$it")
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params : HashMap<String, String>  = HashMap()

                    //here we pass params
                    params[ConstantsObject.idMasterJson] = ConstantsObject.idStockOpname
                    params[ConstantsObject.vMonthYearJson] = monthYear

                    return params
                }
            }

            val retryPolicy: RetryPolicy =
                DefaultRetryPolicy(6000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            stringReq.retryPolicy = retryPolicy
            queue.add(stringReq)
        }
    }

    suspend fun postLogin(model : UserModel,actionLogin : String, callback: ApiCallback<JSONObject>){
        val queue = Volley.newRequestQueue(context)
        withContext(Dispatchers.IO) {
            val stringReq: StringRequest = object : StringRequest(
                Method.POST, ConstantsObject.vPostLoginV3,
                { response ->
                    try {
                        response.let {
                            Timber.e("response Login $response")
                            callback.onDataLoaded(JSONObject(it))
                        }
                    }catch (e :Exception){ callback.onDataError("error $e") }
                },
                {
                    callback.onDataError("$it")
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params : HashMap<String, String>  = HashMap()

                    //here we pass params
                    params[ConstantsObject.actionJson] = actionLogin
                    params[ConstantsObject.usernameJson] = model.username
                    params[ConstantsObject.passwordJson] = model.password

                    return params
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