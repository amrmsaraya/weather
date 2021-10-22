package com.github.amrmsaraya.weather.util.dispatchers

import kotlinx.coroutines.Dispatchers

class IDispatchersImpl : IDispatchers {
    override val default = Dispatchers.Default
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
    override val unconfined = Dispatchers.Unconfined
}