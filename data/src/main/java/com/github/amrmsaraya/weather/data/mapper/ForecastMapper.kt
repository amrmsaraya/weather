package com.github.amrmsaraya.weather.data.mapper

import com.github.amrmsaraya.weather.data.model.forecast.*
import com.github.amrmsaraya.weather.domain.model.forecast.*

fun ForecastDTO.toForecast(): Forecast {
    return Forecast(
        id = id,
        lat = lat,
        lon = lon,
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        current = current.toCurrent(),
        hourly = hourly.map { it.toHourly() },
        daily = daily.map { it.toDaily() },
        alerts = alerts.map { it.toAlert() }
    )
}

fun Forecast.toDTO(): ForecastDTO {
    return ForecastDTO(
        id = id,
        lat = lat,
        lon = lon,
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        current = current.toDTO(),
        hourly = hourly.map { it.toDTO() },
        daily = daily.map { it.toDTO() },
        alerts = alerts.map { it.toDTO() }
    )
}

private fun CurrentDTO.toCurrent(): Current {
    return Current(
        dt = dt,
        sunrise = sunrise,
        sunset = sunset,
        temp = temp,
        feelsLike = feelsLike,
        pressure = pressure,
        humidity = humidity,
        dewPoint = dewPoint,
        uvi = uvi,
        clouds = clouds,
        visibility = visibility,
        windSpeed = windSpeed,
        windDeg = windDeg,
        weather = weather.map { it.toWeather() }
    )
}

private fun Current.toDTO(): CurrentDTO {
    return CurrentDTO(
        dt = dt,
        sunrise = sunrise,
        sunset = sunset,
        temp = temp,
        feelsLike = feelsLike,
        pressure = pressure,
        humidity = humidity,
        dewPoint = dewPoint,
        uvi = uvi,
        clouds = clouds,
        visibility = visibility,
        windSpeed = windSpeed,
        windDeg = windDeg,
        weather = weather.map { it.toDTO() }
    )
}

private fun DailyDTO.toDaily(): Daily {
    return Daily(
        dt = dt,
        sunrise = sunrise,
        sunset = sunset,
        moonrise = moonrise,
        moonset = moonset,
        moonPhase = moonPhase,
        temp = temp.toTemp(),
        feelsLike = feelsLike.toFeelsLike(),
        pressure = pressure,
        humidity = humidity,
        dewPoint = dewPoint,
        windSpeed = windSpeed,
        windDeg = windDeg,
        windGust = windGust,
        weather = weather.map { it.toWeather() }
    )
}

private fun Daily.toDTO(): DailyDTO {
    return DailyDTO(
        dt = dt,
        sunrise = sunrise,
        sunset = sunset,
        moonrise = moonrise,
        moonset = moonset,
        moonPhase = moonPhase,
        temp = temp.toDTO(),
        feelsLike = feelsLike.toDTO(),
        pressure = pressure,
        humidity = humidity,
        dewPoint = dewPoint,
        windSpeed = windSpeed,
        windDeg = windDeg,
        windGust = windGust,
        weather = weather.map { it.toDTO() }
    )
}

private fun HourlyDTO.toHourly(): Hourly {
    return Hourly(
        dt = dt,
        temp = temp,
        feelsLike = feelsLike,
        pressure = pressure,
        humidity = humidity,
        dewPoint = dewPoint,
        uvi = uvi,
        clouds = clouds,
        visibility = visibility,
        windSpeed = windSpeed,
        windDeg = windDeg,
        windGust = windGust,
        weather = weather.map { it.toWeather() }
    )
}

private fun Hourly.toDTO(): HourlyDTO {
    return HourlyDTO(
        dt = dt,
        temp = temp,
        feelsLike = feelsLike,
        pressure = pressure,
        humidity = humidity,
        dewPoint = dewPoint,
        uvi = uvi,
        clouds = clouds,
        visibility = visibility,
        windSpeed = windSpeed,
        windDeg = windDeg,
        windGust = windGust,
        weather = weather.map { it.toDTO() }
    )
}

private fun AlertDTO.toAlert(): Alert {
    return Alert(
        senderName = senderName,
        event = event,
        start = start,
        end = end,
        description = description,
        tags = tags
    )
}

private fun Alert.toDTO(): AlertDTO {
    return AlertDTO(
        senderName = senderName,
        event = event,
        start = start,
        end = end,
        description = description,
        tags = tags
    )
}

private fun TempDTO.toTemp(): Temp {
    return Temp(
        day = day,
        min = min,
        max = max,
        night = night,
        eve = eve,
        morn = morn
    )
}

private fun Temp.toDTO(): TempDTO {
    return TempDTO(
        day = day,
        min = min,
        max = max,
        night = night,
        eve = eve,
        morn = morn
    )
}

private fun FeelsLikeDTO.toFeelsLike(): FeelsLike {
    return FeelsLike(
        day = day,
        night = night,
        eve = eve,
        morn = morn
    )
}

private fun FeelsLike.toDTO(): FeelsLikeDTO {
    return FeelsLikeDTO(
        day = day,
        night = night,
        eve = eve,
        morn = morn
    )
}

private fun WeatherDTO.toWeather(): Weather {
    return Weather(
        id = id,
        main = main,
        description = description,
        icon = icon
    )
}

private fun Weather.toDTO(): WeatherDTO {
    return WeatherDTO(
        id = id,
        main = main,
        description = description,
        icon = icon
    )
}