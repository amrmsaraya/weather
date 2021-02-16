package com.github.amrmsaraya.weather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.amrmsaraya.weather.data.models.Alarm
import com.github.amrmsaraya.weather.data.models.Location
import com.github.amrmsaraya.weather.data.models.WeatherResponse
import com.github.amrmsaraya.weather.utils.Converters

@Database(
    entities = [WeatherResponse::class, Location::class, Alarm::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun locationDao(): LocationDao
    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        fun getInstance(context: Context): WeatherDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java,
                        "weather_database"
                    ).build()
                }
                return instance
            }
        }
    }
}
