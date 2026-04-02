# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

TV Browser Kotlin is an Android TV browser app with D-pad remote control support. Single-Activity architecture using programmatic UI (no XML layouts).

## Build Commands

```bash
# Set SDK path (first time)
echo "sdk.dir=/path/to/android/sdk" > local.properties

# Debug build
./gradlew assembleDebug

# Clean build
./gradlew clean assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

## Architecture

- **Single Activity**: `MainActivity.kt` handles all UI and navigation
- **Programmatic UI**: All views built in Kotlin code (no XML layouts)
- **WebView-based browsing**: Full web browsing with video support
- **D-pad navigation**: Left/Right/Up/Down scroll, Center to focus, Back to go back

### Key Components

| File | Role |
|------|------|
| `MainActivity.kt` | UI container, WebView, URL bar, progress bar, D-pad key handling |
| `TVBrowserApp.kt` | Application class (minimal, no initialization needed) |
| `AndroidManifest.xml` | Leanback/TV launcher intent filters, INTERNET permission |

### D-pad Key Handling (in `onKeyDown`)

- `DPAD_LEFT/RIGHT/UP/DOWN`: Scrolls WebView by 50px
- `DPAD_CENTER`: Requests WebView focus
- `BACK`: Goes to previous page if available

### WebView Configuration

- JavaScript enabled
- DOM storage enabled
- Media playback does NOT require user gesture (for video)
- Custom `WebViewClient` (updates URL bar on page load)
- Custom `WebChromeClient` (progress bar, title updates)

## Dependencies

- `androidx.core:core-ktx:1.12.0`
- `androidx.appcompat:appcompat:1.6.1`

## Notes

- No test suite exists currently
- Min SDK: 21 (Android 5.0), Target SDK: 34
- Leanback feature is optional (app works on regular Android too)
