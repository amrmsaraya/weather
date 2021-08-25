package com.github.amrmsaraya.weather.util

import android.content.Context
import android.location.Geocoder
import com.github.amrmsaraya.weather.R
import java.util.*

object GeocoderHelper {
    fun getCity(context: Context, lat: Double, lon: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val address = geocoder.getFromLocation(lat, lon, 1).firstOrNull()
            return address?.let {
                when {
                    !it.locality.isNullOrEmpty() -> it.locality
                    !it.adminArea.isNullOrEmpty() -> it.adminArea
                    !it.getAddressLine(0).isNullOrEmpty() -> it.getAddressLine(0)
                    else -> context.getString(R.string.unknown)
                }
            } ?: context.getString(R.string.unknown)
        } catch (exception: Exception) {
            context.getString(R.string.unknown)
        }
    }

    fun getAddress(context: Context, lat: Double, lon: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val address = geocoder.getFromLocation(lat, lon, 1).firstOrNull()
            return address?.let {
                when {
                    !address.getAddressLine(0).isNullOrEmpty() -> address.getAddressLine(0)
                    else -> context.getString(R.string.unknown)
                }
            } ?: context.getString(R.string.unknown)
        } catch (exception: Exception) {
            context.getString(R.string.unknown)
        }
    }
}
