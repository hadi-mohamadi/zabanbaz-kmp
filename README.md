# Zabanbaz Mobile (KMP + Compose Multiplatform)

UI is shared between **Android** and **iOS** in the `:composeApp` module (Compose Multiplatform).

```
composeApp/     ← shared UI (screens, theme, navigation)
androidApp/     ← Android shell (MainActivity, Application)
iosApp/         ← iOS shell (SwiftUI hosts Compose via ComposeApp framework)
core/           ← networking, storage, DI, presentation base
common/         ← profile, languages, placement, session
feature/        ← auth, startup, onboarding, placement, home, profile-ui
```

## Open in Android Studio

**Open this folder** (`mobile/`), not the parent `zabanbaz/` repo root.

```
File → Open → …/zabanbaz/mobile
```

Wait for **Gradle Sync** to finish. Then:

1. Run configuration: **androidApp** (toolbar dropdown)
2. Device: emulator or physical device
3. Click **Run**

### Gradle sync fails on `commonizeNativeDistribution` / `kotlin-native-prebuilt`

By default, **iOS Kotlin/Native targets are not registered** (`zabanbaz.enableNativeCommonizer=false` in `gradle.properties`). Android Studio sync on Linux therefore does not download the ~300MB `kotlin-native-prebuilt` archive from Maven Central.

If you only develop for **Android**, sync again — no changes needed.

When building **iOS on macOS**, enable native tooling in `gradle.properties` or `local.properties`:

```properties
zabanbaz.enableNativeCommonizer=true
```

Then sync and build the iOS framework. If Maven Central is blocked (VPN), repositories try JetBrains cache redirector and Aliyun Central before Maven Central. Retry on a stable connection if downloads still fail.

If Run is still disabled:

- **File → Sync Project with Gradle Files**
- **Build → Make Project**
- Confirm `local.properties` exists with a valid `sdk.dir` (see `local.properties.example`)
- **File → Project Structure → SDK Location** — set Android SDK path

## Run from terminal

```bash
./gradlew :androidApp:installDebug
adb shell am start -n ir.startup.zabanbaz.android/.MainActivity
```

## API

Production base URL: `https://zabandar.ir/api/v1`

## Stack versions

| Component | Version |
|-----------|---------|
| Kotlin | 2.1.10 |
| AGP | 8.7.3 |
| Compose Multiplatform | 1.7.3 |
| Koin | 4.0.3 |
| Ktor | 3.0.3 |
