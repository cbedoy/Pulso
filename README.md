# Pulso 🥁

A Wear OS app for musicians.

## Modules

- **Metronome** — BPM (40–208), time signature (2/4–6/4), accented downbeat, audio click and haptic vibration.
- **Note Identifier** — identify the note or chord name from a given sound (coming soon).

## Features

- Adjustable tempo and time signature with a live beat indicator (pulsing current beat).
- Accented downbeat: higher-pitch click + stronger vibration.
- Optional haptic feedback on every beat.
- Warm, spicy dark theme with smooth animations.

## Architecture

Clean architecture, three layers:

```
com.mx.weartwo
├── domain/        pure Kotlin — MetronomeEngine, models, output ports
├── data/          Android adapters — AudioClickOutput, AndroidHapticOutput, clock
└── presentation/  Compose UI, navigation, ViewModels
```

## Tech Stack

- Kotlin, Jetpack Compose for Wear OS (Material 3)
- Kotlin Coroutines + Flow
- AndroidX Lifecycle ViewModel

## Build

```bash
./gradlew assembleDebug
```

Deploy to a Wear OS device or emulator:

```bash
./gradlew installDebug
```

## Credits

Made with 🌮 🌶️ by Bedoy
