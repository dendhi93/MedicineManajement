package com.dracoo.medicinemanagement.repo

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dracoo.medicinemanagement.model.MedicineMasterModel
import com.dracoo.medicinemanagement.utils.ConstantsObject
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


@ViewModelScoped
class ApiRepository@Inject
constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun getMedicineMaster() : List<MedicineMasterModel>{
        var list = ArrayList<MedicineMasterModel>()
        val queue = Volley.newRequestQueue(context)

        val stringReq = StringRequest(Request.Method.GET, ConstantsObject.vMasterObatExcel,
            { response ->
                try {
                    val feedObj = JSONObject(response).getJSONObject("feed")
                    val entryArray: JSONArray = feedObj.getJSONArray("entry")
                    for (i in 0 until entryArray.length()) {
                        val entryObj = entryArray.getJSONObject(i)
                        val timeStamp = entryObj.getJSONObject("gsx\$TimeStamp").getString("\$t")
                        val kodeObat = entryObj.getJSONObject("gsx\$kodeobat").getString("\$t")
                        val satuanObat = entryObj.getJSONObject("gsx\$satuanobat").getString("\$t")
                        val hargaObat = entryObj.getJSONObject("gsx\$hargaobat").getInt("\$t")
                        val namaObat = entryObj.getJSONObject("gsx\$hargaobat").getInt("\$t")
//                        userModalArrayList.add(UserModal(firstName, lastName, email, avatar))
//                        list.add(MedicineMasterModel(
//
//                        ))
                    }

                }catch (e : Exception){
                    Timber.e("err Exception $e")
                }
            },
            { Timber.e("failed to get master medicine") })
        queue.add(stringReq)

        return list
    }
}