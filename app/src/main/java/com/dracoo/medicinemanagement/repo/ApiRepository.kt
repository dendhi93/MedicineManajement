package com.dracoo.medicinemanagement.repo

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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
             queue.add(stringReq)
         }
    }

    //interface response from server
    interface ApiCallback<T> {
        fun onDataLoaded(data: T?)
        fun onDataError(error: String?)
    }
}