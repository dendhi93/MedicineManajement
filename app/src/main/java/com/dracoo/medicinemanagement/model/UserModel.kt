package com.dracoo.medicinemanagement.model

data class UserModel(
    val createDate : String = "",
    val username : String,
    val password : String,
    val isActive : Int = 0,
    val address : String = "",
    val userRole : String = "",
)
