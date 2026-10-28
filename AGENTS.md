# AGENTS.md

## Project

Pulso — a Wear OS app for musicians built with Kotlin and Jetpack Compose.

- Package / applicationId: `com.mx.weartwo`
- minSdk 30, targetSdk 36, Java 11
- Build system: Gradle (Kotlin DSL), version catalog in `gradle/libs.versions.toml`
- Single module: `:app`

## Architecture

Clean architecture with three layers:

- `domain/` — pure Kotlin, no Android dependencies. Models, the `MetronomeEngine`, and output ports (`ClickOutput`, `HapticOutput`, `ElapsedRealtimeClock`).
- `data/` — Android adapters implementing the domain ports (`AudioClickOutput`, `AndroidHapticOutput`, `AndroidElapsedRealtimeClock`).
- `presentation/` — Compose UI, navigation, and `ViewModel`s. Screens are stateless and driven by `StateFlow`.

## Commit Rules

- Commit changes in English.
- Commit on each task done on the CLI: after completing a unit of work, run `git add` and `git commit` with a concise English message in the style of the existing history (e.g. `First commit`).
- Do not commit secrets or local config (`local.properties`, `.idea`, build outputs).

## Build & Verify

- Build: `./gradlew assembleDebug`
- Tests: `./gradlew test` and `./gradlew connectedAndroidTest` (device/emulator required)
- Lint: `./gradlew lint`
