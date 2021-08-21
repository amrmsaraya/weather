package com.github.amrmsaraya.weather.util

import android.app.Activity
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*

private const val TAG = "Location"

class LocationHelper(activity: Activity, private val onLocationChange: (Location) -> Unit) {
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    var isStarted = false
        private set

    init {
        buildLocationRequest()
        buildLocationCallback()
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onLocationChange(locationResult.lastLocation)
            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10 * 60 * 1000
            smallestDisplacement = 500f
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    fun startLocationUpdates() {
        try {
            Log.d(TAG, "Location started.")
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            isStarted = true
        } catch (exception: SecurityException) {
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $exception")
        }
    }

    fun stopLocationUpdates() {
        try {
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Location Callback removed.")
                } else {
                    Log.d(TAG, "Failed to remove Location Callback.")
                }
            }

        } catch (exception: SecurityException) {
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $exception")
        }
    }
}
