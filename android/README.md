# TGQ Android Wrapper App

Aplikasi Android (WebView Wrapper) untuk TGQ — Prediksi Togel Cerdas.

## Struktur Direktori

- `app/src/main/assets/index.html` — Landing page UI premium (Dark Glassmorphism).
- `app/src/main/java/com/tgq/app/MainActivity.java` — WebView loader dengan JavaScript & DomStorage diaktifkan.
- `app/src/main/AndroidManifest.xml` — Konfigurasi izin `INTERNET` dan `ACCESS_NETWORK_STATE`.

## Cara Build APK

### 1. Menggunakan Command Line (Gradle Wrapper):
```bash
cd android
./gradlew assembleDebug
```
Hasil APK debug akan berada di: `app/build/outputs/apk/debug/app-debug.apk`

Untuk Build APK Release:
```bash
./gradlew assembleRelease
```

### 2. Menggunakan Android Studio:
1. Buka Android Studio.
2. Pilih **Open an existing Android Studio project**.
3. Arahkan ke folder `/home/milklho/.gemini/antigravity-cli/scratch/tgq_android/android`.
4. Jalankan atau klik **Build > Build APK(s)**.
