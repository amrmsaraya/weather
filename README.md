<p align="center">
  <img src="https://raw.githubusercontent.com/amrmsaraya/Weather/main/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png">
</p>

# Weather - [Google Play](https://play.google.com/store/apps/details?id=com.github.amrmsaraya.weather)
Android mobile application that inform user about the current weather condition in a selected location using GPS or choose a place from google-maps, user can add favorite places which can be accessed any time to check weather condition, default notifications for weather alerts which can be disabled, user can add custom alarms in a specific time range to be informed if there is any alerts within this time period, user can change current location provider, application language or temeprature and windspeed units any time.

## Features
- Real-Time weather condition with full details
- Favorite places to save places which user regularly check
- Automatic notifications about weather alerts
- Custom alarms in a specific time range to be informed if there is any alerts within this time period
- Change location provider (GPS, Google-Maps Location)
- Light / Dark theme support
- 6 Colorful palettes to choose from
- Change application language (English, Arabic)
- Change temperature unit (Celsius, Kelvin, Fahrenheit)
- Change wind speed unit (m/s, mph)

## Libraries and Frameworks

- [Jetpack Compose](https://developer.android.com/jetpack/compose?) - Declarative UI Framework
- [Material Design](https://material.io/design)  - Design System
- [Coroutines Flows](https://kotlinlang.org/docs/reference/coroutines/flow.html) - Reactive Programming
- [Ktor](https://ktor.io/) - HTTP Client
- [Room](https://developer.android.com/jetpack/androidx/releases/room) - Local Database
- [Hilt](http://google.github.io/hilt/) - Dependency Injection
- [Coil](https://coil-kt.github.io/coil/compose) - Image Loading
- [JUnit](https://junit.org/junit4/) - Unit Testing
- [Truth](https://truth.dev/) - Fluent Assertions

## Architecture and Design Patterns
- [Clean Architecture](https://koenig-media.raywenderlich.com/uploads/2019/02/Clean-Architecture-Bob-650x454.png) - Application architecture pattern
	- :app module - Presentation layer that contains UI related code and dependency injection
	- :data module - Data layer that contains DAOs, DTOs, Mapper, Http services, Data sources and Repository Implementation
	- :domain module - Business layer that contains Repository interfaces and Models (Entities)
- [MVI](https://miro.medium.com/max/5152/1*iFis87B9sIfpsgQeFkgu8Q.png) - Model-View-Intent design pattern


