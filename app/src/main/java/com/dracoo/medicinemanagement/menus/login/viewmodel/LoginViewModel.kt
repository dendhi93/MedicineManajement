package com.dracoo.medicinemanagement.menus.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracoo.medicinemanagement.model.UserModel
import com.dracoo.medicinemanagement.repo.ApiRepository
import com.dracoo.medicinemanagement.repo.DataStoreRepo
import com.dracoo.medicinemanagement.utils.ConstantsObject
import com.dracoo.medicinemanagement.utils.DataCallback
import com.dracoo.medicinemanagement.utils.MedicalUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val storeRepository : DataStoreRepo,
    private val apiRepository: ApiRepository
): ViewModel() {

    fun saveUser(name : String, address : String,password :  String, role : String){
        viewModelScope.launch(Dispatchers.IO) {
            storeRepository.saveUser(name, password,role,address)
        }
    }

    fun postLogin(model : UserModel, callback: DataCallback<UserModel>){
        viewModelScope.launch {
            apiRepository.postLogin(model, ConstantsObject.vLoginJson,object : ApiRepository.ApiCallback<JSONObject>{
                override fun onDataLoaded(data: JSONObject?) {
                    data?.let {
                        when(it.getString(ConstantsObject.vMessageKey)){
                            ConstantsObject.vSuccessResponse ->{
                                val jObjUser = it.getJSONObject("user")
                                val userModel = UserModel(
                                    createDate = MedicalUtil.getCurrentDateTime(ConstantsObject.vTahunJamSetrip),
                                    username = model.username,
                                    password = model.password,
                                    isActive = 1,
                                    address = jObjUser.getString("address"),
                                    userRole = jObjUser.getString("userRole")
                                )
                                callback.onDataLoaded(userModel)
                            }
                            else -> callback.onDataError(it.getString(ConstantsObject.vMessageKey))
                        }
                    }
                }

                override fun onDataError(error: String?) {
                    error?.let {
                        callback.onDataError(it)
                    }
                }
            })
        }
    }
}