# SWRLZ Core Android Foundation

This repository contains a reusable Android foundation for future SWRLZ applications. It is organized as a modular Android project with a core domain/data layer, a design system module, and a feature module that can be extended for product-specific experiences.

## Goals
- Provide a clean, reusable Android foundation.
- Keep modules decoupled and maintainable.
- Support future SWRLZ applications through inheritance and extension.

## Modules
- app: Android application entry point and host UI.
- core: shared domain and data abstractions.
- designsystem: reusable Compose styling primitives.
- featurehome: starter feature module demonstrating composition.

## Build
Run:

./gradlew --no-daemon --max-workers=1 assembleDebug

The output APK is produced at app/build/outputs/apk/debug/app-debug.apk.