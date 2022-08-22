package com.dracoo.medicinemanagement.model

import com.google.gson.annotations.SerializedName

data class NewMedicineResponse(

	@field:SerializedName("majorDimension")
	val majorDimension: String? = null,

	@field:SerializedName("values")
	val values: List<String?>? = null,

	@field:SerializedName("range")
	val range: String? = null
)
