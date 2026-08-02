# TGQ Android — Prediksi Togel Cerdas

Aplikasi Android (WebView wrapper) untuk TGQ. Build APK otomatis via GitHub Actions.

## Struktur
- `android/` — Project Gradle Android (WebView wrapper)
- `index.html` — Landing page UI (juga di-copy ke `android/app/src/main/assets/`)
- `.github/workflows/build-apk.yml` — Workflow build APK otomatis

## Build
APK di-build otomatis oleh GitHub Actions setiap push ke `main/master` (jika ada perubahan di `android/`).

Download APK dari tab **Actions** → workflow terakhir → artifacts `tgq-app-debug` / `tgq-app-release`.

Build manual:
```bash
cd android
./gradlew assembleDebug
```
