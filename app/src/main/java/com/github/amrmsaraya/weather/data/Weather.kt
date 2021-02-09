package com.github.amrmsaraya.weather.data

import com.google.gson.annotations.SerializedName

data class Weather(

	@SerializedName("id") val id: Int,
	@SerializedName("main") val main: String,
	@SerializedName("description") val description: String,
	@SerializedName("icon") val icon: String
)
