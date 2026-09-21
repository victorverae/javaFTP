# Dashboard (Android)

App de Android (Kotlin + Jetpack Compose) que muestra un dashboard con
tarjetas de **varias fuentes de datos públicas**, todas sin necesidad de
API key:

| Tarjeta                  | Fuente                                                   |
|---------------------------|----------------------------------------------------------|
| Clima                     | [Open-Meteo](https://open-meteo.com/)                     |
| Criptomonedas              | [CoinGecko](https://www.coingecko.com/en/api)              |
| Frase del día              | [Quotable](https://api.quotable.io/)                       |
| Personas en el espacio     | [Open Notify](http://open-notify.org/Open-Notify-API/)     |

Cada tarjeta se carga de forma independiente y concurrente: si una fuente
falla (sin red, error del servidor, etc.) las demás igual se muestran.

## Estructura del proyecto

```
android/
├── core/   módulo Kotlin/JVM puro: modelos, parsing y llamadas HTTP (Ktor + kotlinx.serialization)
└── app/    módulo Android: UI en Jetpack Compose (Material 3) + ViewModel
```

Separar la lógica de red/parsing en `core` permite testearla con JUnit
puro, sin depender del SDK de Android ni de un emulador.

## Cómo compilar y ejecutar

Requiere **Android Studio** (o el Android SDK + variables de entorno
`ANDROID_HOME`/`ANDROID_SDK_ROOT` configuradas) porque el módulo `app`
depende del Android Gradle Plugin, publicado en el repositorio Maven de
Google (`dl.google.com`).

1. Abre la carpeta `android/` en Android Studio.
2. Deja que sincronice Gradle.
3. Ejecuta la configuración `app` en un emulador o dispositivo (API 24+).

Desde línea de comandos, una vez tengas el SDK instalado:

```bash
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

## Tests

El módulo `core` no requiere el SDK de Android y se puede compilar y
testear con cualquier JDK 17+:

```bash
./gradlew :core:test
```

> **Nota sobre este entorno de desarrollo:** esta sesión en sandbox no
> tiene salida de red hacia `dl.google.com` (el proxy de la organización
> lo bloquea), así que no fue posible resolver el Android Gradle Plugin
> aquí ni compilar/ejecutar `:app`. Sí se validó `:core:test` (5 tests,
> todos verdes) directamente en este entorno. El módulo `app` sigue
> convenciones estándar de Android/Compose y debería compilar sin cambios
> en un entorno con acceso normal a los repositorios de Google.

## Añadir una fuente nueva

1. Crea un modelo de dominio en `core/.../model/Models.kt`.
2. Crea una clase en `core/.../source/` que implemente
   `DashboardSource<T>` (ver `WeatherSource.kt` como referencia): separa
   el *parsing* puro (testeable) de la llamada de red.
3. Agrega un test de parsing en `core/src/test/...`.
4. Regístrala en `DashboardViewModel.Factory` y agrega su rama en
   `DashboardScreen.kt` (`when (value) { is TuModelo -> ... }`).
