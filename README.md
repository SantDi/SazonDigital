# Sazón Digital — Semana 3 (Compose + Hilt + Room)

## Versiones
- Gradle Wrapper: 8.9 (regenera con `gradle wrapper --gradle-version 8.9` si falta el jar)
- AGP: 8.6.1
- Kotlin: 2.0.20
- Compose Compiler Plugin: aplicado (Kotlin 2.x)
- AndroidX: activado (`android.useAndroidX=true`), Jetifier desactivado

## Ejecutar
1. Abrir en Android Studio Ladybug+.
2. Sync Gradle.
3. Ejecutar en Android 10+.

### Dynamic Colors
- Activados con `DynamicColors.applyToActivitiesIfAvailable(this)` en `SazonApp`.
- Requiere Android 12+ para paleta basada en wallpaper; en versiones anteriores usa esquema por defecto.
