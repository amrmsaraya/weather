package com.github.amrmsaraya.weather.util

import android.content.Context
import android.content.ContextWrapper
import android.os.LocaleList
import java.util.*

object LocaleHelper {
    fun setLocale(mContext: Context, newLocale: Locale): ContextWrapper {
        var context = mContext
        val res = context.applicationContext.resources
        val configuration = res.configuration
        configuration.setLocale(newLocale)
        configuration.setLayoutDirection(newLocale)
        val localeList = LocaleList(newLocale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)
        context = context.createConfigurationContext(configuration)
        return ContextWrapper(context)
    }
}
